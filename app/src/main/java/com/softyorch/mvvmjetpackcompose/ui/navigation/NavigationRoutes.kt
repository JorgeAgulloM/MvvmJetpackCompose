package com.softyorch.mvvmjetpackcompose.ui.navigation

import com.softyorch.mvvmjetpackcompose.utils.USER_ID

sealed class NavigationRoutes(val route: String) {
    enum class Routes {
        SPLASH,
        MAIN,
        USER_DETAIL
    }

    data object SplashScreen: NavigationRoutes(route = Routes.SPLASH.name)
    data object MainScreen: NavigationRoutes(route = Routes.MAIN.name)
    data object UserDetailScreen: NavigationRoutes(route = "${Routes.USER_DETAIL.name}?${USER_ID}={${USER_ID}}") {
        fun createRoute(userId: String) = "${Routes.USER_DETAIL.name}?${USER_ID}=$userId"
    }
}
