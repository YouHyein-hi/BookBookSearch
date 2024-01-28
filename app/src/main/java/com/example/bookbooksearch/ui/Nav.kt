package com.example.bookbooksearch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Badge
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookbooksearch.MainViewModel
import com.example.bookbooksearch.dataClass.BottomNavItem

object Nav {

    // 네비게이션 컨트롤러 정의
    @Composable
    fun Navigation(navController: NavHostController, viewModel: MainViewModel){
        NavHost(navController = navController, startDestination = "search") {
            composable("search") {
                SearchScreen(viewModel, navController)
            }
            composable("favorite") {
                FavoriteScreen()
            }
            composable("detail"){
                DetailScreen(viewModel)
            }
        }
    }

    // 하단 네이게이션 바 구성
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomNavigationBar(
        items: List<BottomNavItem>, navController: NavHostController,
        modifier: Modifier = Modifier, onItemClick: (BottomNavItem) -> Unit
    ) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        BottomNavigation (
            modifier = modifier,
            backgroundColor = Color(0xFF5AB166),
            elevation = 5.dp
        ) {
            // items 배열에 담긴 모든 항목을 추가합니다.
            items.forEach { item ->
                // 뷰의 활동 상태를 백스택에 담아 저장합니다.
                val selected = item.route == backStackEntry.value?.destination?.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color(0xFFBABBBA),
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (item.badgeCount > 0) {
                                BadgedBox(badge = { Badge { Text(text = item.badgeCount.toString()) } }) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                            if (selected) {
                                Text(
                                    text = item.name,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}