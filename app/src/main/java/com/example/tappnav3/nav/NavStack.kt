package com.example.tappnav3.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.tappnav3.screen.home.HomeScreen
import com.example.tappnav3.screen.story.StoryViewer
import kotlinx.serialization.Serializable

@Serializable
data class StoryViewerScreen(val id: Int) : NavKey

@Composable
fun AppNavGraphs(backStack: NavBackStack, modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<BottomBarNav.HomeScreen> {
                HomeScreen(backStack = backStack)
            }

            entry<StoryViewerScreen> { key ->
                StoryViewer(storyId = key.id, backStack = backStack)
            }

            entry<BottomBarNav.PayScreen> { }
            entry<BottomBarNav.CityScreen> { }
            entry<BottomBarNav.ChatScreen> { }
            entry<BottomBarNav.MoreScreen> { }
        }
    )
}