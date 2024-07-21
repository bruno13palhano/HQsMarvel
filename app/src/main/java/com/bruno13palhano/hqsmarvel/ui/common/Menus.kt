package com.bruno13palhano.hqsmarvel.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bruno13palhano.hqsmarvel.R
import com.bruno13palhano.hqsmarvel.ui.navigation.MainRoutes

@Composable
fun BottomMenu(navController: NavController) {
    val items =
        listOf(
            Screen.Home,
            Screen.FavoriteComics
        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            val selected = currentDestination?.selectedRoute(screen = screen)

            NavigationBarItem(
                selected = selected == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = screen.icon, contentDescription = stringResource(id = screen.resourceId)) },
                label = { Text(text = stringResource(id = screen.resourceId)) }
            )
        }
    }
}

fun NavDestination.selectedRoute(screen: Screen<MainRoutes>): Boolean {
    return hierarchy.any {
        it.route?.split(".")?.lastOrNull() == screen.route.toString()
    }
}

sealed class Screen<T : MainRoutes>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Home : Screen<MainRoutes>(
        route = MainRoutes.Home,
        icon = Icons.Filled.Home,
        resourceId = R.string.home_label
    )

    data object FavoriteComics : Screen<MainRoutes>(
        route = MainRoutes.FavoriteComics,
        icon = Icons.Filled.Favorite,
        resourceId = R.string.favorite_comics_label
    )
}