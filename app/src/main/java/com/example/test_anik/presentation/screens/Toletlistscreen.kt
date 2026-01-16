package com.example.test_anik.presentation.screens.tolet

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Data Models
data class ToLetProperty(
    val id: Int,
    val title: String,
    val location: String,
    val area: String,
    val price: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val size: String,
    val type: String, // Apartment, House, Studio, etc.
    val description: String,
    val amenities: List<String>,
    val images: List<String>, // URLs or resource IDs
    val ownerName: String,
    val ownerPhone: String,
    val ownerEmail: String,
    val isVerified: Boolean,
    val isFeatured: Boolean,
    val availableFrom: String,
    val rating: Float,
    val totalReviews: Int,
    val floorNumber: String,
    val furnishingStatus: String, // Furnished, Semi-Furnished, Unfurnished
    val parkingAvailable: Boolean,
    val petFriendly: Boolean,
    val postedDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToLetListScreen(
    onPropertyClick: (ToLetProperty) -> Unit = {},
    onAddNewClick: () -> Unit = {}
) {
    var properties by remember { mutableStateOf(getMockProperties()) }
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

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
                        Color(0xFFFFF5F5),
                        Color(0xFFFEE2E2),
                        Color(0xFFFED7D7)
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ToLetTopBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddNewClick,
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New Property",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Header Stats
                item {
                    PropertyStatsHeader(totalProperties = properties.size)
                }

                // Filter Chips
                item {
                    FilterChipsRow(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it }
                    )
                }

                // Featured Properties Section
                item {
                    val featuredProperties = properties.filter { it.isFeatured }
                    if (featuredProperties.isNotEmpty()) {
                        FeaturedPropertiesSection(
                            properties = featuredProperties,
                            onPropertyClick = onPropertyClick
                        )
                    }
                }

                // All Properties Section
                item {
                    Text(
                        text = "All Properties",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }

                itemsIndexed(
                    properties.filter {
                        (selectedFilter == "All" || it.type == selectedFilter) &&
                                (searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true) ||
                                        it.location.contains(searchQuery, ignoreCase = true))
                    }
                ) { index, property ->
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
                        PropertyCard(
                            property = property,
                            onClick = { onPropertyClick(property) },
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToLetTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.95f))
            .statusBarsPadding()
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "To-Let Properties",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "Find your perfect home",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }

            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEE2E2))
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filters",
                    tint = Color(0xFFEF4444)
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            placeholder = {
                Text(
                    "Search by location or title...",
                    fontSize = 14.sp,
                    color = Color(0xFF9CA3AF)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFFEF4444)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color(0xFF9CA3AF)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEF4444),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PropertyStatsHeader(totalProperties: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Available",
            value = totalProperties.toString(),
            icon = Icons.Default.Home,
            color = Color(0xFFEF4444),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Featured",
            value = "8",
            icon = Icons.Default.Star,
            color = Color(0xFFF59E0B),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Verified",
            value = "12",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF10B981),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(85.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
fun FilterChipsRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("All", "Apartment", "House", "Studio", "Commercial")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter,
                        fontSize = 13.sp,
                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFEF4444),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF6B7280)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun FeaturedPropertiesSection(
    properties: List<ToLetProperty>,
    onPropertyClick: (ToLetProperty) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Featured Properties",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = "See all",
                fontSize = 13.sp,
                color = Color(0xFFEF4444),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(properties) { property ->
                FeaturedPropertyCard(
                    property = property,
                    onClick = { onPropertyClick(property) }
                )
            }
        }
    }
}

@Composable
fun FeaturedPropertyCard(
    property: ToLetProperty,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFEF4444).copy(alpha = 0.3f),
                                Color(0xFFF97316).copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            // Featured Badge
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF59E0B))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .align(Alignment.TopStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Featured",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Property Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF1F2937).copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = property.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = property.location,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Text(
                    text = property.price,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFBBF24),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PropertyCard(
    property: ToLetProperty,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFEF4444).copy(alpha = 0.2f),
                                Color(0xFFF97316).copy(alpha = 0.3f)
                            )
                        )
                    )
            ) {
                // Verified Badge
                if (property.isVerified) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Verified",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Favorite Icon
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Property Details
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title and Type
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = property.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = property.type,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFEF4444),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEF4444).copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${property.location} • ${property.area}",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                // Property Features
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PropertyFeature(
                        icon = Icons.Default.KingBed,
                        text = "${property.bedrooms} Bed"
                    )
                    PropertyFeature(
                        icon = Icons.Default.Shower,
                        text = "${property.bathrooms} Bath"
                    )
                    PropertyFeature(
                        icon = Icons.Default.SquareFoot,
                        text = property.size
                    )
                }

                Divider(color = Color(0xFFE5E7EB))

                // Price and Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = property.price,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                        Text(
                            text = "/month",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${property.rating}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "(${property.totalReviews})",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyFeature(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Medium
        )
    }
}

// Mock Data
fun getMockProperties(): List<ToLetProperty> {
    return listOf(
        ToLetProperty(
            id = 1,
            title = "Luxury 3BHK Apartment",
            location = "Gulshan-2",
            area = "Dhaka",
            price = "৳65,000",
            bedrooms = 3,
            bathrooms = 3,
            size = "1850 sqft",
            type = "Apartment",
            description = "Modern luxury apartment with premium amenities, spacious rooms, and stunning city views. Located in the heart of Gulshan.",
            amenities = listOf("WiFi", "Gym", "Swimming Pool", "24/7 Security", "Elevator", "Parking", "Generator"),
            images = listOf(),
            ownerName = "Ahmed Rahman",
            ownerPhone = "+880 1712345678",
            ownerEmail = "ahmed@example.com",
            isVerified = true,
            isFeatured = true,
            availableFrom = "Immediate",
            rating = 4.8f,
            totalReviews = 45,
            floorNumber = "7th Floor",
            furnishingStatus = "Fully Furnished",
            parkingAvailable = true,
            petFriendly = false,
            postedDate = "2 days ago"
        ),
        ToLetProperty(
            id = 2,
            title = "Modern Studio Apartment",
            location = "Banani",
            area = "Dhaka",
            price = "৳28,000",
            bedrooms = 1,
            bathrooms = 1,
            size = "650 sqft",
            type = "Studio",
            description = "Cozy studio perfect for students or young professionals. Fully furnished with modern appliances.",
            amenities = listOf("WiFi", "AC", "Furnished", "24/7 Security", "Generator"),
            images = listOf(),
            ownerName = "Sarah Khan",
            ownerPhone = "+880 1798765432",
            ownerEmail = "sarah@example.com",
            isVerified = true,
            isFeatured = false,
            availableFrom = "15th Feb",
            rating = 4.5f,
            totalReviews = 28,
            floorNumber = "3rd Floor",
            furnishingStatus = "Fully Furnished",
            parkingAvailable = false,
            petFriendly = true,
            postedDate = "5 days ago"
        ),
        ToLetProperty(
            id = 3,
            title = "Spacious 4BHK House",
            location = "Dhanmondi",
            area = "Dhaka",
            price = "৳85,000",
            bedrooms = 4,
            bathrooms = 4,
            size = "2500 sqft",
            type = "House",
            description = "Beautiful family house with garden, modern kitchen, and separate servant quarters.",
            amenities = listOf("Garden", "Parking", "Rooftop", "24/7 Security", "Generator", "Gas Connection"),
            images = listOf(),
            ownerName = "Mohammad Ali",
            ownerPhone = "+880 1923456789",
            ownerEmail = "ali@example.com",
            isVerified = true,
            isFeatured = true,
            availableFrom = "1st March",
            rating = 4.9f,
            totalReviews = 62,
            floorNumber = "Ground + 1st",
            furnishingStatus = "Semi-Furnished",
            parkingAvailable = true,
            petFriendly = true,
            postedDate = "1 week ago"
        ),
        ToLetProperty(
            id = 4,
            title = "Commercial Office Space",
            location = "Mohakhali",
            area = "Dhaka",
            price = "৳1,20,000",
            bedrooms = 0,
            bathrooms = 2,
            size = "3000 sqft",
            type = "Commercial",
            description = "Prime commercial space suitable for offices, showrooms, or retail business. Excellent location with high visibility.",
            amenities = listOf("Elevator", "Parking", "24/7 Security", "Generator", "High-Speed Internet"),
            images = listOf(),
            ownerName = "Karim Properties",
            ownerPhone = "+880 1834567890",
            ownerEmail = "karim@example.com",
            isVerified = true,
            isFeatured = true,
            availableFrom = "Immediate",
            rating = 4.6f,
            totalReviews = 34,
            floorNumber = "5th Floor",
            furnishingStatus = "Unfurnished",
            parkingAvailable = true,
            petFriendly = false,
            postedDate = "3 days ago"
        ),
        ToLetProperty(
            id = 5,
            title = "Cozy 2BHK Flat",
            location = "Uttara",
            area = "Dhaka",
            price = "৳32,000",
            bedrooms = 2,
            bathrooms = 2,
            size = "1100 sqft",
            type = "Apartment",
            description = "Well-maintained apartment in family-friendly neighborhood with easy access to schools and markets.",
            amenities = listOf("Balcony", "Security", "Gas", "Water Supply"),
            images = listOf(),
            ownerName = "Fatima Begum",
            ownerPhone = "+880 1645678901",
            ownerEmail = "fatima@example.com",
            isVerified = false,
            isFeatured = false,
            availableFrom = "20th Feb",
            rating = 4.2f,
            totalReviews = 18,
            floorNumber = "4th Floor",
            furnishingStatus = "Semi-Furnished",
            parkingAvailable = true,
            petFriendly = false,
            postedDate = "4 days ago"
        )
    )
}