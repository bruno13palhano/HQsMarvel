package com.bruno13palhano.hqsmarvel

import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import androidx.test.espresso.Espresso
import com.bruno13palhano.hqsmarvel.ui.common.BottomMenu
import com.bruno13palhano.hqsmarvel.ui.navigation.MainNavGraph
import com.bruno13palhano.hqsmarvel.ui.navigation.MainRoutes
import com.bruno13palhano.hqsmarvel.ui.theme.HQsMarvelTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainNavGraphTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            HQsMarvelTheme {
                var showBottomMenu by rememberSaveable { mutableStateOf(true) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = showBottomMenu,
                                enter =
                                    slideInVertically(
                                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                        initialOffsetY = { it / 8 }
                                    ),
                                exit =
                                    slideOutVertically(
                                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                        targetOffsetY = { it / 8 }
                                    )
                            ) {
                                BottomMenu(navController = navController)
                            }
                        }
                    ) {
                        MainNavGraph(
                            modifier = Modifier.padding(it),
                            navController = navController
                        ) { show ->
                            showBottomMenu = show
                        }
                    }
                }
            }
        }
    }

    @Test
    fun verifyStartDestination() {
        composeTestRule.onNodeWithContentDescription("Home screen").assertExists()
    }

    @Test
    fun onFavoriteClicked_FromHomeScreen_shouldNavigateToFavoriteComicsScreen() {
        composeTestRule.onNodeWithContentDescription("Favorite Comics", useUnmergedTree = true)
            .performClick()

        val route = navController.currentBackStackEntry?.toRoute<MainRoutes.FavoriteComics>()
        val expectedRoute = MainRoutes.FavoriteComics

        composeTestRule.onNodeWithContentDescription("Favorite comics screen").assertExists()
        assert(route == expectedRoute)
    }

    @Test
    fun onNavigateBackClicked_FromFavoriteComicsScreen_shouldNavigateBackToHomeScreen() {
        composeTestRule.onNodeWithContentDescription("Favorite Comics", useUnmergedTree = true)
            .performClick()

        Espresso.pressBack()

        val route = navController.currentBackStackEntry?.toRoute<MainRoutes.Home>()
        val expectedRoute = MainRoutes.Home

        composeTestRule.onNodeWithContentDescription("Home screen").assertExists()
        assert(route == expectedRoute)
    }
}