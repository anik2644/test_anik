package com.example.test_anik.presentation.screens

// LoginScreen1.kt

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette for Design 1
object AuthColors1 {
    val PrimaryBlue = Color(0xFF6366F1)
    val SecondaryPurple = Color(0xFF8B5CF6)
    val AccentPink = Color(0xFFEC4899)
    val DarkBackground = Color(0xFF0F172A)
    val CardBackground = Color(0xFF1E293B)
    val SurfaceLight = Color(0xFF334155)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF94A3B8)
    val ErrorRed = Color(0xFFEF4444)
    val SuccessGreen = Color(0xFF22C55E)

    val GradientPrimary = Brush.linearGradient(
        colors = listOf(PrimaryBlue, SecondaryPurple, AccentPink)
    )

    val GradientBackground = Brush.verticalGradient(
        colors = listOf(
            DarkBackground,
            Color(0xFF1A1A2E),
            DarkBackground
        )
    )
}

// Login Type Enum
enum class LoginType {
    EMAIL, PHONE, USERNAME
}

// Mock Data
object MockAuthData {
    const val MOCK_EMAIL = "john.doe@example.com"
    const val MOCK_PHONE = "+1 234 567 8900"
    const val MOCK_USERNAME = "johndoe123"
    const val MOCK_PASSWORD = "password123"
    const val MOCK_NAME = "John Doe"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernLoginScreen1(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSocialLoginClick: (String) -> Unit = {}
) {
    var loginIdentifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedLoginType by remember { mutableStateOf(LoginType.EMAIL) }
    var isLoading by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthColors1.GradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo/Brand Section
            GradientLogo1()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = AuthColors1.TextPrimary
            )

            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = AuthColors1.TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Type Selector
            LoginTypeSelector1(
                selectedType = selectedLoginType,
                onTypeSelected = {
                    selectedLoginType = it
                    loginIdentifier = ""
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AuthColors1.CardBackground.copy(alpha = 0.8f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Identifier Field
                    GradientOutlinedTextField1(
                        value = loginIdentifier,
                        onValueChange = { loginIdentifier = it },
                        label = when (selectedLoginType) {
                            LoginType.EMAIL -> "Email Address"
                            LoginType.PHONE -> "Phone Number"
                            LoginType.USERNAME -> "Username"
                        },
                        leadingIcon = when (selectedLoginType) {
                            LoginType.EMAIL -> Icons.Outlined.Email
                            LoginType.PHONE -> Icons.Outlined.Phone
                            LoginType.USERNAME -> Icons.Outlined.Person
                        },
                        keyboardType = when (selectedLoginType) {
                            LoginType.EMAIL -> KeyboardType.Email
                            LoginType.PHONE -> KeyboardType.Phone
                            LoginType.USERNAME -> KeyboardType.Text
                        },
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        imeAction = ImeAction.Next
                    )

                    // Password Field
                    GradientOutlinedTextField1(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Outlined.VisibilityOff
                                    else
                                        Icons.Outlined.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = AuthColors1.TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                onLoginClick(loginIdentifier, password)
                            }
                        ),
                        imeAction = ImeAction.Done
                    )

                    // Remember Me & Forgot Password Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { rememberMe = !rememberMe }
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AuthColors1.PrimaryBlue,
                                    uncheckedColor = AuthColors1.TextSecondary
                                )
                            )
                            Text(
                                text = "Remember me",
                                color = AuthColors1.TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        TextButton(onClick = onForgotPasswordClick) {
                            Text(
                                text = "Forgot Password?",
                                color = AuthColors1.PrimaryBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            GradientButton1(
                text = if (isLoading) "Signing in..." else "Sign In",
                onClick = {
                    isLoading = true
                    onLoginClick(loginIdentifier, password)
                },
                enabled = loginIdentifier.isNotBlank() && password.isNotBlank() && !isLoading,
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Divider with "OR"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = AuthColors1.SurfaceLight
                )
                Text(
                    text = "  OR CONTINUE WITH  ",
                    color = AuthColors1.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = AuthColors1.SurfaceLight
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Login Buttons
            SocialLoginRow1(onSocialLoginClick = onSocialLoginClick)

            Spacer(modifier = Modifier.height(32.dp))

            // Register Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = AuthColors1.TextSecondary
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Sign Up",
                        color = AuthColors1.PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun GradientLogo1() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(AuthColors1.GradientPrimary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = "Logo",
            modifier = Modifier.size(56.dp),
            tint = Color.White
        )
    }
}

@Composable
fun LoginTypeSelector1(
    selectedType: LoginType,
    onTypeSelected: (LoginType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AuthColors1.CardBackground.copy(alpha = 0.6f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LoginType.entries.forEach { type ->
            val isSelected = type == selectedType
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) AuthColors1.GradientPrimary
                        else Brush.linearGradient(
                            listOf(Color.Transparent, Color.Transparent)
                        )
                    )
                    .clickable { onTypeSelected(type) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (type) {
                        LoginType.EMAIL -> "Email"
                        LoginType.PHONE -> "Phone"
                        LoginType.USERNAME -> "Username"
                    },
                    color = if (isSelected) Color.White else AuthColors1.TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun GradientOutlinedTextField1(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = AuthColors1.TextSecondary
            )
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AuthColors1.PrimaryBlue,
            unfocusedBorderColor = AuthColors1.SurfaceLight,
            focusedLabelColor = AuthColors1.PrimaryBlue,
            unfocusedLabelColor = AuthColors1.TextSecondary,
            cursorColor = AuthColors1.PrimaryBlue,
            focusedTextColor = AuthColors1.TextPrimary,
            unfocusedTextColor = AuthColors1.TextPrimary
        )
    )
}

@Composable
fun GradientButton1(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) AuthColors1.GradientPrimary
                    else Brush.linearGradient(
                        listOf(
                            AuthColors1.SurfaceLight,
                            AuthColors1.SurfaceLight
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun SocialLoginRow1(
    onSocialLoginClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SocialLoginButton1(
            icon = Icons.Default.Email, // Replace with Google icon
            contentDescription = "Google",
            backgroundColor = Color(0xFFDB4437),
            onClick = { onSocialLoginClick("google") }
        )

        SocialLoginButton1(
            icon = Icons.Default.Facebook, // Replace with Facebook icon
            contentDescription = "Facebook",
            backgroundColor = Color(0xFF4267B2),
            onClick = { onSocialLoginClick("facebook") }
        )

        SocialLoginButton1(
            icon = Icons.Default.Work, // Replace with LinkedIn icon
            contentDescription = "LinkedIn",
            backgroundColor = Color(0xFF0A66C2),
            onClick = { onSocialLoginClick("linkedin") }
        )
    }
}

@Composable
fun SocialLoginButton1(
    icon: ImageVector,
    contentDescription: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(backgroundColor.copy(alpha = 0.1f))
            .border(1.dp, backgroundColor.copy(alpha = 0.3f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = backgroundColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

// ==================== REGISTRATION SCREEN 1 ====================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernRegistrationScreen1(
    onRegisterClick: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onLoginClick: () -> Unit = {},
    onSocialLoginClick: (String) -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val passwordsMatch = password == confirmPassword || confirmPassword.isEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthColors1.GradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header
            GradientLogo1()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = AuthColors1.TextPrimary
            )

            Text(
                text = "Fill in your details to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = AuthColors1.TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Registration Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AuthColors1.CardBackground.copy(alpha = 0.8f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Full Name
                    GradientOutlinedTextField1(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        leadingIcon = Icons.Outlined.Person,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Email
                    GradientOutlinedTextField1(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        leadingIcon = Icons.Outlined.Email,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Phone
                    GradientOutlinedTextField1(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        leadingIcon = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Password
                    GradientOutlinedTextField1(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Outlined.VisibilityOff
                                    else
                                        Icons.Outlined.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = AuthColors1.TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    // Password Strength Indicator
                    if (password.isNotEmpty()) {
                        PasswordStrengthIndicator1(password = password)
                    }

                    // Confirm Password
                    GradientOutlinedTextField1(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm Password",
                        leadingIcon = Icons.Outlined.Lock,
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible)
                                        Icons.Outlined.VisibilityOff
                                    else
                                        Icons.Outlined.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = AuthColors1.TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    // Password Match Error
                    if (!passwordsMatch) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = AuthColors1.ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Passwords do not match",
                                color = AuthColors1.ErrorRed,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Terms & Conditions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { acceptTerms = !acceptTerms }
                    ) {
                        Checkbox(
                            checked = acceptTerms,
                            onCheckedChange = { acceptTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AuthColors1.PrimaryBlue,
                                uncheckedColor = AuthColors1.TextSecondary
                            )
                        )
                        Text(
                            text = "I agree to the ",
                            color = AuthColors1.TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Terms & Conditions",
                            color = AuthColors1.PrimaryBlue,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            GradientButton1(
                text = if (isLoading) "Creating Account..." else "Create Account",
                onClick = {
                    isLoading = true
                    onRegisterClick(fullName, email, phone, password)
                },
                enabled = fullName.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        passwordsMatch &&
                        acceptTerms &&
                        !isLoading,
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = AuthColors1.SurfaceLight
                )
                Text(
                    text = "  OR SIGN UP WITH  ",
                    color = AuthColors1.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = AuthColors1.SurfaceLight
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Login
            SocialLoginRow1(onSocialLoginClick = onSocialLoginClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Login Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = AuthColors1.TextSecondary
                )
                TextButton(onClick = onLoginClick) {
                    Text(
                        text = "Sign In",
                        color = AuthColors1.PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PasswordStrengthIndicator1(password: String) {
    val strength = calculatePasswordStrength(password)
    val (color, label) = when {
        strength < 0.33f -> AuthColors1.ErrorRed to "Weak"
        strength < 0.66f -> Color(0xFFFFA500) to "Medium"
        else -> AuthColors1.SuccessGreen to "Strong"
    }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Password Strength",
                style = MaterialTheme.typography.bodySmall,
                color = AuthColors1.TextSecondary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
        LinearProgressIndicator(
            progress = { strength },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = AuthColors1.SurfaceLight
        )
    }
}

fun calculatePasswordStrength(password: String): Float {
    var score = 0f
    if (password.length >= 8) score += 0.25f
    if (password.any { it.isUpperCase() }) score += 0.25f
    if (password.any { it.isDigit() }) score += 0.25f
    if (password.any { !it.isLetterOrDigit() }) score += 0.25f
    return score
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewModernLoginScreen1() {
    ModernLoginScreen1()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewModernRegistrationScreen1() {
    ModernRegistrationScreen1()
}