package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Data Models
data class Category(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val description: String
)

data class UpdateItem(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val icon: ImageVector,
    val accentColor: Color,
    val price: String? = null,
    val location: String? = null,
    val rating: Float? = null,
    val bloodGroup: String? = null,  // Added for Blood Donation
    val urgency: String? = null       // Added for Blood Donation urgency level
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = getCategories()
    val updates = getRecentUpdates()

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
                        Color(0xFFFEF3C7),
                        Color(0xFFFBCFE8),
                        Color(0xFFDDD6FE)
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                HomeTopBar()
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Welcome Section
                item {
                    WelcomeSection()
                }

                // Category Cards
                item {
                    Text(
                        text = "Explore Categories",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                    CategoryCardsSection(categories = categories)
                }

                // Quick Stats - Updated with Blood Donation stat
                item {
                    QuickStatsSection()
                }

                // Recent Updates Section
                item {
                    Text(
                        text = "Recent Updates",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }

                // Filter Tabs - Added Blood Donation
                item {
                    FilterTabs(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                // Update Items
                itemsIndexed(
                    if (selectedCategory == "All") updates
                    else updates.filter { it.category == selectedCategory }
                ) { index, update ->
                    var isVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(index * 50L)
                        isVisible = true
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(400)) +
                                slideInVertically(initialOffsetY = { it / 2 })
                    ) {
                        UpdateItemCard(
                            update = update,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }

                // Bottom Spacer
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    Surface(
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Profile Avatar
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFF59E0B),
                                    Color(0xFFEF4444)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "M",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = "Hello Anik! 👋",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Welcome back",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF6B7280)
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Badge {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6366F1),
                        Color(0xFF8B5CF6),
                        Color(0xFFEC4899)
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Discover Your Next Adventure",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 32.sp
            )
            Text(
                text = "Find the best places, services, and experiences tailored for you",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6366F1)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(44.dp)
            ) {
                Text("Get Started", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryCardsSection(categories: List<Category>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(index * 100L)
                isVisible = true
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(500)) +
                        scaleIn(initialScale = 0.8f)
            ) {
                CategoryCard(category = category)
            }
        }
    }
}

@Composable
fun CategoryCard(category: Category) {
    val scale = remember { Animatable(1f) }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp)
            .scale(scale.value)
            .clickable {
                // Handle click
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = category.gradient
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.name,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column {
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = category.description,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStatsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Active",
                value = "248",
                icon = Icons.Default.List,
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Saved",
                value = "52",
                icon = Icons.Default.Favorite,
                color = Color(0xFFEF4444),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Views",
                value = "1.2K",
                icon = Icons.Default.RemoveRedEye,
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
        }

        // Blood Donation Quick Action Card
        BloodDonationQuickCard()
    }
}

@Composable
fun BloodDonationQuickCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle blood donation quick action */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDC2626).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDC2626)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bloodtype,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "Urgent Blood Needed",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                    Text(
                        text = "5 active requests near you",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun FilterTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    // Added "Blood" to the tabs list
    val tabs = listOf("All", "To-Let", "Medical", "Education", "Tour", "Blood")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tabs) { tab ->
            FilterChip(
                selected = selectedCategory == tab,
                onClick = { onCategorySelected(tab) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Add blood drop icon for Blood tab
                        if (tab == "Blood") {
                            Icon(
                                imageVector = Icons.Default.Bloodtype,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedCategory == tab) Color.White else Color(0xFFDC2626)
                            )
                        }
                        Text(
                            text = tab,
                            fontSize = 14.sp,
                            fontWeight = if (selectedCategory == tab) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = if (tab == "Blood") Color(0xFFDC2626) else Color(0xFF6366F1),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White.copy(alpha = 0.9f),
                    labelColor = Color(0xFF6B7280)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedCategory == tab,
                    borderColor = if (tab == "Blood") Color(0xFFDC2626).copy(alpha = 0.5f) else Color(0xFFE5E7EB),
                    selectedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun UpdateItemCard(update: UpdateItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(update.accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = update.icon,
                        contentDescription = null,
                        tint = update.accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = update.category,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = update.accentColor,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(update.accentColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )

                            // Urgency badge for Blood Donation
                            update.urgency?.let { urgency ->
                                val urgencyColor = when (urgency) {
                                    "Critical" -> Color(0xFFDC2626)
                                    "Urgent" -> Color(0xFFF59E0B)
                                    else -> Color(0xFF10B981)
                                }
                                Text(
                                    text = urgency,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(urgencyColor)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = update.timestamp,
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }

                    Text(
                        text = update.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = update.description,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )
                }
            }

            // Additional Info
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Blood Group Badge
                update.bloodGroup?.let { bloodGroup ->
                    BloodGroupBadge(bloodGroup = bloodGroup)
                }

                update.price?.let { price ->
                    InfoBadge(
                        icon = Icons.Default.AttachMoney,
                        text = price,
                        color = Color(0xFF10B981)
                    )
                }
                update.location?.let { location ->
                    InfoBadge(
                        icon = Icons.Default.LocationOn,
                        text = location,
                        color = Color(0xFF3B82F6)
                    )
                }
                update.rating?.let { rating ->
                    InfoBadge(
                        icon = Icons.Default.Star,
                        text = rating.toString(),
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }
    }
}

@Composable
fun BloodGroupBadge(bloodGroup: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFDC2626).copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Bloodtype,
            contentDescription = null,
            tint = Color(0xFFDC2626),
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = bloodGroup,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDC2626)
        )
    }
}

@Composable
fun InfoBadge(icon: ImageVector, text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151)
        )
    }
}

// Mock Data Functions
fun getCategories(): List<Category> {
    return listOf(
        Category(
            id = 1,
            name = "To-Let",
            icon = Icons.Default.Home,
            gradient = listOf(Color(0xFFEF4444), Color(0xFFF97316)),
            description = "Find your perfect home"
        ),
        Category(
            id = 2,
            name = "Medical Info",
            icon = Icons.Default.LocalHospital,
            gradient = listOf(Color(0xFF10B981), Color(0xFF059669)),
            description = "Healthcare services"
        ),
        Category(
            id = 3,
            name = "Education",
            icon = Icons.Default.School,
            gradient = listOf(Color(0xFF3B82F6), Color(0xFF2563EB)),
            description = "Learning opportunities"
        ),
        Category(
            id = 4,
            name = "Tour",
            icon = Icons.Default.Flight,
            gradient = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
            description = "Explore destinations"
        ),
        // Added Blood Donation Category
        Category(
            id = 5,
            name = "Blood Donation",
            icon = Icons.Default.Bloodtype,
            gradient = listOf(Color(0xFFDC2626), Color(0xFFB91C1C)),
            description = "Save lives, donate blood"
        )
    )
}

fun getRecentUpdates(): List<UpdateItem> {
    return listOf(
        // To-Let Updates
        UpdateItem(
            id = 1,
            category = "To-Let",
            title = "Luxury 3BHK Apartment in Gulshan",
            description = "Spacious apartment with modern amenities, 24/7 security, and parking facility.",
            timestamp = "2 hours ago",
            icon = Icons.Default.Home,
            accentColor = Color(0xFFEF4444),
            price = "৳45,000/mo",
            location = "Gulshan-2"
        ),
        UpdateItem(
            id = 2,
            category = "To-Let",
            title = "Cozy Studio Near University",
            description = "Perfect for students, fully furnished with high-speed internet and utilities included.",
            timestamp = "5 hours ago",
            icon = Icons.Default.Home,
            accentColor = Color(0xFFEF4444),
            price = "৳18,000/mo",
            location = "Mohakhali"
        ),
        UpdateItem(
            id = 3,
            category = "To-Let",
            title = "Commercial Space in Banani",
            description = "Prime location for office or retail business with excellent connectivity.",
            timestamp = "1 day ago",
            icon = Icons.Default.Home,
            accentColor = Color(0xFFEF4444),
            price = "৳80,000/mo",
            location = "Banani"
        ),

        // Blood Donation Updates
        UpdateItem(
            id = 13,
            category = "Blood",
            title = "Urgent: A+ Blood Needed",
            description = "Patient at Square Hospital requires 3 bags of A+ blood for emergency surgery. Please help if you can.",
            timestamp = "30 mins ago",
            icon = Icons.Default.Bloodtype,
            accentColor = Color(0xFFDC2626),
            bloodGroup = "A+",
            location = "Square Hospital",
            urgency = "Critical"
        ),
        UpdateItem(
            id = 14,
            category = "Blood",
            title = "B- Blood Required",
            description = "Cancer patient needs regular blood transfusion. B- donors please contact immediately.",
            timestamp = "1 hour ago",
            icon = Icons.Default.Bloodtype,
            accentColor = Color(0xFFDC2626),
            bloodGroup = "B-",
            location = "Dhaka Medical",
            urgency = "Urgent"
        ),
        UpdateItem(
            id = 15,
            category = "Blood",
            title = "O+ Donors Needed",
            description = "Blood bank running low on O+ blood. Healthy donors aged 18-60 are welcome.",
            timestamp = "2 hours ago",
            icon = Icons.Default.Bloodtype,
            accentColor = Color(0xFFDC2626),
            bloodGroup = "O+",
            location = "Sandhani Blood Bank",
            urgency = "Normal"
        ),
        UpdateItem(
            id = 16,
            category = "Blood",
            title = "AB- Rare Blood Emergency",
            description = "Rare blood type needed for accident victim. AB- donors please respond urgently.",
            timestamp = "3 hours ago",
            icon = Icons.Default.Bloodtype,
            accentColor = Color(0xFFDC2626),
            bloodGroup = "AB-",
            location = "United Hospital",
            urgency = "Critical"
        ),
        UpdateItem(
            id = 17,
            category = "Blood",
            title = "Blood Donation Camp Tomorrow",
            description = "Join us for a blood donation camp organized by Red Crescent. Free health checkup for all donors.",
            timestamp = "5 hours ago",
            icon = Icons.Default.Bloodtype,
            accentColor = Color(0xFFDC2626),
            location = "Dhanmondi 27",
            rating = 4.9f
        ),
        UpdateItem(
            id = 18,
            category = "Blood",
            title = "Platelet Donation Request",
            description = "Dengue patient requires platelet donation. Any blood group compatible donors welcome.",
            timestamp = "6 hours ago",
            icon = Icons.Default.Bloodtype,
            accentColor = Color(0xFFDC2626),
            bloodGroup = "Any",
            location = "Apollo Hospital",
            urgency = "Urgent"
        ),

        // Medical Updates
        UpdateItem(
            id = 4,
            category = "Medical",
            title = "Free Health Checkup Camp",
            description = "General health screening, blood pressure check, and doctor consultation available.",
            timestamp = "3 hours ago",
            icon = Icons.Default.LocalHospital,
            accentColor = Color(0xFF10B981),
            location = "Dhanmondi",
            rating = 4.8f
        ),
        UpdateItem(
            id = 5,
            category = "Medical",
            title = "New Diagnostic Center Opens",
            description = "State-of-the-art medical equipment with 24/7 emergency services.",
            timestamp = "6 hours ago",
            icon = Icons.Default.LocalHospital,
            accentColor = Color(0xFF10B981),
            location = "Uttara",
            rating = 4.5f
        ),
        UpdateItem(
            id = 6,
            category = "Medical",
            title = "Dental Care Special Offer",
            description = "Get 30% off on all dental treatments this month. Expert dentists available.",
            timestamp = "1 day ago",
            icon = Icons.Default.LocalHospital,
            accentColor = Color(0xFF10B981),
            price = "From ৳500",
            location = "Mirpur"
        ),

        // Education Updates
        UpdateItem(
            id = 7,
            category = "Education",
            title = "Web Development Bootcamp",
            description = "Learn full-stack development from industry experts. 6-month intensive course.",
            timestamp = "4 hours ago",
            icon = Icons.Default.School,
            accentColor = Color(0xFF3B82F6),
            price = "৳35,000",
            rating = 4.9f
        ),
        UpdateItem(
            id = 8,
            category = "Education",
            title = "IELTS Preparation Course",
            description = "Expert guidance for IELTS exam. Weekend batches available.",
            timestamp = "7 hours ago",
            icon = Icons.Default.School,
            accentColor = Color(0xFF3B82F6),
            price = "৳15,000",
            location = "Dhanmondi"
        ),
        UpdateItem(
            id = 9,
            category = "Education",
            title = "Free Digital Marketing Workshop",
            description = "Learn SEO, social media marketing, and content strategy in this 2-day workshop.",
            timestamp = "2 days ago",
            icon = Icons.Default.School,
            accentColor = Color(0xFF3B82F6),
            location = "Gulshan",
            rating = 4.7f
        ),

        // Tour Updates
        UpdateItem(
            id = 10,
            category = "Tour",
            title = "Cox's Bazar Beach Resort Package",
            description = "3 days 2 nights all-inclusive beach holiday with adventure activities.",
            timestamp = "5 hours ago",
            icon = Icons.Default.Flight,
            accentColor = Color(0xFFF59E0B),
            price = "৳12,500/person",
            rating = 4.6f
        ),
        UpdateItem(
            id = 11,
            category = "Tour",
            title = "Sajek Valley Adventure Tour",
            description = "Experience the beauty of hill tracks with camping and trekking activities.",
            timestamp = "8 hours ago",
            icon = Icons.Default.Flight,
            accentColor = Color(0xFFF59E0B),
            price = "৳8,000/person",
            location = "Sajek"
        ),
        UpdateItem(
            id = 12,
            category = "Tour",
            title = "Sundarbans Wildlife Safari",
            description = "Explore the world's largest mangrove forest and spot royal Bengal tigers.",
            timestamp = "1 day ago",
            icon = Icons.Default.Flight,
            accentColor = Color(0xFFF59E0B),
            price = "৳15,000/person",
            rating = 4.9f
        )
    )
}