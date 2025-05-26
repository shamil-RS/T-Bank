package com.example.tappnav3.screen.story

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tappnav3.R
import com.example.tappnav3.data.StoryRepository
import com.example.tappnav3.screen.home.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class StoryViewerState(
    val story: Story? = null,
    val comments: List<Comment> = emptyList(),
    val replies: MutableList<Comment> = mutableListOf(),
    val reactions: List<Reactions> = Reactions.reactionData,
    val isLoading: Boolean = false, val error: String? = null,

    val displayState: StoryViewerIntent
)

sealed class StoryViewerIntent {
    data class LoadStory(val story: Story?) : StoryViewerIntent()
    data class LoadStory1(val comments: List<Comment>) : StoryViewerIntent()
    data class AddComment(val commentText: String, val replyToId: Int? = null) : StoryViewerIntent()
    data class UpdateReaction(val commentIndex: Int, val reactionId: Int) : StoryViewerIntent()
    data class CopyComment(val commentText: String) : StoryViewerIntent()
    data class EditComment(val index: Int, val newText: String) : StoryViewerIntent()

    data object Loading : StoryViewerIntent()
    data object Error : StoryViewerIntent()
}

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: StoryRepository,
    private val clipboardManager: ClipboardManager
) : ViewModel() {

    private val _state = MutableStateFlow(
        StoryViewerState(
            isLoading = true,
            displayState = StoryViewerIntent.Loading
        )
    )
    val state: StateFlow<StoryViewerState> = _state.asStateFlow()
    var commentIdCounter = 0

    fun handleIntent(intent: StoryViewerIntent) {
        viewModelScope.launch {
            when (intent) {
                is StoryViewerIntent.LoadStory -> {}
                is StoryViewerIntent.LoadStory1 -> {}
                is StoryViewerIntent.AddComment -> addComment(intent.commentText, intent.replyToId)
                is StoryViewerIntent.UpdateReaction -> updateReaction(
                    intent.commentIndex,
                    intent.reactionId
                )

                is StoryViewerIntent.CopyComment -> copyComment(intent.commentText)
                is StoryViewerIntent.EditComment -> editComment(intent.index, intent.newText)

                is StoryViewerIntent.Loading -> {}
                is StoryViewerIntent.Error -> {}
            }
        }
    }

    private fun editComment(index: Int, newText: String) {
        val updatedComments = _state.value.comments.mapIndexed { i, comment ->
            if (i == index) {
                comment.copy(text = newText, edited = true)
            } else {
                comment
            }
        }
        _state.value = _state.value.copy(comments = updatedComments)
    }

    suspend fun loadStory(storyId: Int) {
            _state.value = _state.value.copy(isLoading = true) // Setting the download status

            try {
                // Uploading a story
                val storyResult = repository.getStory(storyId)
                storyResult.fold(
                    onSuccess = { story ->
                        // Updating the status with the uploaded history
                        _state.update {
                            it.copy(
                                story = story,
                                displayState = StoryViewerIntent.LoadStory(story)
                            )
                        }
                    },
                    onFailure = { error ->
                        // Handling the history upload error
                        _state.update {
                            it.copy(
                                error = error.message,
                                displayState = StoryViewerIntent.Error
                            )
                        }
                    }
                )

                // Uploading comments
                val commentsResult = repository.getCommentsForStory(storyId)
                commentsResult.fold(
                    onSuccess = { comments ->
                        // Updating the status with uploaded comments
                        _state.update {
                            it.copy(
                                comments = comments,
                                displayState = StoryViewerIntent.LoadStory1(comments)
                            )
                        }
                    },
                    onFailure = { error ->
                        // Handling the error of uploading comments
                        _state.update {
                            it.copy(
                                error = error.message,
                                displayState = StoryViewerIntent.Error
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                // Exception handling
                _state.update {
                    it.copy(
                        error = e.message,
                        displayState = StoryViewerIntent.Error
                    )
                }
            } finally {
                _state.value = _state.value.copy(isLoading = false) // Устанавливаем состояние загрузки в false
            }
        }

    private fun addComment(commentText: String, replyToId: Int? = null) {
        val newComment = Comment(
            id =  commentIdCounter,
            userName = "Mark",
            text = commentText,
            timestamp = LocalDateTime.now(),
            replyToId = replyToId // Setting the ID of the comment that we are responding to
        )
        _state.value = _state.value.copy(comments = _state.value.comments + newComment)
    }

    private fun updateReaction(commentIndex: Int, reactionId: Int) {
        val updatedComments = _state.value.comments.mapIndexed { index, comment ->
            if (index == commentIndex) {
                comment.copy(reaction = if (reactionId == 0) 0 else reactionId)
            } else {
                comment
            }
        }
        _state.value = _state.value.copy(comments = updatedComments)
    }

    private fun copyComment(commentText: String) {
        val clip = ClipData.newPlainText("Comment Text", commentText)
        clipboardManager.setPrimaryClip(clip)
    }

    fun getCommentCountText(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "$count Комментарий"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "$count Комментария"
            else -> "$count Комментариев"
        }
    }
}

data class CommentUiState(
    val allComment: List<Comment> = emptyList(),
) {
    companion object {
        val mock = listOf(
            CommentUiState(
                allComment = listOf(
                    Comment(
                        id = 1,
                        text = "My Love Game.",
                        userName = "May Gera",
                    ),
                    Comment(
                        id = 2,
                        text = "Early reviews from critics and influencers can shape public perception before a game’s release. " +
                                "Positive previews can elevate expectations significantly, " +
                                "while negative feedback can lead to skepticism.\n",
                        userName = "John Genry",
                    ),
                )
            ),
            CommentUiState(
                allComment = listOf(
                    Comment(
                        id = 3,
                        text = "In most cases, expectations were high",
                        userName = "John Poesano",
                    )
                )
            )
        )
    }
}

data class Comment(
    val id: Int,
    val text: String,
    val userName: String = "",
    val reaction: Int = 0,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val edited: Boolean = false, // Adding an edit tracking field
    val replyToId: Int? = null
)

data class Reactions(val id: Int = 0, val reaction: Int = 0) {
    companion object {
        val reactionData = listOf(
            Reactions(1, R.drawable.like),
            Reactions(2, R.drawable.neutral),
            Reactions(3, R.drawable.laughter),
            Reactions(4, R.drawable.sad),
            Reactions(5, R.drawable.bad_reactions),
        )
    }
}