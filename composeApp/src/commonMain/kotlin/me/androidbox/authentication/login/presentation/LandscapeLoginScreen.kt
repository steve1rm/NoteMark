package me.androidbox.authentication.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.core.presentation.designsystem.textfields.NoteMarkPasswordTextField
import me.androidbox.core.presentation.designsystem.textfields.NoteMarkTextField
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.buttons.OutlineButton
import me.androidbox.core.presentation.designsystem.buttons.SolidButton
import me.androidbox.core.presentation.designsystem.buttons.TextButton
import me.androidbox.core.presentation.designsystem.theming.bgGradient
import me.androidbox.isAtLeastMedium
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun LandscapeLoginScreen(
    modifier: Modifier = Modifier,
    onAction: (LoginActions) -> Unit,
    state: LoginUiState,
    onNavigateToRegister: () -> Unit,
) {
    val snackState = remember { SnackbarHostState() }
    NoteMarkLayout(
        snackState = snackState,
        modifier = modifier,
        toolBar = {},
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = MaterialTheme.colorScheme.bgGradient)
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        end = innerPadding.calculateEndPadding(
                            LayoutDirection.Ltr
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp))
                        .background(Color.White)
                        .padding(
                            innerPadding.calculateTopPadding()
                        ),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "Log In",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Capture your thoughts and ideas.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NoteMarkTextField(
                            "Email",
                            "john.doe@example.com",
                            state.email,
                            onValueChange = { onAction(LoginActions.OnEmailChange(it)) }
                        )
                        Spacer(Modifier.height(16.dp))
                        NoteMarkPasswordTextField(
                            "Password",
                            "Password",
                            state.password,
                            onValueChange = { onAction(LoginActions.OnPasswordChange(it)) },
                            showPassword = state.showPassword,
                            onToggleShowPassword = { onAction(LoginActions.OnToggleShowPassword) }
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

                        TextButton(
                            text = "Don’t have an account?",
                            onClick = onNavigateToRegister,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun LandscapeLoginScreenPreview() {
    LandscapeLoginScreen(
        onAction = {},
        state = LoginUiState(),
        onNavigateToRegister = {}
    )
}