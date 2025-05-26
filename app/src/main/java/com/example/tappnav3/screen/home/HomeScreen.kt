package com.example.tappnav3.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import com.example.tappnav3.R
import com.example.tappnav3.nav.StoryViewerScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    backStack: NavBackStack,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Display content based on state (loading, success, error)
    when {
        state.isLoading -> LoadingScreen(modifier = Modifier)
        state.error != null -> Text(state.error!!)
        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                SearchBox()
                UserName()

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.stories.forEach { story ->
                        StoryListScreen(
                            img = story.imageResId,
                            onNavigateClick = {
                                backStack.add(StoryViewerScreen(story.id))
                            }
                        )
                    }
                }

                Row {
                    TransactionBlock()
                    Spacer(modifier = Modifier.weight(1f))
                    DiscountBlock()
                }

                CardBlock()
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = { CircularProgressIndicator() }
    )
}

@Composable
fun CardBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1c1c1e)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.money),
                modifier = Modifier.size(50.dp),
                contentDescription = null
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "400 Р",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = "Black",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.black),
                        modifier = Modifier.size(width = 42.dp, height = 36.dp),
                        contentDescription = null
                    )
                    Image(
                        painter = painterResource(id = R.drawable.mir),
                        modifier = Modifier.size(width = 42.dp, height = 36.dp),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun DiscountBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1c1c1e))
    ) {
        Column(
            modifier = modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                Text(
                    text = "Кешбек\nи бонусы",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "3", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mvideo),
                        contentDescription = null
                    )
                }
                Box(
                    modifier = modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null
                    )
                }
                Box(
                    modifier = modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.coffe),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(180.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1c1c1e))
    ) {
        Column(modifier = modifier.padding(20.dp)) {
            Text(
                text = "Все операции",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            Text(text = "Трат в сентябре", color = Color.White)
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Text(text = "806 Р", fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF4ec7dc))
            )
        }
    }
}

@Composable
fun StoryListScreen(
    modifier: Modifier = Modifier,
    img: Int,
    onNavigateClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(22.dp))
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(22.dp),
                    color = Color(0xFF2a6cda)
                )
                .clickable {
                    onNavigateClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = img),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }
    }
}

@Composable
fun UserName(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(26.dp)),
        ) {
            Image(
                painter = painterResource(id = R.drawable.da),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text(
            text = "Mark",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = Color.White,
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.size(40.dp)) {
            Image(painter = painterResource(id = R.drawable.surprise), contentDescription = null)
        }
    }
}

@Composable
fun SearchBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF363636)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color(0xFF8c9399),
                modifier = Modifier.size(16.dp),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(text = "Банкоматы", color = Color(0xFF8c9399), fontSize = 16.sp)
        }
    }
}
