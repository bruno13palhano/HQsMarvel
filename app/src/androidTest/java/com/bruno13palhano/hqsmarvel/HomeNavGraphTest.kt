package com.bruno13palhano.hqsmarvel

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import com.bruno13palhano.hqsmarvel.ui.navigation.HomeRoutes
import com.bruno13palhano.hqsmarvel.ui.navigation.MainNavGraph
import com.bruno13palhano.hqsmarvel.ui.theme.HQsMarvelTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeNavGraphTest {
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavGraph(navController = navController) {}
                }
            }
        }
    }

    @Test
    fun verifyStartDestination() {
        composeTestRule.onNodeWithContentDescription("Home screen").assertExists()
    }

    @Test
    fun onItemClicked_FromHomeScreen_ShouldNavigateToCharactersScreen() {
        composeTestRule.onNodeWithContentDescription("List of comics")
            .performScrollToIndex(6)
            .onChildAt(6)
            .performClick()

        composeTestRule.onNodeWithContentDescription("See characters")
            .performClick()

        val route = navController.currentBackStackEntry?.toRoute<HomeRoutes.Characters>()
        val expectedRoute = HomeRoutes.Characters(route!!.comicId)

        assert(route == expectedRoute)
    }

    @Test
    fun onNavigateBackClicked_FromCharactersScreen_ShouldNavigateBackToHomeScreen() {
        composeTestRule.onNodeWithContentDescription("List of comics")
            .performScrollToIndex(6)
            .onChildAt(6)
            .performClick()

        composeTestRule.onNodeWithContentDescription("See characters")
            .performClick()

        composeTestRule.onNodeWithContentDescription("Navigate back")
            .performClick()

        val route = navController.currentBackStackEntry?.toRoute<HomeRoutes.Main>()
        val expectedRoute = HomeRoutes.Main

        assert(route == expectedRoute)
    }

    @Test
    fun onItemClicked_FromCharactersScreen_ShouldNavigateToCharacterScreen() {
        composeTestRule.onNodeWithContentDescription("List of comics")
            .performScrollToIndex(6)
            .onChildAt(6)
            .performClick()

        composeTestRule.onNodeWithContentDescription("See characters")
            .performClick()

        composeTestRule.onNodeWithContentDescription("List of characters")
            .onChildren()
            .onFirst()
            .performClick()

        val route = navController.currentBackStackEntry?.toRoute<HomeRoutes.Character>()
        val expectedRoute = HomeRoutes.Character(route!!.id)

        assert(route == expectedRoute)
    }

    @Test
    fun onNavigateBackClicked_FromCharacterScreen_ShouldNavigateBackToCharactersScreen() {
        composeTestRule.onNodeWithContentDescription("List of comics")
            .performScrollToIndex(6)
            .onChildAt(6)
            .performClick()

        composeTestRule.onNodeWithContentDescription("See characters")
            .performClick()

        composeTestRule.onNodeWithContentDescription("List of characters")
            .onChildren()
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithContentDescription("Navigate back")
            .performClick()

        val route = navController.currentBackStackEntry?.toRoute<HomeRoutes.Characters>()
        val expectedRoute = HomeRoutes.Characters(route!!.comicId)

        assert(route == expectedRoute)
    }
}