package com.bruno13palhano.hqsmarvel.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bruno13palhano.hqsmarvel.ui.screens.characters.CharactersRoute
import com.bruno13palhano.hqsmarvel.ui.screens.home.HomeRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    showBottomMenu: (show: Boolean) -> Unit
) {
    navigation<MainRoutes.Home>(startDestination = HomeRoutes.Main) {
        composable<HomeRoutes.Main> {
            HomeRoute(
                onItemClick = {
                    navController.navigate(HomeRoutes.Characters(it))
                },
                showBottomMenu = showBottomMenu
            )
        }

        composable<HomeRoutes.Characters> {
            val id = it.toRoute<HomeRoutes.Characters>().id

            CharactersRoute(
                id = id,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}

sealed interface HomeRoutes {
    @Serializable
    data object Main : HomeRoutes

    @Serializable
    data class Characters(val id: Long) : HomeRoutes
}