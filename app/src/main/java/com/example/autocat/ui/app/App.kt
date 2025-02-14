package com.example.autocat.ui.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autocat.di.appModule
import com.example.autocat.ui.history.HistoryScreenRoot
import com.example.autocat.ui.settings.SettingsScreenRoot
import com.example.autocat.ui.theme.AutoCatTheme
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

data class BottomNavigationItem(
    val route: Route,
    val title: String,
    val icon: ImageVector,
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Route.History,
        "History",
        Icons.Outlined.AccountBox
    ),
    BottomNavigationItem(
        Route.Settings,
        "Settings",
        Icons.Outlined.Settings
    ),
)

@Composable
fun App() {
    val navController = rememberNavController()
    var selectedBottomBarIndex by rememberSaveable { mutableIntStateOf(0) }
    AutoCatTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomNavigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(selected = selectedBottomBarIndex == index,
                            onClick = {
                                selectedBottomBarIndex = index
                                navController.navigate(item.route)
                            },
                            icon = {
                                Icon(imageVector = item.icon, contentDescription = item.title)
                            },
                            label = {
                                Text(item.title)
                            })
                    }
                }
            }
        ) { padding ->
            NavHost(navController = navController, startDestination = Route.History) {
                composable<Route.History> {
                    HistoryScreenRoot()
                }
                composable<Route.Settings> {
                    SettingsScreenRoot()
                }
            }
        }
    }
}