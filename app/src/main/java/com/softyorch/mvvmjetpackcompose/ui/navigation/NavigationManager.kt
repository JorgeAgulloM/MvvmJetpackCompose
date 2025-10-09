package com.softyorch.mvvmjetpackcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softyorch.mvvmjetpackcompose.ui.screen.splash.SplashScreen
import com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.ContactDetailScreen
import com.softyorch.mvvmjetpackcompose.ui.screen.main.MainScreen
import com.softyorch.mvvmjetpackcompose.ui.screen.search.SearchScreen
import com.softyorch.mvvmjetpackcompose.utils.ToUUID
import com.softyorch.mvvmjetpackcompose.utils.CONTACT_ID

@Composable
fun NavigationManager(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = NavigationRoutes.SplashScreen.route) {
        composable(route = NavigationRoutes.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = NavigationRoutes.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = NavigationRoutes.ContactDetailScreen.route,
            arguments = listOf(navArgument(CONTACT_ID) { defaultValue = "unKnown" })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(CONTACT_ID)?.let {
                ContactDetailScreen(it.ToUUID()) { navController.popBackStack() }
            }
        }
        composable(route = NavigationRoutes.SearchScreen.route) {
            SearchScreen(
                viewModel = hiltViewModel(checkNotNull(LocalViewModelStoreOwner.current) {}, null),
                onDetailNav = { route -> navController.navigate(route) }) {
                navController.popBackStack()
            }
        }
    }
}
