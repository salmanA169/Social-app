package com.example.social.sa.screens.register.info_register

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.RegisterButton
import com.example.social.sa.component.RegisterTextField
import com.example.social.sa.ui.theme.SocialTheme

fun NavGraphBuilder.infoRegisterDest(navController: NavController) {
    composable<Screens.InfoRegisterRoute> {
        val infoViewModel = hiltViewModel<InfoRegisterViewModel>()
        val getArg = it.toRoute<Screens.InfoRegisterRoute>()
        val state by infoViewModel.state.collectAsStateWithLifecycle()
        val effect by infoViewModel.effect.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = effect) {
            when (effect) {
                InfoRegisterEffect.NavigateHome -> {
                    navController.navigate(Screens.HomeScreen.route) {
                        popUpTo<Screens.InfoRegisterRoute>() {
                            inclusive = true
                        }
                    }
                }

                null -> {

                }
            }
            infoViewModel.resetEffect()
        }
        LaunchedEffect(key1 = getArg) {
            infoViewModel.onEvent(
                InfoRegisterEvent.ArgChanges(
                    getArg.email,
                    getArg.userName,
                    getArg.imageUrl
                )
            )
        }
        InfoRegisterScreen(infoRegisterState = state, onInfoRegisterEvent = infoViewModel::onEvent)
    }
}

@Composable
fun InfoRegisterScreen(
    modifier: Modifier = Modifier,
    infoRegisterState: InfoRegisterState,
    onInfoRegisterEvent: (InfoRegisterEvent) -> Unit = {}
) {

    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                onInfoRegisterEvent(InfoRegisterEvent.ImageChanged(uri.toString()))
            }
        }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        if (infoRegisterState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        AsyncImage(
            model = infoRegisterState.imageUri,
            contentDescription = "",
            modifier = Modifier
                .align(CenterHorizontally)
                .size(90.dp)
                .clip(
                    CircleShape
                )
                .clickable { pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.preview_image_icon),
        )

        Spacer(modifier = Modifier.height(16.dp))
        RegisterTextField(
            hint = stringResource(id = R.string.displayNameHint),
            isError = infoRegisterState.displayName.isError,
            supportText = infoRegisterState.displayName.errorText,
            value = infoRegisterState.displayName.content,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.displayName)
        ) {
            onInfoRegisterEvent(InfoRegisterEvent.NameChanged(it))
        }
        Spacer(modifier = Modifier.height(12.dp))
        RegisterTextField(
            hint = stringResource(id = R.string.userNameHint),
            isError = infoRegisterState.userName.isError,
            supportText = infoRegisterState.userName.errorText,
            value = infoRegisterState.userName.content,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.userName)
        ) {
            onInfoRegisterEvent(InfoRegisterEvent.UserNameChanged(it))
        }
        Spacer(modifier = Modifier.height(12.dp))
        RegisterTextField(
            hint = stringResource(id = R.string.email_hint),
            isError = infoRegisterState.email.isError,
            supportText = infoRegisterState.email.errorText,
            value = infoRegisterState.email.content,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.email)
        ) {
            onInfoRegisterEvent(InfoRegisterEvent.EmailChanged(it))
        }
        Spacer(modifier = Modifier.height(12.dp))
        RegisterButton(text = "Submit", onClick = {
            onInfoRegisterEvent(InfoRegisterEvent.Submit)
        }, modifier = Modifier.fillMaxWidth())
    }
}

@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun InfoScreenPreview() {
    SocialTheme {
        InfoRegisterScreen(infoRegisterState = InfoRegisterState())
    }

}