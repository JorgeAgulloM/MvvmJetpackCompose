package com.softyorch.mvvmjetpackcompose.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.softyorch.mvvmjetpackcompose.R
import com.softyorch.mvvmjetpackcompose.ui.navigation.NavigationRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            sleep(1000L)
            withContext(Dispatchers.Main) {
                navController.navigate(NavigationRoutes.MainScreen.route) {
                    navController.popBackStack()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.jetpack_compose),
            contentDescription = stringResource(R.string.splash_screen_content_desc_icon)
        )
    }
}
