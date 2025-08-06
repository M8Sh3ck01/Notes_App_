package com.champox.notes.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.champox.notes.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    fun validateEmail(): Boolean {
        return when {
            email.isBlank() -> {
                emailError = "Email is required"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailError = "Invalid email format"
                false
            }
            else -> {
                emailError = null
                true
            }
        }
    }

    fun validatePassword(): Boolean {
        return when {
            password.isBlank() -> {
                passwordError = "Password is required"
                false
            }
            password.length < 4 -> {
                passwordError = "Minimum 4 characters required"
                false
            }
            else -> {
                passwordError = null
                true
            }
        }
    }

    fun validateConfirmPassword(): Boolean {
        return when {
            confirmPassword.isBlank() -> {
                confirmPasswordError = "Please confirm your password"
                false
            }
            password != confirmPassword -> {
                confirmPasswordError = "Passwords don't match"
                false
            }
            else -> {
                confirmPasswordError = null
                true
            }
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Success) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)  // <-- add this
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                validateEmail()
            },
            label = { Text("Email", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                validatePassword()
                if (confirmPassword.isNotBlank()) validateConfirmPassword()
            },
            label = { Text("Password (min 4 chars)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                validateConfirmPassword()
            },
            label = { Text("Confirm Password", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (confirmPasswordError != null) {
            Text(
                text = confirmPasswordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()

                val isEmailValid = validateEmail()
                val isPasswordValid = validatePassword()
                val isConfirmValid = validateConfirmPassword()

                if (isEmailValid && isPasswordValid && isConfirmValid) {
                    viewModel.register(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthViewModel.AuthState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            if (authState is AuthViewModel.AuthState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .height(24.dp)
                        .padding(4.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Register")
            }
        }

        TextButton(onClick = onNavigateToLogin) {
            Text(
                "Already have an account? Login",
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (authState is AuthViewModel.AuthState.Error) {
            Text(
                text = (authState as AuthViewModel.AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
