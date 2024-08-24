package com.youeefkhalaj.frosilis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.youeefkhalaj.frosilis.ui.calendar.CalendarEditDestination
import com.youeefkhalaj.frosilis.ui.calendar.CalendarEditScreen
import com.youeefkhalaj.frosilis.ui.home.HomeDestination
import com.youeefkhalaj.frosilis.ui.home.HomeOverviewDestination
import com.youeefkhalaj.frosilis.ui.home.HomeOverviewScreen
import com.youeefkhalaj.frosilis.ui.home.HomeScreen
import com.youeefkhalaj.frosilis.ui.item.PersonalDetailsScreen
import com.youeefkhalaj.frosilis.ui.item.PersonalEditDestination
import com.youeefkhalaj.frosilis.ui.item.PersonalEditScreen
import com.youeefkhalaj.frosilis.ui.item.PersonalItemDetailsDestination
import com.youeefkhalaj.frosilis.ui.item.PersonalItemEntryDestination
import com.youeefkhalaj.frosilis.ui.item.PersonalItemEntryScreen

@Composable
fun FrosilisNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

        NavHost(
            navController = navController, startDestination = HomeDestination.route, modifier = modifier
        ) {


        composable(route = HomeDestination.route) {
            HomeScreen(navigateToItemEntry = { navController.navigate(PersonalItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate(
                        "${CalendarEditDestination.route}/$it"
                    )
                },
                navigateToItemeDetail = {
                    navController.navigate(
                        "${PersonalItemDetailsDestination.route}/$it"
                    )
                },
                navigateToHomeOverView = { navController.navigate(
                    HomeOverviewDestination.route
                )}
            )
        }
            composable(route = HomeOverviewDestination.route ){
                HomeOverviewScreen(navigateToHome = { navController.navigate(
                    HomeDestination.route
                )})
            }

        composable(route = PersonalItemEntryDestination.route) {

            PersonalItemEntryScreen(navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
               )
        }
        composable(
            route = PersonalItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(PersonalItemDetailsDestination.personalItemIdArg) {
                type = NavType.IntType
            })
        ) {
            PersonalDetailsScreen(
                navigateToEditItem = { navController.navigate("${PersonalEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = PersonalEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PersonalEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            PersonalEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = CalendarEditDestination.routeWithArgs,
            arguments = listOf(navArgument(CalendarEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            CalendarEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        }
    }
