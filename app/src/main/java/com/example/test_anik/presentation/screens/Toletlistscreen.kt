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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// Data model
data class ToLetProperty(
    val id: Int,
    val title: String,
    val location: String,
    val area: String,
    val price: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val size: String,
    val type: String,
    val rating: Float,
    val totalReviews: Int,
    val isVerified: Boolean = false,
    val isFeatured: Boolean = false,
    val floorNumber: String = "",
    val furnishingStatus: String = "",
    val availableFrom: String = "",
    val parkingAvailable: Boolean = false,
    val petFriendly: Boolean = false,
    val description: String = "",
    val amenities: List<String> = emptyList(),
    val postedDate: String = "",
    val ownerName: String = "",
    val ownerContact: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToLetListScreen(
    properties: List<ToLetProperty>,
    onBackClick: () -> Unit = {},
    onPropertyClick: (ToLetProperty) -> Unit = {},
    onAddPropertyClick: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProperties = remember(properties, selectedFilter, searchQuery) {
        properties.filter { property ->
            val matchesFilter = when (selectedFilter) {
                "All" -> true
                "Apartment" -> property.type == "Apartment"
                "House" -> property.type == "House"
                "Studio" -> property.type == "Studio"
                "Featured" -> property.isFeatured
                else -> true
            }

            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                property.title.contains(searchQuery, ignoreCase = true) ||
                        property.location.contains(searchQuery, ignoreCase = true) ||
                        property.area.contains(searchQuery, ignoreCase = true)
            }

            matchesFilter && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            ToLetTopBar(
                onBackClick = onBackClick,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPropertyClick,
                containerColor = Color(0xFFEF4444),
                contentColor = Color.White,
                modifier = Modifier
                    .size(64.dp)
                    .shadow(8.dp, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Property",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Filter Tabs
            item {
                FilterSection(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }

            // Results Count
            item {
                Text(
                    text = "${filteredProperties.size} Properties Found",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }

            // Property List
            itemsIndexed(filteredProperties) { index, property ->
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
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }

            // Empty State
            if (filteredProperties.isEmpty()) {
                item {
                    EmptyStateView(searchQuery = searchQuery)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToLetTopBar(
    onBackClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            // Title Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1F2937)
                    )
                }

                Text(
                    text = "To-Let Properties",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Color(0xFF1F2937)
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                placeholder = { Text("Search by location, title...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF)
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
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFEF4444),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    cursorColor = Color(0xFFEF4444),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("All", "Apartment", "House", "Studio", "Featured")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter,
                        fontSize = 14.sp,
                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = if (filter == "Featured") {
                    {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFEF4444),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF6B7280),
                    selectedLeadingIconColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedFilter == filter,
                    borderColor = Color(0xFFE5E7EB),
                    selectedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun PropertyCard(
    property: ToLetProperty,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Image Placeholder with Gradient
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

                // Badges Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (property.isFeatured) {
                            Badge(
                                text = "Featured",
                                backgroundColor = Color(0xFFFBBF24),
                                icon = Icons.Default.Star
                            )
                        }
                        if (property.isVerified) {
                            Badge(
                                text = "Verified",
                                backgroundColor = Color(0xFF10B981),
                                icon = Icons.Default.CheckCircle
                            )
                        }
                    }

                    // Favorite Button
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title and Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = property.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${property.location}, ${property.area}",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = property.price,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                        Text(
                            text = "/month",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }

                Divider(color = Color(0xFFE5E7EB))

                // Property Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PropertyDetailItem(
                        icon = Icons.Default.KingBed,
                        value = property.bedrooms.toString(),
                        label = "Beds"
                    )
                    PropertyDetailItem(
                        icon = Icons.Default.Shower,
                        value = property.bathrooms.toString(),
                        label = "Baths"
                    )
                    PropertyDetailItem(
                        icon = Icons.Default.SquareFoot,
                        value = property.size.replace(" sqft", ""),
                        label = "Sq Ft"
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = property.rating.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(16.dp)
        )
        Column {
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

@Composable
fun Badge(
    text: String,
    backgroundColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun EmptyStateView(searchQuery: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = if (searchQuery.isNotBlank()) {
                "No properties found for \"$searchQuery\""
            } else {
                "No properties available"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6B7280)
        )
        Text(
            text = "Try adjusting your search or filters",
            fontSize = 14.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}

// Mock Data Function
fun getToLetProperties(): List<ToLetProperty> {
    return listOf(
        ToLetProperty(
            id = 1,
            title = "Luxury 3BHK Apartment in Gulshan",
            location = "Gulshan-2",
            area = "Dhaka",
            price = "৳45,000",
            bedrooms = 3,
            bathrooms = 2,
            size = "1500 sqft",
            type = "Apartment",
            rating = 4.8f,
            totalReviews = 24,
            isVerified = true,
            isFeatured = true,
            floorNumber = "5th Floor",
            furnishingStatus = "Semi-Furnished",
            availableFrom = "Immediate",
            parkingAvailable = true,
            petFriendly = false,
            description = "Spacious 3 bedroom apartment with modern amenities. Located in the heart of Gulshan with easy access to restaurants, shopping centers, and offices.",
            amenities = listOf("Wi-Fi", "Air Conditioning", "Gym", "Swimming Pool", "Elevator", "Security", "Balcony"),
            postedDate = "2 days ago",
            ownerName = "Karim Ahmed",
            ownerContact = "+880 1712-345678"
        ),
        ToLetProperty(
            id = 2,
            title = "Cozy Studio Apartment Near University",
            location = "Mohakhali",
            area = "Dhaka",
            price = "৳18,000",
            bedrooms = 1,
            bathrooms = 1,
            size = "600 sqft",
            type = "Studio",
            rating = 4.5f,
            totalReviews = 18,
            isVerified = true,
            isFeatured = false,
            floorNumber = "3rd Floor",
            furnishingStatus = "Fully Furnished",
            availableFrom = "1st June",
            parkingAvailable = false,
            petFriendly = true,
            description = "Perfect for students or young professionals. Fully furnished studio with all necessary amenities.",
            amenities = listOf("Wi-Fi", "Air Conditioning", "Elevator", "Security"),
            postedDate = "1 week ago",
            ownerName = "Fatima Rahman",
            ownerContact = "+880 1856-234567"
        ),
        ToLetProperty(
            id = 3,
            title = "Spacious 4BHK House in Banani",
            location = "Banani",
            area = "Dhaka",
            price = "৳80,000",
            bedrooms = 4,
            bathrooms = 3,
            size = "2500 sqft",
            type = "House",
            rating = 4.9f,
            totalReviews = 32,
            isVerified = true,
            isFeatured = true,
            floorNumber = "Ground + 1st Floor",
            furnishingStatus = "Semi-Furnished",
            availableFrom = "15th June",
            parkingAvailable = true,
            petFriendly = true,
            description = "Beautiful independent house with a private garden. Perfect for families looking for spacious living.",
            amenities = listOf("Wi-Fi", "Air Conditioning", "Garden", "Parking", "Generator", "CCTV"),
            postedDate = "3 days ago",
            ownerName = "Rahim Uddin",
            ownerContact = "+880 1923-456789"
        ),
        ToLetProperty(
            id = 4,
            title = "Modern 2BHK in Dhanmondi",
            location = "Dhanmondi-27",
            area = "Dhaka",
            price = "৳35,000",
            bedrooms = 2,
            bathrooms = 2,
            size = "1200 sqft",
            type = "Apartment",
            rating = 4.6f,
            totalReviews = 15,
            isVerified = false,
            isFeatured = false,
            floorNumber = "7th Floor",
            furnishingStatus = "Unfurnished",
            availableFrom = "Immediate",
            parkingAvailable = true,
            petFriendly = false,
            description = "Contemporary apartment in a prime location. Close to schools, hospitals, and shopping areas.",
            amenities = listOf("Elevator", "Security", "Parking", "Balcony"),
            postedDate = "5 days ago",
            ownerName = "Nasir Khan",
            ownerContact = "+880 1634-567890"
        ),
        ToLetProperty(
            id = 5,
            title = "Penthouse in Uttara",
            location = "Uttara Sector 7",
            area = "Dhaka",
            price = "৳95,000",
            bedrooms = 5,
            bathrooms = 4,
            size = "3200 sqft",
            type = "Apartment",
            rating = 4.9f,
            totalReviews = 28,
            isVerified = true,
            isFeatured = true,
            floorNumber = "15th Floor (Top Floor)",
            furnishingStatus = "Fully Furnished",
            availableFrom = "1st July",
            parkingAvailable = true,
            petFriendly = true,
            description = "Luxurious penthouse with panoramic city views. Premium finishes and state-of-the-art amenities.",
            amenities = listOf("Wi-Fi", "Air Conditioning", "Gym", "Swimming Pool", "Elevator", "Security", "Balcony", "Garden", "CCTV", "Generator"),
            postedDate = "1 day ago",
            ownerName = "Hasan Mahmud",
            ownerContact = "+880 1789-123456"
        )
    )
}