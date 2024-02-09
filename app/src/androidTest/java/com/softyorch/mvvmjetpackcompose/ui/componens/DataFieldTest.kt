package com.softyorch.mvvmjetpackcompose.ui.componens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.text.input.KeyboardType
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DataFieldTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun onBefore() {
        composeTestRule.setContent {
            var text by remember { mutableStateOf(value = "TextTest") }
            var error by remember { mutableStateOf(value = false) }
            error = text.length < 3

            DataField(
                label = "LabelTest",
                text = text,
                error = error,
                supportingText = "SupportTextTest",
                leadingIcon = Icons.Default.PrivacyTip,
                keyboardType = KeyboardType.Text,
                onTextChange = { text = it }
            )
        }
    }


    @Test
    fun whenDataTextFieldExistAndIsDisplayedAndEnabled() {
        composeTestRule.onNodeWithTag("TextField").assertExists()
        composeTestRule.onNodeWithTag("TextField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("TextField").assertIsEnabled()
    }

    @Test
    fun whenDataTextFieldIsClicked() {
        composeTestRule.onNodeWithTag("TextField").performClick()
        composeTestRule.onNodeWithTag("TextField").assertIsFocused()
    }

    @Test
    fun whenDataTextFieldHasText() {
        composeTestRule.onNodeWithTag("TextField").assertTextContains("TextTest")
        composeTestRule.onNodeWithTag("TextField").performTextClearance()
        composeTestRule.onNodeWithTag("TextField").assertTextContains("")
    }

    @Test
    fun whenDataTextFieldChangingText() {
        composeTestRule.onNodeWithTag("TextField").performTextReplacement("Test")
        composeTestRule.onNodeWithTag("TextField").assertTextContains("Test")
    }
}