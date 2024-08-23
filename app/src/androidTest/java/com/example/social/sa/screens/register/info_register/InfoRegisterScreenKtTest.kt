package com.example.social.sa.screens.register.info_register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test


class InfoRegisterScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ClickSubmit_ShowPassword() {
        composeTestRule.setContent {
            var shouldShowPassword by remember {
                mutableStateOf(false)
            }
            InfoRegisterScreen(
                infoRegisterState = InfoRegisterState(),
                showPassword = shouldShowPassword, onInfoRegisterEvent = {
                    when(it){
                        is InfoRegisterEvent.ArgChanges -> TODO()
                        is InfoRegisterEvent.EmailChanged -> TODO()
                        is InfoRegisterEvent.ImageChanged -> TODO()
                        is InfoRegisterEvent.NameChanged -> TODO()
                        is InfoRegisterEvent.PasswordChanged -> TODO()
                        InfoRegisterEvent.Submit -> {
                            shouldShowPassword = !shouldShowPassword
                        }
                        is InfoRegisterEvent.UserNameChanged -> TODO()
                    }
                }
            )
        }
        composeTestRule.onNodeWithText("password").assertDoesNotExist()
        composeTestRule.onNodeWithText("Submit").performClick()
        composeTestRule.onNodeWithText("password").assertExists()
    }
}