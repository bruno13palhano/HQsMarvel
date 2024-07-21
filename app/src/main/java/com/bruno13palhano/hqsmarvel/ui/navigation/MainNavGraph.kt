package com.bruno13palhano.hqsmarvel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bruno13palhano.hqsmarvel.ui.screens.favorites.FavoriteComicsRoute
import kotlinx.serialization.Serializable

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainRoutes.Home
    ) {
        homeNavGraph(
            navController = navController,
            showBottomMenu = showBottomMenu
        )

        composable<MainRoutes.FavoriteComics> {
            FavoriteComicsRoute()
        }
    }
}

sealed interface MainRoutes {
    @Serializable
    data object Home : MainRoutes

    @Serializable
    data object FavoriteComics : MainRoutes
}