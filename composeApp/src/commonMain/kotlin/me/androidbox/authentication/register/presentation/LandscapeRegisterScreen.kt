package me.androidbox.authentication.register.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.androidbox.authentication.core.presentation.components.NoteMarkPasswordTextField
import me.androidbox.authentication.core.presentation.components.NoteMarkTextField
import me.androidbox.core.presentation.designsystem.NoteMarkLayout
import me.androidbox.core.presentation.designsystem.buttons.OutlineButton
import me.androidbox.core.presentation.designsystem.buttons.SolidButton
import me.androidbox.core.presentation.designsystem.theming.bgGradient

@Composable
fun LandscapeRegisterScreen(
    modifier: Modifier = Modifier,
    onAction: (RegisterActions) -> Unit,
    state: RegisterUiState,
    onNavigateToLogin: () -> Unit,
) {
    NoteMarkLayout(
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
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NoteMarkTextField(
                            label = "Username",
                            hint = "John.doe",
                            value = state.username,
                            onValueChange = {
                                onAction(RegisterActions.OnUsernameChange(it))
                            },
                            supportText = "Use between 3 and 20 characters for your username."
                        )
                        Spacer(Modifier.height(16.dp))
                        NoteMarkTextField(
                            label = "Email",
                            hint = "john.doe@example.com",
                            value = state.email,
                            onValueChange = {
                                onAction(RegisterActions.OnEmailChange(it))
                            }
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
                            supportText = "Use 8+ characters with a number or symbol for better security."
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
                            }
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
        }
    )
}