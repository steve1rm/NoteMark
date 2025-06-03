package me.androidbox.authentication.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.authentication.core.presentation.components.NoteMarkPasswordTextField
import me.androidbox.authentication.core.presentation.components.NoteMarkTextField
import me.androidbox.authentication.core.presentation.utils.isAtLeastMedium
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.buttons.OutlineButton
import me.androidbox.core.presentation.designsystem.buttons.SolidButton
import me.androidbox.core.presentation.designsystem.theming.bgGradient
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PortraitLoginScreen(
    modifier: Modifier = Modifier,
    onAction: (LoginActions) -> Unit,
    state: LoginUiState,
    isAtLeastMedium: Boolean = isAtLeastMedium()
) {
    NoteMarkLayout(
        modifier = modifier,
        toolBar = {},
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = MaterialTheme.colorScheme.bgGradient)
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(Color.White)
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                        .padding(
                            vertical = if (isAtLeastMedium) 100.dp else 0.dp,
                            horizontal = if (isAtLeastMedium) 120.dp else 0.dp
                        ),
                    horizontalAlignment = if (isAtLeastMedium) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Text(
                        text = "Log In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Color(0xff1B1B1C)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Capture your thoughts and ideas.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color(0xff535364),
                    )

                    Spacer(Modifier.height(40.dp))

                    NoteMarkTextField(
                        label = "Email",
                        hint = "john.doe@example.com",
                        value = state.email,
                        onValueChange = {
                            onAction(LoginActions.OnEmailChange(it))
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    NoteMarkPasswordTextField(
                        label = "Password",
                        hint = "Password",
                        value = state.password,
                        onValueChange = {
                            onAction(LoginActions.OnPasswordChange(it))
                        },
                        showPassword = state.showPassword,
                        onToggleShowPassword = {
                            onAction(LoginActions.OnToggleShowPassword)
                        }
                    )

                    Spacer(Modifier.height(24.dp))

                    SolidButton(
                        text = "Log in",
                        onClick = {

                        },
                        enabled = state.isLoginEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlineButton(
                        text = "Don’t have an account?",
                        onClick = {

                        },
                        enabled = state.isLoginEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun PortraitLoginScreenPreview() {
//    PortraitLoginScreen()
}