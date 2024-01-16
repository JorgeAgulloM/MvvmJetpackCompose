package com.softyorch.mvvmjetpackcompose.ui.navigation

sealed class NavigationRoutes(val route: String) {
    enum class Routes {
        SPLASH,
        MAIN,
        USER_DETAIL
    }

    data object SplashScreen: NavigationRoutes(route = Routes.SPLASH.name)
    data object MainScreen: NavigationRoutes(route = Routes.MAIN.name)
    data object UserDetailScreen: NavigationRoutes(route = "${Routes.USER_DETAIL.name}?userId={userId}") {
        fun createRoute(userId: String) = "${Routes.USER_DETAIL.name}?userId=$userId"
    }
}
