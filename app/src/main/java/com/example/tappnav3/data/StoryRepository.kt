package com.example.tappnav3.data

import com.example.tappnav3.screen.home.MessageStory
import com.example.tappnav3.screen.home.Story
import com.example.tappnav3.screen.story.Comment
import com.example.tappnav3.screen.story.CommentUiState
import kotlinx.coroutines.delay
import javax.inject.Inject

interface StoryRepository {
    suspend fun getStories(): List<Story>
    suspend fun getDiscountStories(): List<Story>
    suspend fun getMessageStories(): List<MessageStory>
    suspend fun getStory(storyId: Int): Result<Story>
    suspend fun getCommentsForStory(storyId: Int): Result<List<Comment>>
}

class StoryRepositoryImpl @Inject constructor() : StoryRepository {

    override suspend fun getStories():List<Story> {
        // Simulate network delay
        delay(1000)
        return Story.storyList
    }

    override suspend fun getDiscountStories(): List<Story> {
        delay(1000)
        return Story.discountData
    }

    override suspend fun getMessageStories(): List<MessageStory> {
        delay(1000)
        return MessageStory.messageData
    }

    override suspend fun getStory(storyId: Int): Result<Story> {
        delay(1000)
        return try {
            val story = Story.storyData.first { it.id == storyId }
            Result.success(story)
        } catch (e: NoSuchElementException) {
            Result.failure(e)
        }
    }

    override suspend fun getCommentsForStory(storyId: Int): Result<List<Comment>> {
        delay(1000)
        return if (storyId == 1) {
            Result.success(CommentUiState.Companion.mock.first().allComment)
        } else {
            val e = Exception()
            Result.failure(exception = e)
        }
    }
}