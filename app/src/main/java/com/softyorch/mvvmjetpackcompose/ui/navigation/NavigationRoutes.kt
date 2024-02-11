package com.softyorch.mvvmjetpackcompose.ui.navigation

import com.softyorch.mvvmjetpackcompose.utils.CONTACT_ID

sealed class NavigationRoutes(val route: String) {
    enum class Routes {
        SPLASH,
        MAIN,
        CONTACT_DETAIL,
        SEARCH
    }

    data object SplashScreen: NavigationRoutes(route = Routes.SPLASH.name)
    data object MainScreen: NavigationRoutes(route = Routes.MAIN.name)
    data object ContactDetailScreen: NavigationRoutes(route = "${Routes.CONTACT_DETAIL.name}?${CONTACT_ID}={${CONTACT_ID}}") {
        fun createRoute(contactId: String) = "${Routes.CONTACT_DETAIL.name}?${CONTACT_ID}=$contactId"
    }
    data object SearchScreen: NavigationRoutes(route = Routes.SEARCH.name)
}
