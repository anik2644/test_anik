package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

// Data Models
data class ProfileStats(
    val posts: Int,
    val followers: Int,
    val following: Int,
    val rating: Float = 4.8f
)

data class Achievement(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val color: Color
)

data class MenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val subtitle: String? = null,
    val showBadge: Boolean = false,
    val badgeCount: Int = 0,
    val gradient: List<Color>
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
                        Color(0xFFFEF3C7),  // Light yellow
                        Color(0xFFFBCFE8),  // Light pink
                        Color(0xFFDDD6FE)   // Light purple
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        // Floating decorative elements
        ProfileDecorativeElements()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Enhanced Header with Profile
            item {
                EnhancedProfileHeader()
            }

            // Animated Stats Cards
            item {
                AnimatedStatsSection(
                    stats = ProfileStats(
                        posts = 248,
                        followers = 1205,
                        following = 567,
                        rating = 4.8f
                    )
                )
            }

            // Achievements Section
            item {
                AchievementsSection()
            }

            // Interactive Quick Actions
            item {
                InteractiveQuickActions()
            }

            // Colorful Menu Sections
            item {
                ColorfulMenuSection(
                    title = "🎨 Account",
                    items = getAccountMenuItems(),
                    onItemClick = { menuItem ->
                        if (menuItem.id == 13) {
                            showLogoutDialog = true
                        }
                    }
                )
            }

            item {
                ColorfulMenuSection(
                    title = "⚙️ Preferences",
                    items = getPreferencesMenuItems(),
                    onItemClick = {}
                )
            }

            item {
                ColorfulMenuSection(
                    title = "📱 More",
                    items = getMoreMenuItems(),
                    onItemClick = {}
                )
            }

            // Vibrant Footer
            item {
                VibrantFooter()
            }
        }
    }

    // Enhanced Logout Dialog
    if (showLogoutDialog) {
        EnhancedLogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            }
        )
    }
}

@Composable
fun BoxScope.ProfileDecorativeElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "decorations")

    // Rotating gradient circles
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Top-left colorful blob
    Box(
        modifier = Modifier
            .size(200.dp)
            .offset(x = (-50).dp, y = 50.dp)
            .rotate(rotation)
            .blur(50.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF6B9D).copy(alpha = 0.3f),
                        Color(0xFFFEA400).copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopStart)
    )

    // Top-right colorful blob
    Box(
        modifier = Modifier
            .size(180.dp)
            .offset(x = 50.dp, y = (-30).dp)
            .rotate(-rotation)
            .blur(40.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6366F1).copy(alpha = 0.3f),
                        Color(0xFF8B5CF6).copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopEnd)
    )
}

@Composable
fun EnhancedProfileHeader() {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // This ensures it starts below the status bar
                .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            // Profile Picture with Animation
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
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
                    modifier = Modifier.scale(scale)
                ) {
                    // Outer glow effect
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF6366F1).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Profile picture
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .align(Alignment.Center)
                    ) {
                        // White border with shadow
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(5.dp)
                        ) {
                            AsyncImage(
                                model = "https://imageio.forbes.com/specials-images/imageserve/663e595b4509f97fdafb95f5/0x0.jpg?format=jpg&crop=383,383,x1045,y23,safe&height=416&width=416&fit=bounds",
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Premium badge on profile
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFFBBF24),
                                            Color(0xFFF59E0B)
                                        )
                                    )
                                )
                                .border(3.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name Section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Abdullah Al Mahmud",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1F2937),
                            textAlign = TextAlign.Center
                        )

                        // Role/Title
                        Text(
                            text = "Premium Member",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6366F1),
                            textAlign = TextAlign.Center
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    // Contact Information
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Email
                        ContactInfoRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = "anik@example.com",
                            iconColor = Color(0xFFEF4444)
                        )

                        // Phone (optional - you can add this)
                        ContactInfoRow(
                            icon = Icons.Default.Phone,
                            label = "Phone",
                            value = "+880 1712-345678",
                            iconColor = Color(0xFF10B981)
                        )

                        // Location (optional)
                        ContactInfoRow(
                            icon = Icons.Default.LocationOn,
                            label = "Location",
                            value = "Dhaka, Bangladesh",
                            iconColor = Color(0xFF3B82F6)
                        )
                    }

                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    // Status Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusBadge(
                            icon = Icons.Default.CheckCircle,
                            text = "Verified",
                            gradient = listOf(Color(0xFF10B981), Color(0xFF059669)),
                            modifier = Modifier.weight(1f)
                        )
                        StatusBadge(
                            icon = Icons.Default.Star,
                            text = "Premium",
                            gradient = listOf(Color(0xFFFBBF24), Color(0xFFF59E0B)),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9FAFB))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9CA3AF)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )
        }

        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            tint = Color(0xFF9CA3AF),
            modifier = Modifier
                .size(18.dp)
                .clickable { /* Copy to clipboard */ }
        )
    }
}

@Composable
fun StatusBadge(
    icon: ImageVector,
    text: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(gradient)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun AnimatedStatsSection(stats: ProfileStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedStatCard(
            value = stats.posts.toString(),
            label = "Posts",
            icon = Icons.Default.Article,
            gradient = listOf(Color(0xFFFF6B9D), Color(0xFFFF8E8E)),
            modifier = Modifier.weight(1f)
        )
        AnimatedStatCard(
            value = formatNumber(stats.followers),
            label = "Followers",
            icon = Icons.Default.People,
            gradient = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
            modifier = Modifier.weight(1f)
        )
        AnimatedStatCard(
            value = formatNumber(stats.following),
            label = "Following",
            icon = Icons.Default.PersonAdd,
            gradient = listOf(Color(0xFFFBBF24), Color(0xFFF59E0B)),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AnimatedStatCard(
    value: String,
    label: String,
    icon: ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) +
                slideInVertically(initialOffsetY = { it / 2 }),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(gradient))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(28.dp)
                    )

                    Column {
                        Text(
                            text = value,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🏆 Achievements",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = "View All",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6366F1),
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getAchievements()) { achievement ->
                AchievementBadge(achievement)
            }
        }
    }
}

@Composable
fun AchievementBadge(achievement: Achievement) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = achievement.color.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = achievement.icon,
                contentDescription = achievement.title,
                tint = achievement.color,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun InteractiveQuickActions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "⚡ Quick Actions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InteractiveActionCard(
                icon = Icons.Default.Edit,
                title = "Edit Profile",
                gradient = listOf(Color(0xFF10B981), Color(0xFF059669)),
                modifier = Modifier.weight(1f)
            )
            InteractiveActionCard(
                icon = Icons.Default.Settings,
                title = "Settings",
                gradient = listOf(Color(0xFF3B82F6), Color(0xFF2563EB)),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InteractiveActionCard(
                icon = Icons.Default.Share,
                title = "Share",
                gradient = listOf(Color(0xFFEC4899), Color(0xFFDB2777)),
                modifier = Modifier.weight(1f)
            )
            InteractiveActionCard(
                icon = Icons.Default.QrCode,
                title = "QR Code",
                gradient = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun InteractiveActionCard(
    icon: ImageVector,
    title: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Card(
        modifier = modifier
            .height(90.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = !isPressed
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ColorfulMenuSection(
    title: String,
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(12.dp))

        items.forEach { menuItem ->
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(menuItem.id * 30L)
                isVisible = true
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(400)) +
                        slideInHorizontally(initialOffsetX = { it / 3 })
            ) {
                ColorfulMenuItem(
                    menuItem = menuItem,
                    onClick = { onItemClick(menuItem) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun ColorfulMenuItem(
    menuItem: MenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Gradient icon background
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(menuItem.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = menuItem.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = menuItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
                menuItem.subtitle?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF),
                        maxLines = 1
                    )
                }
            }

            // Badge or arrow
            if (menuItem.showBadge && menuItem.badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEF4444),
                                    Color(0xFFDC2626)
                                )
                            )
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = menuItem.badgeCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun VibrantFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 32.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Social media icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SocialMediaIcon(
                icon = Icons.Default.Facebook,
                color = Color(0xFF4267B2)
            )
            SocialMediaIcon(
                icon = Icons.Default.Share,
                color = Color(0xFF1DA1F2)
            )
            SocialMediaIcon(
                icon = Icons.Default.Email,
                color = Color(0xFFEA4335)
            )
            SocialMediaIcon(
                icon = Icons.Default.Link,
                color = Color(0xFF0A66C2)
            )
        }

        Text(
            text = "Version 1.0.0",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF),
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Made with ❤️ in Bangladesh",
            fontSize = 13.sp,
            color = Color(0xFF6B7280),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SocialMediaIcon(icon: ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun EnhancedLogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        icon = {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEF4444),
                                Color(0xFFDC2626)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        title = {
            Text(
                text = "Logout?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Are you sure you want to logout? You'll need to login again to access your account.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFEF4444),
                                    Color(0xFFDC2626)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Yes, Logout",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE5E7EB))
            ) {
                Text(
                    "Cancel",
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
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

fun getAchievements(): List<Achievement> {
    return listOf(
        Achievement(1, "Early Bird", Icons.Default.WbSunny, Color(0xFFFBBF24)),
        Achievement(2, "Top Seller", Icons.Default.TrendingUp, Color(0xFF10B981)),
        Achievement(3, "5 Star", Icons.Default.Star, Color(0xFFEF4444)),
        Achievement(4, "Popular", Icons.Default.Favorite, Color(0xFFEC4899)),
        Achievement(5, "Verified", Icons.Default.CheckCircle, Color(0xFF6366F1))
    )
}

// Menu Items Data
fun getAccountMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = 1,
            title = "Edit Profile",
            subtitle = "Update your information",
            icon = Icons.Default.Person,
            gradient = listOf(Color(0xFF10B981), Color(0xFF059669))
        ),
        MenuItem(
            id = 2,
            title = "My Listings",
            subtitle = "Manage your properties",
            icon = Icons.Default.Home,
            showBadge = true,
            badgeCount = 5,
            gradient = listOf(Color(0xFFEF4444), Color(0xFFDC2626))
        ),
        MenuItem(
            id = 3,
            title = "Saved Items",
            subtitle = "Your favorites",
            icon = Icons.Default.Favorite,
            gradient = listOf(Color(0xFFEC4899), Color(0xFFDB2777))
        ),
        MenuItem(
            id = 4,
            title = "Payment Methods",
            subtitle = "Manage payments",
            icon = Icons.Default.CreditCard,
            gradient = listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED))
        )
    )
}

fun getPreferencesMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = 5,
            title = "Notifications",
            subtitle = "Manage alerts",
            icon = Icons.Default.Notifications,
            gradient = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
        ),
        MenuItem(
            id = 6,
            title = "Privacy & Security",
            subtitle = "Control privacy",
            icon = Icons.Default.Lock,
            gradient = listOf(Color(0xFF3B82F6), Color(0xFF2563EB))
        ),
        MenuItem(
            id = 7,
            title = "Language",
            subtitle = "English (US)",
            icon = Icons.Default.Language,
            gradient = listOf(Color(0xFF06B6D4), Color(0xFF0891B2))
        ),
        MenuItem(
            id = 8,
            title = "Theme",
            subtitle = "Customize appearance",
            icon = Icons.Default.Palette,
            gradient = listOf(Color(0xFFA855F7), Color(0xFF9333EA))
        )
    )
}

fun getMoreMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = 9,
            title = "Help & Support",
            subtitle = "Get assistance",
            icon = Icons.Default.Help,
            gradient = listOf(Color(0xFF14B8A6), Color(0xFF0D9488))
        ),
        MenuItem(
            id = 10,
            title = "Terms & Conditions",
            subtitle = "Read our terms",
            icon = Icons.Default.Description,
            gradient = listOf(Color(0xFF84CC16), Color(0xFF65A30D))
        ),
        MenuItem(
            id = 11,
            title = "Rate Us",
            subtitle = "Share feedback",
            icon = Icons.Default.Star,
            gradient = listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))
        ),
        MenuItem(
            id = 12,
            title = "About",
            subtitle = "Learn more",
            icon = Icons.Default.Info,
            gradient = listOf(Color(0xFF64748B), Color(0xFF475569))
        ),
        MenuItem(
            id = 13,
            title = "Logout",
            subtitle = "Sign out",
            icon = Icons.Default.Logout,
            gradient = listOf(Color(0xFFEF4444), Color(0xFFDC2626))
        )
    )
}