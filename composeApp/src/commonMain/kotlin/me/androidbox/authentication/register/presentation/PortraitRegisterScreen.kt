package me.androidbox.authentication.register.presentation

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
import me.androidbox.core.presentation.designsystem.textfields.NoteMarkPasswordTextField
import me.androidbox.core.presentation.designsystem.textfields.NoteMarkTextField
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.buttons.OutlineButton
import me.androidbox.core.presentation.designsystem.buttons.SolidButton
import me.androidbox.core.presentation.designsystem.theming.NoteMarkTheme
import me.androidbox.core.presentation.designsystem.theming.bgGradient
import me.androidbox.isAtLeastMedium
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PortraitRegisterScreen(
    modifier: Modifier = Modifier,
    onAction: (RegisterActions) -> Unit,
    state: RegisterUiState,
    onNavigateToLogin: () -> Unit,
    isAtLeastMedium: Boolean = isAtLeastMedium(),
) {
    // TODO do not pass in the viewmodel into the screen
    // Only state should be passed in, and lambda actions
    // val state by viewModel.state.collectAsState()

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
                        text = "Create account",
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
                        label = "Username",
                        hint = "John.doe",
                        value = state.username,
                        onValueChange = {
                            onAction(RegisterActions.OnUsernameChange(it))
                        },
                        supportText = "Use between 3 and 20 characters for your username.",
                        errorText = state.usernameError
                    )

                    Spacer(Modifier.height(16.dp))

                    NoteMarkTextField(
                        label = "Email",
                        hint = "john.doe@example.com",
                        value = state.email,
                        onValueChange = {
                            onAction(RegisterActions.OnEmailChange(it))
                        },
                        errorText = state.emailError
                    )

                    Spacer(Modifier.height(16.dp))

                    NoteMarkPasswordTextField(
                        label = "Password",
                        hint = "Password",
                        value = state.password,
                        onValueChange = {
                            onAction(RegisterActions.OnPasswordChange(it))
                        },
                        showPassword = state.showPassword,
                        onToggleShowPassword = {
                            onAction(RegisterActions.OnToggleShowPassword)
                        },
                        supportText = "Use 8+ characters with a number or symbol for better security.",
                        errorText = state.passwordError
                    )

                    Spacer(Modifier.height(16.dp))

                    NoteMarkPasswordTextField(
                        label = "Repeat Password",
                        hint = "Password",
                        value = state.repeatPassword,
                        onValueChange = {
                            onAction(RegisterActions.OnRepeatPasswordChange(it))
                        },
                        showPassword = state.showConfirmPassword,
                        onToggleShowPassword = {
                            onAction(RegisterActions.OnToggleShowConfirmPassword)
                        },
                        errorText = state.repeatPasswordError
                    )

                    Spacer(Modifier.height(24.dp))

                    SolidButton(
                        text = "Create account",
                        onClick = {

                        },
                        enabled = state.isRegisterEnabled,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlineButton(
                        text = "Already have an account?",
                        onClick = onNavigateToLogin,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PortraitRegisterScreenPreview() {
    NoteMarkTheme {
        PortraitRegisterScreen(
            onAction = {},
            state = RegisterUiState(),
            onNavigateToLogin = {}
        )
    }
}