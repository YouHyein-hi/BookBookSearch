package com.example.bookbooksearch.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.bookbooksearch.MainViewModel
import com.example.bookbooksearch.dataClass.BottomNavItem
import com.example.bookbooksearch.ui.Nav.BottomNavigationBar
import com.example.bookbooksearch.ui.Nav.Navigation
import com.example.bookbooksearch.ui.theme.BookbookSearchTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        items = listOf(
                            BottomNavItem(
                                name = "search",
                                route = "search",
                                icon = Icons.Default.Search
                            ),
                            BottomNavItem(
                                name = "favorite",
                                route = "favorite",
                                icon = Icons.Default.Favorite
                            )
                        ),
                        navController = navController,
                        onItemClick = {
                            navController.navigate(it.route)
                        })
                }
            ) {
                val viewModel : MainViewModel by viewModels()
                Navigation(navController = navController, viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookbookSearchTheme {

    }
}