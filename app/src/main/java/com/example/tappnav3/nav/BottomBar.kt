package com.example.tappnav3.nav

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.tappnav3.R
import kotlinx.serialization.Serializable

@Composable
fun TBottomBar(
    backStack: NavBackStack,
    modifier: Modifier = Modifier,
) {
    val currentScreen = backStack.lastOrNull()

    BottomAppBar(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
        backgroundColor = Color.Black,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = CenterVertically
        ) {
            bottomNavigationBarItems.forEach { screen ->
                BottomBarItem(
                    icon = screen.icon,
                    label = screen.label,
                    selected = currentScreen == screen,
                    onClick = { backStack.add(screen) }
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    @DrawableRes icon: Int = R.drawable.card_payment,
    label: String = "Главная",
    onClick: () -> Unit = {},
    selected: Boolean = false,
) {

    val colorT = if (selected) Color(0xFF3b78d7) else Color(0xFF767e85)

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = { onClick() }),
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Icon(
            modifier = Modifier
                .align(CenterHorizontally)
                .size(22.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = colorT
        )
        Text(text = label, color = colorT, fontWeight = FontWeight.Bold)
    }
}

val bottomNavigationBarItems = listOf(
    BottomBarNav.HomeScreen,
    BottomBarNav.PayScreen,
    BottomBarNav.CityScreen,
    BottomBarNav.ChatScreen,
    BottomBarNav.MoreScreen
)

@Serializable
sealed class BottomBarNav(
    val label: String,
    @DrawableRes val icon: Int,
) : NavKey {

    @Serializable
    object HomeScreen : BottomBarNav("Главная", R.drawable.card_payment)

    @Serializable
    object PayScreen : BottomBarNav("Платежи", R.drawable.points)

    @Serializable
    object CityScreen : BottomBarNav("Город", R.drawable.direction)

    @Serializable
    object ChatScreen : BottomBarNav("Чат", R.drawable.comment)

    @Serializable
    object MoreScreen : BottomBarNav("Еще", R.drawable.application)
}