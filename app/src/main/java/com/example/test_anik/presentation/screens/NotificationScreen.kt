package com.example.test_anik.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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

// Notification data model
data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector,
    val accentColor: Color,
    val isRead: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen() {
    var notifications by remember {
        mutableStateOf(getDummyNotifications())
    }

    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
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
                        Color(0xFF0A0E27),
                        Color(0xFF1A1B3E),
                        Color(0xFF2A1B3D)
                    ),
                    startY = 0f,
                    endY = gradientOffset * 2000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            NotificationHeader()

            // Filter Tabs
            FilterTabs(
                onFilterChange = { filter ->
                    notifications = when (filter) {
                        "All" -> getDummyNotifications()
                        "Unread" -> getDummyNotifications().filter { !it.isRead }
                        "Read" -> getDummyNotifications().filter { it.isRead }
                        else -> getDummyNotifications()
                    }
                }
            )

            // Notifications List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(notifications) { index, notification ->
                    var isVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(index * 50L)
                        isVisible = true
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(400)) + androidx.compose.animation.slideInVertically(
                            initialOffsetY = { it / 2 }
                        )
                    ) {
                        NotificationCard(
                            notification = notification,
                            onMarkAsRead = {
                                notifications = notifications.map {
                                    if (it.id == notification.id) it.copy(isRead = true) else it
                                }
                            },
                            onDelete = {
                                notifications = notifications.filter { it.id != notification.id }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Notifications",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Stay updated with your activity",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Animated bell icon
        val scale by rememberInfiniteTransition(label = "bell").animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bell_scale"
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FilterTabs(onFilterChange: (String) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Unread", "Read")

    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        containerColor = Color.Transparent,
        contentColor = Color.White,
        indicator = { tabPositions ->
            if (selectedTab < tabPositions.size) {
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(4.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF8B5CF6)
                                )
                            )
                        )
                )
            }
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = {
                    selectedTab = index
                    onFilterChange(title)
                },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCard(
    notification: NotificationItem,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead)
                Color(0xFF1E1E2E).copy(alpha = 0.5f)
            else
                Color(0xFF1E1E2E).copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (notification.isRead) 2.dp else 8.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    notification.accentColor,
                                    notification.accentColor.copy(alpha = 0.6f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = notification.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = notification.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        if (!notification.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(notification.accentColor)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = notification.message,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        lineHeight = 20.sp,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = notification.time,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            // Action buttons when expanded
            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!notification.isRead) {
                        Button(
                            onClick = onMarkAsRead,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = notification.accentColor.copy(alpha = 0.2f),
                                contentColor = notification.accentColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Mark as Read", fontSize = 13.sp)
                        }
                    }

                    Button(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444).copy(alpha = 0.2f),
                            contentColor = Color(0xFFEF4444)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// Dummy data
fun getDummyNotifications(): List<NotificationItem> {
    return listOf(
        NotificationItem(
            id = 1,
            title = "New Message from Sarah",
            message = "Hey! I loved your latest post about mobile development. Would love to collaborate on a project together!",
            time = "2 minutes ago",
            icon = Icons.Default.Email,
            accentColor = Color(0xFF6366F1),
            isRead = false
        ),
        NotificationItem(
            id = 2,
            title = "System Update Available",
            message = "A new version of the app is available. Update now to get the latest features and improvements.",
            time = "1 hour ago",
            icon = Icons.Default.Info,
            accentColor = Color(0xFF8B5CF6),
            isRead = false
        ),
        NotificationItem(
            id = 3,
            title = "Payment Successful",
            message = "Your payment of $49.99 for Premium Subscription has been processed successfully.",
            time = "3 hours ago",
            icon = Icons.Default.CheckCircle,
            accentColor = Color(0xFF10B981),
            isRead = true
        ),
        NotificationItem(
            id = 4,
            title = "New Follower",
            message = "Alex Johnson started following you. Check out their profile!",
            time = "5 hours ago",
            icon = Icons.Default.Person,
            accentColor = Color(0xFFF59E0B),
            isRead = false
        ),
        NotificationItem(
            id = 5,
            title = "Security Alert",
            message = "We detected a new login from an unrecognized device. If this wasn't you, please secure your account immediately.",
            time = "Yesterday",
            icon = Icons.Default.Warning,
            accentColor = Color(0xFFEF4444),
            isRead = true
        ),
        NotificationItem(
            id = 6,
            title = "Your post got 100 likes!",
            message = "Your post 'Building Beautiful UIs with Jetpack Compose' just reached 100 likes. Keep up the great work!",
            time = "Yesterday",
            icon = Icons.Default.Favorite,
            accentColor = Color(0xFFEC4899),
            isRead = true
        ),
        NotificationItem(
            id = 7,
            title = "Event Reminder",
            message = "Don't forget! The Android Developer Conference starts in 2 days. Mark your calendar!",
            time = "2 days ago",
            icon = Icons.Default.DateRange,
            accentColor = Color(0xFF3B82F6),
            isRead = false
        ),
        NotificationItem(
            id = 8,
            title = "Comment on Your Post",
            message = "Michael commented: 'This is exactly what I was looking for! Thanks for sharing.'",
            time = "3 days ago",
            icon = Icons.Default.MailOutline,
            accentColor = Color(0xFF06B6D4),
            isRead = true
        )
    )
}