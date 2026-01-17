package com.example.test_anik.presentation.screens

// LoginScreen1.kt

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
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

    // Animated background gradient
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFEF3C7),  // Light yellow
                        Color(0xFFFBCFE8),  // Light pink
                        Color(0xFFDDD6FE)   // Light purple
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        // Decorative circles
        LoginDecorativeElements()

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
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6B7280)
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
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                    tint = Color(0xFF9CA3AF)
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
                                    checkedColor = Color(0xFF6366F1),
                                    uncheckedColor = Color(0xFFD1D5DB)
                                )
                            )
                            Text(
                                text = "Remember me",
                                color = Color(0xFF374151),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        TextButton(onClick = onForgotPasswordClick) {
                            Text(
                                text = "Forgot Password?",
                                color = Color(0xFF6366F1),
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
                    color = Color(0xFF6B7280).copy(alpha = 0.3f)
                )
                Text(
                    text = "  OR CONTINUE WITH  ",
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF6B7280).copy(alpha = 0.3f)
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
                    color = Color(0xFF6B7280)
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Sign Up",
                        color = Color(0xFF6366F1),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BoxScope.LoginDecorativeElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "decorations")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    // Yellow circle
    Box(
        modifier = Modifier
            .size(150.dp)
            .offset(x = (-40).dp, y = 100.dp + offset1.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFBBF24).copy(alpha = 0.3f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopStart)
    )

    // Pink circle
    Box(
        modifier = Modifier
            .size(180.dp)
            .offset(x = 80.dp, y = (-60).dp + offset2.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEC4899).copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopEnd)
    )

    // Purple circle
    Box(
        modifier = Modifier
            .size(140.dp)
            .offset(x = (-20).dp, y = offset1.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF8B5CF6).copy(alpha = 0.25f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.BottomStart)
    )
}

@Composable
fun GradientLogo1() {
    val scale by rememberInfiniteTransition(label = "logo").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6366F1),
                        Color(0xFF8B5CF6),
                        Color(0xFFEC4899)
                    )
                )
            ),
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
    Surface(
        color = Color.White.copy(alpha = 0.8f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                            if (isSelected) Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF8B5CF6),
                                    Color(0xFFEC4899)
                                )
                            )
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
                        color = if (isSelected) Color.White else Color(0xFF6B7280),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
                tint = Color(0xFF9CA3AF)
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
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6366F1),
            unfocusedBorderColor = Color(0xFFD1D5DB),
            focusedLabelColor = Color(0xFF6366F1),
            unfocusedLabelColor = Color(0xFF9CA3AF),
            cursorColor = Color(0xFF6366F1),
            focusedTextColor = Color(0xFF1F2937),
            unfocusedTextColor = Color(0xFF1F2937),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedLeadingIconColor = Color(0xFF6366F1)
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
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color(0xFFE5E7EB)
        ),
        contentPadding = PaddingValues(),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled) 4.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6),
                            Color(0xFFEC4899)
                        )
                    )
                    else Brush.linearGradient(
                        listOf(
                            Color(0xFFE5E7EB),
                            Color(0xFFE5E7EB)
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
                    color = if (enabled) Color.White else Color(0xFF9CA3AF),
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
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialLoginButton1(
            icon = Icons.Default.Email,
            contentDescription = "Google",
            backgroundColor = Color(0xFFDB4437),
            onClick = { onSocialLoginClick("google") },
            modifier = Modifier.weight(1f)
        )

        SocialLoginButton1(
            icon = Icons.Default.Facebook,
            contentDescription = "Facebook",
            backgroundColor = Color(0xFF4267B2),
            onClick = { onSocialLoginClick("facebook") },
            modifier = Modifier.weight(1f)
        )

        SocialLoginButton1(
            icon = Icons.Default.Work,
            contentDescription = "LinkedIn",
            backgroundColor = Color(0xFF0A66C2),
            onClick = { onSocialLoginClick("linkedin") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SocialLoginButton1(
    icon: ImageVector,
    contentDescription: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
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

    // Animated background gradient
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFEF3C7),
                        Color(0xFFFBCFE8),
                        Color(0xFFDDD6FE)
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        // Decorative circles
        LoginDecorativeElements()

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
                color = Color(0xFF1F2937)
            )

            Text(
                text = "Fill in your details to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Registration Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                                    tint = Color(0xFF9CA3AF)
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
                                    tint = Color(0xFF9CA3AF)
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
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Passwords do not match",
                                color = Color(0xFFEF4444),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Terms & Conditions
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { acceptTerms = !acceptTerms },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF3F4F6)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = acceptTerms,
                                onCheckedChange = { acceptTerms = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF6366F1),
                                    uncheckedColor = Color(0xFFD1D5DB)
                                )
                            )
                            Column {
                                Row {
                                    Text(
                                        text = "I agree to the ",
                                        color = Color(0xFF6B7280),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "Terms & Conditions",
                                        color = Color(0xFF6366F1),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
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
                    color = Color(0xFF6B7280).copy(alpha = 0.3f)
                )
                Text(
                    text = "  OR SIGN UP WITH  ",
                    color = Color(0xFF6B7280),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF6B7280).copy(alpha = 0.3f)
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
                    color = Color(0xFF6B7280)
                )
                TextButton(onClick = onLoginClick) {
                    Text(
                        text = "Sign In",
                        color = Color(0xFF6366F1),
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
        strength < 0.33f -> Color(0xFFEF4444) to "Weak"
        strength < 0.66f -> Color(0xFFF59E0B) to "Medium"
        else -> Color(0xFF10B981) to "Strong"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Password Strength",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFE5E7EB))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(strength)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(color)
                )
            }
        }
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