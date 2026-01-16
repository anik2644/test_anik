package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Data Models
data class ProfileStats(
    val posts: Int,
    val followers: Int,
    val following: Int
)

data class MenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val subtitle: String? = null,
    val showBadge: Boolean = false,
    val badgeCount: Int = 0
)

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Animated background
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
                        Color(0xFFDCFCE7),
                        Color(0xFFD1FAE5),
                        Color(0xFFBBF7D0)
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header with Profile
            item {
                ProfileHeader()
            }

            // Stats Section
            item {
                StatsSection(
                    stats = ProfileStats(
                        posts = 248,
                        followers = 1205,
                        following = 567
                    )
                )
            }

            // Quick Actions
            item {
                QuickActionsSection()
            }

            // Account Settings Section
            item {
                SectionHeader(title = "Account Settings")
            }

            items(getAccountMenuItems()) { menuItem ->
                var isVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(menuItem.id * 50L)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(400)) +
                            slideInHorizontally(initialOffsetX = { it / 2 })
                ) {
                    MenuItemCard(
                        menuItem = menuItem,
                        onClick = {
                            if (menuItem.id == 1) { // Edit Profile
                                // Navigate to edit profile
                            }
                        }
                    )
                }
            }

            // Preferences Section
            item {
                SectionHeader(title = "Preferences")
            }

            items(getPreferencesMenuItems()) { menuItem ->
                var isVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(menuItem.id * 50L)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(400)) +
                            slideInHorizontally(initialOffsetX = { it / 2 })
                ) {
                    MenuItemCard(menuItem = menuItem, onClick = {})
                }
            }

            // More Section
            item {
                SectionHeader(title = "More")
            }

            items(getMoreMenuItems()) { menuItem ->
                var isVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(menuItem.id * 50L)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(400)) +
                            slideInHorizontally(initialOffsetX = { it / 2 })
                ) {
                    MenuItemCard(
                        menuItem = menuItem,
                        onClick = {
                            if (menuItem.id == 13) { // Logout
                                showLogoutDialog = true
                            }
                        }
                    )
                }
            }

            // App Info
            item {
                AppInfoSection()
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            }
        )
    }
}

@Composable
fun ProfileHeader() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) + scaleIn(initialScale = 0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Profile Picture
            val scale by rememberInfiniteTransition(label = "profile").animateFloat(
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
                    .size(120.dp)
                    .scale(scale)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF10B981),
                                    Color(0xFF059669)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Online Status Indicator
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Abdullah Al Mahmud",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Text(
                text = "anik@example.com",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Verified Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF10B981).copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Verified Account",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF047857)
                )
            }
        }
    }
}

@Composable
fun StatsSection(stats: ProfileStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Posts", value = stats.posts.toString())
            VerticalDivider(height = 50.dp)
            StatItem(label = "Followers", value = formatNumber(stats.followers))
            VerticalDivider(height = 50.dp)
            StatItem(label = "Following", value = formatNumber(stats.following))
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
fun VerticalDivider(height: androidx.compose.ui.unit.Dp = 40.dp) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(height)
            .background(Color(0xFFE5E7EB))
    )
}

@Composable
fun QuickActionsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = Icons.Default.Edit,
            label = "Edit Profile",
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f),
            onClick = {}
        )
        QuickActionButton(
            icon = Icons.Default.Share,
            label = "Share Profile",
            color = Color(0xFF3B82F6),
            modifier = Modifier.weight(1f),
            onClick = {}
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            color,
                            color.copy(alpha = 0.8f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF374151),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getIconBackgroundColor(menuItem.id)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = menuItem.icon,
                    contentDescription = null,
                    tint = getIconColor(menuItem.id),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Title and Subtitle
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = menuItem.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
                menuItem.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            // Badge or Arrow
            if (menuItem.showBadge && menuItem.badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = menuItem.badgeCount.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AppInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "App Version 1.0.0",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF)
        )
        Text(
            text = "© 2026 Test Anik. All rights reserved.",
            fontSize = 11.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = "Logout",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        },
        text = {
            Text(
                text = "Are you sure you want to logout from your account?",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Yes, Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF6B7280))
            }
        }
    )
}

// Helper Functions
fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> String.format("%.1fM", number / 1000000.0)
        number >= 1000 -> String.format("%.1fK", number / 1000.0)
        else -> number.toString()
    }
}

fun getIconBackgroundColor(id: Int): Color {
    return when (id) {
        1 -> Color(0xFF10B981).copy(alpha = 0.15f)
        2 -> Color(0xFF3B82F6).copy(alpha = 0.15f)
        3 -> Color(0xFFF59E0B).copy(alpha = 0.15f)
        4 -> Color(0xFF8B5CF6).copy(alpha = 0.15f)
        5 -> Color(0xFF06B6D4).copy(alpha = 0.15f)
        6 -> Color(0xFFEC4899).copy(alpha = 0.15f)
        7 -> Color(0xFF6366F1).copy(alpha = 0.15f)
        8 -> Color(0xFFF97316).copy(alpha = 0.15f)
        9 -> Color(0xFF14B8A6).copy(alpha = 0.15f)
        10 -> Color(0xFFA855F7).copy(alpha = 0.15f)
        11 -> Color(0xFF84CC16).copy(alpha = 0.15f)
        12 -> Color(0xFF64748B).copy(alpha = 0.15f)
        13 -> Color(0xFFEF4444).copy(alpha = 0.15f)
        else -> Color(0xFF9CA3AF).copy(alpha = 0.15f)
    }
}

fun getIconColor(id: Int): Color {
    return when (id) {
        1 -> Color(0xFF10B981)
        2 -> Color(0xFF3B82F6)
        3 -> Color(0xFFF59E0B)
        4 -> Color(0xFF8B5CF6)
        5 -> Color(0xFF06B6D4)
        6 -> Color(0xFFEC4899)
        7 -> Color(0xFF6366F1)
        8 -> Color(0xFFF97316)
        9 -> Color(0xFF14B8A6)
        10 -> Color(0xFFA855F7)
        11 -> Color(0xFF84CC16)
        12 -> Color(0xFF64748B)
        13 -> Color(0xFFEF4444)
        else -> Color(0xFF9CA3AF)
    }
}

// Menu Items Data
fun getAccountMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = 1,
            title = "Edit Profile",
            subtitle = "Update your personal information",
            icon = Icons.Default.Person
        ),
        MenuItem(
            id = 2,
            title = "My Listings",
            subtitle = "View your posted properties",
            icon = Icons.Default.Home,
            showBadge = true,
            badgeCount = 5
        ),
        MenuItem(
            id = 3,
            title = "Saved Items",
            subtitle = "Your favorite properties",
            icon = Icons.Default.Favorite
        ),
        MenuItem(
            id = 4,
            title = "Payment Methods",
            subtitle = "Manage payment options",
            icon = Icons.Default.CreditCard
        )
    )
}

fun getPreferencesMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = 5,
            title = "Notifications",
            subtitle = "Manage notification settings",
            icon = Icons.Default.Notifications
        ),
        MenuItem(
            id = 6,
            title = "Privacy & Security",
            subtitle = "Control your privacy settings",
            icon = Icons.Default.Lock
        ),
        MenuItem(
            id = 7,
            title = "Language",
            subtitle = "English (US)",
            icon = Icons.Default.Language
        ),
        MenuItem(
            id = 8,
            title = "Theme",
            subtitle = "Light mode",
            icon = Icons.Default.Brightness6
        )
    )
}

fun getMoreMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = 9,
            title = "Help & Support",
            subtitle = "Get help with your account",
            icon = Icons.Default.Help
        ),
        MenuItem(
            id = 10,
            title = "Terms & Conditions",
            subtitle = "Read our terms",
            icon = Icons.Default.Description
        ),
        MenuItem(
            id = 11,
            title = "Rate Us",
            subtitle = "Share your feedback",
            icon = Icons.Default.Star
        ),
        MenuItem(
            id = 12,
            title = "About",
            subtitle = "Learn more about us",
            icon = Icons.Default.Info
        ),
        MenuItem(
            id = 13,
            title = "Logout",
            subtitle = "Sign out from your account",
            icon = Icons.Default.Logout
        )
    )
}