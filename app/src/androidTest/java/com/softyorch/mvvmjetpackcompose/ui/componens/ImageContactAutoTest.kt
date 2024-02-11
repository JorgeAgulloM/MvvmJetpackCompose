package com.softyorch.mvvmjetpackcompose.ui.componens

import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.softyorch.mvvmjetpackcompose.R
import com.softyorch.mvvmjetpackcompose.utils.LOGO_IMAGE
import com.softyorch.mvvmjetpackcompose.utils.LOGO_TEXT
import org.junit.Rule
import org.junit.Test

class ImageContactAutoTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLogoStart_thenHasNotImageAndText() {
        composeTestRule.setContent {
            ImageContactAuto(
                contactImage = null,
                contactLogo = null,
                contactLogoColor = null
            )
        }

        composeTestRule.onNodeWithTag(LOGO_IMAGE).assertDoesNotExist()
        composeTestRule.onNodeWithTag(LOGO_TEXT).assertExists()
    }

    @Test
    fun whenLogoCreated_thenHasTextAndColor() {
        composeTestRule.setContent {
            ImageContactAuto(
                contactImage = null,
                contactLogo = "J",
                contactLogoColor = MaterialTheme.colorScheme.primary.value.toLong()
            )
        }

        composeTestRule.onNodeWithTag(LOGO_IMAGE).assertDoesNotExist()
        composeTestRule.onNodeWithTag(LOGO_TEXT).apply {
            assertExists()
            assertIsDisplayed()
            assertTextContains("J")
        }
    }

    @Test
    fun whenLogoCreatedWithImageExist() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val imageUri = Uri.parse(
                "android.resource://${
                    context.packageName
                }/${
                    R.drawable.jetpack_compose
                }"
            ).toString()

            ImageContactAuto(
                contactImage = imageUri,
                contactLogo = null,
                contactLogoColor = null
            )
        }

        composeTestRule.onNodeWithTag(LOGO_TEXT).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag(LOGO_IMAGE).apply {
            assertExists()
            assertIsDisplayed()
        }
    }
}
