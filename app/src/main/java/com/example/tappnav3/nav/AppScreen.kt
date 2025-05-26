package com.example.tappnav3.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.tappnav3.nav.BottomBarNav.HomeScreen

@Composable
fun AppScreen() {
    val backStack = rememberNavBackStack(HomeScreen)

    val isBottomBarShow = backStack.lastOrNull() is StoryViewerScreen

    Scaffold(
        bottomBar = {
            AnimatedVisibility(!isBottomBarShow) {
                TBottomBar(backStack = backStack, modifier = Modifier)
            }
        },
    ) { innerPadding ->
        AppNavGraphs(backStack = backStack,modifier = Modifier.padding(innerPadding))
    }
}