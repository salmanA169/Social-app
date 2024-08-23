package com.example.social.sa.screens.register

import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.RegisterButton
import com.example.social.sa.component.RegisterIconButton
import com.example.social.sa.component.RegisterTextField
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.PreviewBothLightAndDark

// TODO: there is some issue when navigate to infoScreen then back to register screen it duplicated screen register
fun NavGraphBuilder.registerDest(navController: NavController) {
    composable<Screens.RegisterScreen>() {
        val registerViewModel = hiltViewModel<RegisterViewModel>()
        val context = LocalContext.current
        val state by registerViewModel.state.collectAsState()
        val effect by registerViewModel.effect.collectAsState()

        LaunchedEffect(key1 = effect) {
            when (effect) {
                is RegisterEffect.Navigate -> {
                    navController.navigate((effect as RegisterEffect.Navigate).route) {
                        popUpTo(Screens.RegisterScreen) {
                            inclusive = true
                        }
                    }
                }
                is RegisterEffect.ToastError -> {
                    Toast.makeText(
                        context,
                        (effect as RegisterEffect.ToastError).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                null -> Unit
                is RegisterEffect.NavigateToInfoRegister -> {
                    navController.navigate(
                        Screens.InfoRegisterRoute(
                            (effect as RegisterEffect.NavigateToInfoRegister).email,
                            (effect as RegisterEffect.NavigateToInfoRegister).userName,
                            (effect as RegisterEffect.NavigateToInfoRegister).imageUrl,
                            (effect as RegisterEffect.NavigateToInfoRegister).isGoogle
                        )
                    )
                }
            }
            registerViewModel.resetEffect()
        }
        RegisterScreen(state, registerViewModel::onEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    registerState: RegisterState,
    onRegisterType: (RegisterEvent) -> Unit
) {
    val registerTypes = remember {
        RegisterType.values().toList()
    }

    val registerIntentSender =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            onRegisterType(
                RegisterEvent.GoogleSignInResult(
                    it.data ?: return@rememberLauncherForActivityResult
                )
            )
        }

    LaunchedEffect(key1 = registerState.googleIntentSender) {
        if (registerState.googleIntentSender != null) {
            registerIntentSender.launch(
                IntentSenderRequest.Builder(registerState.googleIntentSender).build()
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .systemBarsPadding(),
    ) {
        Text(text = "App Name ", fontSize = 30.sp, modifier = Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh,
                    RoundedCornerShape(30f)
                )
                .padding(horizontal = 6.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            registerTypes.forEachIndexed { index, registerType ->
                InputChip(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    selected = registerType == registerState.registerType,
                    onClick = {
                        onRegisterType(RegisterEvent.RegisterTypeChange(registerType))
                    },
                    label = {
                        Text(
                            text = registerType.localizeString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    border = null,
                    colors = InputChipDefaults.inputChipColors(selectedContainerColor = MaterialTheme.colorScheme.surfaceDim)
                )
            }
        }
        Spacer(modifier = Modifier.height(34.dp))
        Crossfade(
            targetState = registerState.registerType,
            label = "",
            modifier = Modifier.fillMaxSize()
        ) {
            when (it) {
                RegisterType.SIGN_IN -> {
                    SignInSection(registerState.email, registerState.password, onRegisterType)
                }

                RegisterType.SIGN_UP -> {
                    SignupSection(email = registerState.email, onRegisterType)
                }
            }
        }
    }
}

@Composable
fun SignupSection(email: InputData, onRegisterType: (RegisterEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = CenterHorizontally,
    ) {
        RegisterTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(id = R.string.email_hint),
            isError = email.isError,
            supportText = email.errorText,
            value = email.content,
            onValueChange = {
                onRegisterType(RegisterEvent.EmailDataChange(it))
            },
            label = stringResource(id = R.string.email)
        )
        Spacer(modifier = Modifier.height(18.dp))
        RegisterButton(
            text = stringResource(id = R.string.sign_up),
            onClick = { onRegisterType(RegisterEvent.SignUp) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = stringResource(id = R.string.other_sign_in_options))
        Spacer(modifier = Modifier.height(18.dp))

        RegisterIconButton(icon = painterResource(id = R.drawable.google_icon)) {

        }

    }
}

@Composable
fun SignInSection(
    email: InputData,
    password: InputData,
    onRegisterType: (RegisterEvent) -> Unit
) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    val iconPassword = rememberSaveable(showPassword) {
        if (showPassword) R.drawable.visibility_icon else R.drawable.visibility_off_icon
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = CenterHorizontally,
    ) {
        RegisterTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(id = R.string.email_hint),
            isError = email.isError,
            supportText = email.errorText,
            value = email.content,
            onValueChange = {
                onRegisterType(RegisterEvent.EmailDataChange(it))
            },
            label = stringResource(id = R.string.email)
        )
        Spacer(modifier = Modifier.height(16.dp))
        RegisterTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(id = R.string.password_hint),
            isError = password.isError,
            supportText = password.errorText,
            value = password.content,
            onValueChange = {
                onRegisterType(RegisterEvent.PasswordDataChange(it))
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = painterResource(id = iconPassword),
            onTrailingIconClick = {
                showPassword = !showPassword
            },
            label = stringResource(id = R.string.password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(modifier = Modifier.align(End),
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 17.sp
                    )
                ) {
                    append(stringResource(id = R.string.forget_password))
                }
            },
            onClick = {
                Log.d("Register Screen", "SignInSection: called click text")
            }
        )

        Spacer(modifier = Modifier.height(30.dp))
        RegisterButton(
            text = stringResource(id = R.string.sign_in),
            onClick = { onRegisterType(RegisterEvent.SignIn) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(text = stringResource(id = R.string.other_sign_in_options))
        Spacer(modifier = Modifier.height(18.dp))

        RegisterIconButton(icon = painterResource(id = R.drawable.google_icon)) {
            onRegisterType(RegisterEvent.GoogleSign)
        }


    }
}

@PreviewBothLightAndDark
@Composable
fun RegisterPreview() {
    SocialTheme {
        RegisterScreen(registerState = RegisterState()) {}
    }
}