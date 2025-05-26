@file:OptIn(ExperimentalMaterialApi::class)

package com.example.tappnav3.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tappnav3.R
import com.example.tappnav3.screen.story.Comment
import com.example.tappnav3.screen.story.Reactions
import com.example.tappnav3.screen.story.SwipeableStates
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@SuppressLint("NewApi")
@Composable
fun SendMessage(
    modifier: Modifier = Modifier,
    currentId: Int,
    initialText: String = "",
    focusRequester: FocusRequester,
    onSendMessage: (Comment) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(initialText) }

    LaunchedEffect(initialText) {
        text = initialText
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .width(332.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .focusRequester(focusRequester),
                textStyle = TextStyle.Default.copy(color = Color.White),
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            ) { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = text,
                    innerTextField = innerTextField,
                    singleLine = true,
                    enabled = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFF3e3e40)
                    ),
                    placeholder = {
                        Text(
                            text = "Комментарий",
                            style = MaterialTheme.typography.subtitle2,
                            color = Color.White.copy(0.3f),
                            maxLines = 1
                        )
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    visualTransformation = VisualTransformation.None
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xBA03CAFC))
                    .clickable(
                        onClick = {
                            if (text.isNotEmpty()) {
                                focusManager.clearFocus()
                                onSendMessage(
                                    Comment(
                                        id = currentId,
                                        text = text,
                                        timestamp = LocalDateTime.now()
                                    )
                                )
                                text = ""
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@SuppressLint("NewApi", "UnrememberedMutableState")
@Composable
fun MessageBlock(
    modifier: Modifier = Modifier,
    userName: String = "Mark",
    text: String = "Is the great musclecar",
    swipeUserName: String = "",
    swipeText: String = "",
    imgUser: Int = R.drawable.dark,
    icon: Int = R.drawable.dar,
    count: Int = 1,
    timestamp: LocalDateTime = LocalDateTime.now(),
    onClick: () -> Unit = {},
    onClickCopyText: (String) -> Unit = {},
    selectedReaction: Int? = null,
    isSelected: Boolean = false,
    isSwipeable: Boolean = false,
    onReactionSelected: (Int) -> Unit = {},
    onReply: () -> Unit = {},
    onSwipeableReply: () -> Unit = {},
    onEdit: () -> Unit = {},
) {
    val swipeableState = rememberSwipeableState(initialValue = SwipeableStates.Collapsed)
    val width = 350.dp
    val squareSize = 50.dp
    val sizePx = with(LocalDensity.current) { (width - squareSize).toPx() }
    val iconAlpha by derivedIconAlpha(swipeableState, sizePx)
    val anchor = mapOf(
        0f to SwipeableStates.Collapsed,
        -sizePx / 2 to SwipeableStates.Expanded
    )

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = timestamp.format(formatter)

    val showReaction = remember { mutableStateOf(false) }
    val reaction = Reactions()

    val newText = remember { mutableStateOf(text) }

    LaunchedEffect(swipeableState.currentValue) {
        when(swipeableState.currentValue) {
            SwipeableStates.Collapsed -> { }

            SwipeableStates.Expanded -> {
                if (swipeableState.currentValue == SwipeableStates.Expanded) {
                    onSwipeableReply()
                    swipeableState.animateTo(SwipeableStates.Collapsed)
                }
            }
        }
    }

    LaunchedEffect(selectedReaction) {
        showReaction.value = selectedReaction != null
    }

    Row(
        modifier = modifier
            .swipeable(
                state = swipeableState,
                anchors = anchor,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.Bottom)
        ) {
            Image(
                painter = painterResource(id = imgUser),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF3e3e40))
                .clickable { onClick() }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnimatedVisibility(visible = isSwipeable) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 14.dp,
                                    bottomStart = 8.dp,
                                    bottomEnd = 4.dp
                                )
                            )
                            .background(Color(0xFF53C3D2).copy(0.7f))
                            .drawBehind {
                                val gradientBrush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 1f),
                                        Color.White.copy(alpha = 0f)
                                    ),
                                    startY = 0f,
                                    endY = size.height
                                )

                                drawRect(
                                    brush = gradientBrush,
                                    topLeft = Offset(0f, 0f),
                                    size = Size(4.dp.toPx(), size.height)
                                )
                            },
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = swipeUserName,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4BD73B)
                            )
                            Text(
                                text = swipeText,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                color = Color.White
                            )
                        }
                    }
                }
                Row {
                    Text(text = userName, color = Color(0xFF3b78d7))
                    Spacer(modifier = Modifier.weight(1f))
                    if (newText.value != text) {
                        Text(text = "Изменено", color = Color(0xFF717273))
                    }
                }
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ответить",
                                color = Color(0xFF717273),
                                modifier = Modifier.clickable { onReply() }
                            )

                            if (userName == "Mark") {
                                Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = "Редактировать",
                                    tint = Color(0xFF717273),
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { onEdit() }
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = showReaction.value,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Reaction(
                                icon = icon,
                                count = count,
                                selectedReaction = selectedReaction,
                                onReactionSelected = { onReactionSelected(reaction.id) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clickable { onClickCopyText(text) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.copy),
                            tint = Color.Gray,
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = formattedTime,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Gray.copy(alpha = iconAlpha))
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.ic_reply),
                modifier = Modifier
                    .size(16.dp)
                    .alpha(iconAlpha),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun derivedIconAlpha(swipeableState: SwipeableState<SwipeableStates>, sizePx: Float): State<Float> {
    val targetAlpha by remember {
        derivedStateOf {
            val offset = swipeableState.offset.value
            val alpha = (-offset / (sizePx / 2)).coerceIn(0f, 1f)
            alpha
        }
    }

    return animateFloatAsState(targetValue = targetAlpha)
}

@Composable
fun Reaction(
    modifier: Modifier = Modifier,
    icon: Int,
    count: Int,
    selectedReaction: Int?,
    onReactionSelected: (Int) -> Unit
) {
    val reaction = Reactions()
    Box(
        modifier = modifier
            .width(72.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF21C4AC))
            .clickable {
                if (selectedReaction == reaction.id) {
                    // Remove reaction if already selected
                    onReactionSelected(0)
                } else {
                    // Add or change reaction
                    onReactionSelected(reaction.id)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )
            Text(text = "$count")
        }
    }
}

@Composable
fun ReactionSelectionBlock(
    modifier: Modifier = Modifier,
    reactions: List<Reactions>,
    selectedReaction: Int?,
    onReactionSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .background(Color(0xFF1c1c1e))
            .clip(RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Select a response", color = Color.White, fontSize = 16.sp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            reactions.forEach { reaction ->
                Image(
                    painter = painterResource(id = reaction.reaction),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (selectedReaction == reaction.id) {
                                // Remove reaction if already selected
                                onReactionSelected(0)
                            } else {
                                // Add or change reaction
                                onReactionSelected(reaction.id)
                            }
                        }
                )
            }
        }
    }
}