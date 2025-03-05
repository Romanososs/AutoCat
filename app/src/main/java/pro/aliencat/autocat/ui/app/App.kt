package pro.aliencat.autocat.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import pro.aliencat.autocat.R
import pro.aliencat.autocat.ui.check.CheckScreenRoot
import pro.aliencat.autocat.ui.history.HistoryScreenRoot
import pro.aliencat.autocat.ui.records.RecordsScreenRoot
import pro.aliencat.autocat.ui.search.SearchScreenRoot
import pro.aliencat.autocat.ui.search.filter.FilterScreenRoot
import pro.aliencat.autocat.ui.search.filter.list.FilterListScreenRoot
import pro.aliencat.autocat.ui.settings.SettingsScreenRoot
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import kotlin.reflect.typeOf

data class BottomNavigationItem(
    val route: Route,
    val title: String,
    val icon: Int,
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Route.History,
        "History",//TODO to resources
        R.drawable.history_24dp
    ),
    BottomNavigationItem(
        Route.Records,
        "Records",
        R.drawable.voicemail_24dp
    ),
    BottomNavigationItem(
        Route.Add,
        "",
        R.drawable.add_24dp
    ),
    BottomNavigationItem(
        Route.Search,
        "Search",
        R.drawable.search_24dp
    ),
    BottomNavigationItem(
        Route.Settings,
        "Settings",
        R.drawable.settings_24dp
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
                                Icon(painterResource(item.icon), contentDescription = item.title)
                            },
                            label = {
                                Text(item.title)
                            })
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Route.History,
                modifier = Modifier.padding(padding)
            ) {
                composable<Route.History> {
                    HistoryScreenRoot()
                }
                composable<Route.Records> {
                    RecordsScreenRoot()
                }
                dialog<Route.Add>{
                    CheckScreenRoot {
                        navController.popBackStack()
                    }
                }
                navigation<Route.SearchGraph>(
                    startDestination = Route.Search
                ) {
                    composable<Route.Search> {
                        SearchScreenRoot(
                            it.sharedKoinViewModel(navController), {
                                navController.navigate(Route.Filter)
                            }, {
                                //TODO
                            })
                    }
                    composable<Route.Filter> {
                        FilterScreenRoot(
                            it.sharedKoinViewModel(navController), { type ->
                                navController.navigate(Route.FilterList(type))
                            }, {
                                navController.popBackStack()
                            })
                    }
                    composable<Route.FilterList> {
                        FilterListScreenRoot(
                            it.toRoute<Route.FilterList>().type,
                            it.sharedKoinViewModel(navController)
                        ) {
                            navController.popBackStack()
                        }
                    }
                }
                composable<Route.Settings> {
                    SettingsScreenRoot()
                }
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}