package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Form state
data class SignUpFormState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val agreeToTerms: Boolean = false,
    val selectedTab: SignUpTab = SignUpTab.EMAIL
)

enum class SignUpTab {
    EMAIL, PHONE, USERNAME
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var formState by remember { mutableStateOf(SignUpFormState()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Animated background - matching HomeScreen theme
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
        DecorativeBackgroundElements()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header with animated icon
            SignUpHeader()

            Spacer(modifier = Modifier.height(32.dp))

            // Tab Selector
            TabSelector(
                selectedTab = formState.selectedTab,
                onTabSelected = { formState = formState.copy(selectedTab = it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            SignUpFormFields(
                formState = formState,
                onFormStateChange = { formState = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChange = { confirmPasswordVisible = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Terms and Conditions
            TermsCheckbox(
                checked = formState.agreeToTerms,
                onCheckedChange = { formState = formState.copy(agreeToTerms = it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            SignUpButton(
                enabled = formState.agreeToTerms && formState.fullName.isNotBlank() &&
                        formState.password.isNotBlank() && formState.password == formState.confirmPassword,
                isLoading = isLoading,
                onClick = {
                    isLoading = true
                    // Simulate sign up
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with text
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF6B7280).copy(alpha = 0.3f)
                )
                Text(
                    text = "OR SIGN UP WITH",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Medium
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF6B7280).copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Sign Up Options
            SocialSignUpOptions()

            Spacer(modifier = Modifier.height(32.dp))

            // Already have account
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = "Sign In",
                    fontSize = 14.sp,
                    color = Color(0xFF6366F1),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SignUpHeader() {
    val scale by rememberInfiniteTransition(label = "icon").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
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
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
        }

        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            letterSpacing = (-0.5).sp
        )

        Text(
            text = "Sign up to get started",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TabSelector(
    selectedTab: SignUpTab,
    onTabSelected: (SignUpTab) -> Unit
) {
    Surface(
        color = Color.White.copy(alpha = 0.7f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SignUpTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF6366F1),
                                        Color(0xFF8B5CF6),
                                        Color(0xFFEC4899)
                                    )
                                )
                            } else {
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                )
                            }
                        )
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpFormFields(
    formState: SignUpFormState,
    onFormStateChange: (SignUpFormState) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChange: (Boolean) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) + slideInVertically()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Full Name
            CustomTextField(
                value = formState.fullName,
                onValueChange = { onFormStateChange(formState.copy(fullName = it)) },
                label = "Full Name",
                placeholder = "Enter your full name",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Text
            )

            // Based on selected tab
            when (formState.selectedTab) {
                SignUpTab.EMAIL -> {
                    CustomTextField(
                        value = formState.email,
                        onValueChange = { onFormStateChange(formState.copy(email = it)) },
                        label = "Email Address",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )
                }
                SignUpTab.PHONE -> {
                    CustomTextField(
                        value = formState.phone,
                        onValueChange = { onFormStateChange(formState.copy(phone = it)) },
                        label = "Phone Number",
                        placeholder = "Enter your phone number",
                        leadingIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )
                }
                SignUpTab.USERNAME -> {
                    CustomTextField(
                        value = formState.username,
                        onValueChange = { onFormStateChange(formState.copy(username = it)) },
                        label = "Username",
                        placeholder = "Choose a username",
                        leadingIcon = Icons.Default.AccountCircle,
                        keyboardType = KeyboardType.Text
                    )
                }
            }

            // Date of Birth
            CustomTextField(
                value = formState.dateOfBirth,
                onValueChange = { onFormStateChange(formState.copy(dateOfBirth = it)) },
                label = "Date of Birth",
                placeholder = "DD/MM/YYYY",
                leadingIcon = Icons.Default.CalendarToday,
                keyboardType = KeyboardType.Number
            )

            // Gender Selector
            GenderSelector(
                selectedGender = formState.gender,
                onGenderSelected = { onFormStateChange(formState.copy(gender = it)) }
            )

            // Password
            CustomTextField(
                value = formState.password,
                onValueChange = { onFormStateChange(formState.copy(password = it)) },
                label = "Password",
                placeholder = "Create a password",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF9CA3AF)
                        )
                    }
                }
            )

            // Confirm Password
            CustomTextField(
                value = formState.confirmPassword,
                onValueChange = { onFormStateChange(formState.copy(confirmPassword = it)) },
                label = "Confirm Password",
                placeholder = "Re-enter your password",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { onConfirmPasswordVisibilityChange(!confirmPasswordVisible) }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF9CA3AF)
                        )
                    }
                },
                isError = formState.confirmPassword.isNotBlank() && formState.password != formState.confirmPassword,
                errorMessage = if (formState.confirmPassword.isNotBlank() && formState.password != formState.confirmPassword)
                    "Passwords do not match" else null
            )

            // Password strength indicator
            if (formState.password.isNotBlank()) {
                PasswordStrengthIndicator(password = formState.password)
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF9CA3AF),
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1F2937),
                unfocusedTextColor = Color(0xFF1F2937),
                focusedBorderColor = if (isError) Color(0xFFEF4444) else Color(0xFF6366F1),
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedContainerColor = Color.White.copy(alpha = 0.9f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                cursorColor = Color(0xFF6366F1),
                errorBorderColor = Color(0xFFEF4444),
                errorContainerColor = Color.White.copy(alpha = 0.9f),
                focusedLeadingIconColor = Color(0xFF6366F1)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            isError = isError
        )

        if (isError && errorMessage != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
fun GenderSelector(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Gender",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Male", "Female", "Other").forEach { gender ->
                val isSelected = selectedGender == gender
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onGenderSelected(gender) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color.Transparent else Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 0.dp else 2.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF6366F1),
                                            Color(0xFF8B5CF6)
                                        )
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(Color.Transparent, Color.Transparent)
                                    )
                                }
                            )
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = gender,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = calculatePasswordStrength2(password)
    val strengthColor = when (strength) {
        in 0..30 -> Color(0xFFEF4444)
        in 31..60 -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }
    val strengthText = when (strength) {
        in 0..30 -> "Weak"
        in 31..60 -> "Medium"
        else -> "Strong"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = strengthColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password Strength",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = strengthText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = strengthColor
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
                        .fillMaxWidth(strength / 100f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(strengthColor)
                )
            }
        }
    }
}

@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF6366F1),
                    uncheckedColor = Color(0xFFD1D5DB),
                    checkmarkColor = Color.White
                )
            )

            Text(
                text = "I agree to the Terms & Conditions and Privacy Policy",
                fontSize = 13.sp,
                color = Color(0xFF374151),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SignUpButton(
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color(0xFFE5E7EB)
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled) 4.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6366F1),
                                Color(0xFF8B5CF6),
                                Color(0xFFEC4899)
                            )
                        )
                    } else {
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE5E7EB),
                                Color(0xFFE5E7EB)
                            )
                        )
                    }
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
                    text = "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Color.White else Color(0xFF9CA3AF)
                )
            }
        }
    }
}

@Composable
fun SocialSignUpOptions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialButton(
            icon = Icons.Default.Email,
            color = Color(0xFFDB4437),
            label = "Google",
            modifier = Modifier.weight(1f)
        )
        SocialButton(
            icon = Icons.Default.Facebook,
            color = Color(0xFF1877F2),
            label = "Facebook",
            modifier = Modifier.weight(1f)
        )
        SocialButton(
            icon = Icons.Default.Work,
            color = Color(0xFF0A66C2),
            label = "LinkedIn",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SocialButton(
    icon: ImageVector,
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(56.dp)
            .clickable { },
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
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun BoxScope.DecorativeBackgroundElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "decorations")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    // Yellow circle
    Box(
        modifier = Modifier
            .size(200.dp)
            .offset(x = (-50).dp, y = 80.dp + offset1.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFBBF24).copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopStart)
    )

    // Pink circle
    Box(
        modifier = Modifier
            .size(250.dp)
            .offset(x = 100.dp, y = (-80).dp + offset2.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEC4899).copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopEnd)
    )

    // Purple circle
    Box(
        modifier = Modifier
            .size(180.dp)
            .offset(x = 50.dp, y = offset1.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF8B5CF6).copy(alpha = 0.15f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.BottomEnd)
    )
}

// Helper function
fun calculatePasswordStrength2(password: String): Int {
    var strength = 0
    if (password.length >= 8) strength += 25
    if (password.any { it.isUpperCase() }) strength += 25
    if (password.any { it.isLowerCase() }) strength += 20
    if (password.any { it.isDigit() }) strength += 15
    if (password.any { !it.isLetterOrDigit() }) strength += 15
    return strength.coerceIn(0, 100)
}