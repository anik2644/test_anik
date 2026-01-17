package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToLetDetailsScreen(
    property: ToLetProperty,
    onBackClick: () -> Unit = {},
    onBookClick: () -> Unit = {}
) {
    var showBookingDialog by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
        ) {
            // Image Gallery Section
            item {
                ImageGallerySection(property = property)
            }

            // Quick Info Bar
            item {
                QuickInfoBar(property = property)
            }

            // Price and Title Section
            item {
                PriceAndTitleSection(property = property)
            }

            // Property Overview
            item {
                PropertyOverviewSection(property = property)
            }

            // Description
            item {
                DescriptionSection(description = property.description)
            }

            // Amenities
            item {
                AmenitiesSection(amenities = property.amenities)
            }

            // Additional Details
            item {
                AdditionalDetailsSection(property = property)
            }

            // Price Breakdown
            item {
                PriceBreakdownSection(property = property)
            }

            // Owner/Contact Section
            item {
                OwnerContactSection(property = property)
            }

            // Location Section
            item {
                LocationSection(property = property)
            }

            // Reviews Section
            item {
                ReviewsSection(property = property)
            }

            // Bottom Spacer for FAB
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Top Bar
        PropertyDetailsTopBar(
            onBackClick = onBackClick,
            isFavorite = isFavorite,
            onFavoriteClick = { isFavorite = !isFavorite },
            onShareClick = { }
        )

        // Bottom Action Bar
        BottomActionBar(
            price = property.price,
            onBookClick = { showBookingDialog = true },
            onCallClick = { }
        )
    }

    // Booking Dialog
    if (showBookingDialog) {
        BookingDialog(
            property = property,
            onDismiss = { showBookingDialog = false },
            onConfirm = {
                showBookingDialog = false
                onBookClick()
            }
        )
    }
}

@Composable
fun ImageGallerySection(property: ToLetProperty) {
    var currentImageIndex by remember { mutableStateOf(0) }
    val imageCount = 5 // Mock image count

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
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

        // Image Counter
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .align(Alignment.BottomEnd)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "1/$imageCount",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        // Verified Badge
        if (property.isVerified) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF10B981))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .align(Alignment.TopStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Verified Owner",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun BoxScope.PropertyDetailsTopBar(
    onBackClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp)
            .align(Alignment.TopStart),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF1F2937)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = onShareClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color(0xFF1F2937)
                )
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color(0xFFEF4444)
                )
            }
        }
    }
}

@Composable
fun QuickInfoBar(property: ToLetProperty) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-20).dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            QuickInfoItem(
                icon = Icons.Default.KingBed,
                value = property.bedrooms.toString(),
                label = "Bedrooms"
            )
            VerticalDivider()
            QuickInfoItem(
                icon = Icons.Default.Shower,
                value = property.bathrooms.toString(),
                label = "Bathrooms"
            )
            VerticalDivider()
            QuickInfoItem(
                icon = Icons.Default.SquareFoot,
                value = property.size.replace(" sqft", ""),
                label = "Sq Ft"
            )
        }
    }
}

@Composable
fun QuickInfoItem(icon: ImageVector, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(50.dp)
            .background(Color(0xFFE5E7EB))
    )
}

@Composable
fun PriceAndTitleSection(property: ToLetProperty) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = property.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            lineHeight = 32.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "${property.location}, ${property.area}",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = property.price,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )
                Text(
                    text = "per month",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFBBF24).copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${property.rating}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "(${property.totalReviews})",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun PropertyOverviewSection(property: ToLetProperty) {
    SectionCard(title = "Property Overview") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OverviewItem(label = "Property Type", value = property.type)
            OverviewItem(label = "Floor Number", value = property.floorNumber)
            OverviewItem(label = "Furnishing", value = property.furnishingStatus)
            OverviewItem(label = "Available From", value = property.availableFrom)
            OverviewItem(
                label = "Parking",
                value = if (property.parkingAvailable) "Available" else "Not Available"
            )
            OverviewItem(
                label = "Pet Friendly",
                value = if (property.petFriendly) "Yes" else "No"
            )
        }
    }
}

@Composable
fun OverviewItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )
    }
}

@Composable
fun DescriptionSection(description: String) {
    var isExpanded by remember { mutableStateOf(false) }

    SectionCard(title = "Description") {
        Column {
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF374151),
                lineHeight = 22.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3
            )
            if (description.length > 150) {
                Text(
                    text = if (isExpanded) "Show less" else "Read more",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEF4444),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { isExpanded = !isExpanded }
                )
            }
        }
    }
}

@Composable
fun AmenitiesSection(amenities: List<String>) {
    SectionCard(title = "Amenities") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            amenities.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { amenity ->
                        AmenityChip(
                            text = amenity,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill empty space if odd number
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityChip(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF3F4F6))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
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

@Composable
fun AdditionalDetailsSection(property: ToLetProperty) {
    SectionCard(title = "Additional Details") {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DetailRow(icon = Icons.Default.CalendarToday, label = "Posted", value = property.postedDate)
            DetailRow(icon = Icons.Default.Person, label = "Property ID", value = "#${property.id.toString().padStart(6, '0')}")
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )
    }
}

@Composable
fun PriceBreakdownSection(property: ToLetProperty) {
    SectionCard(title = "Price Breakdown") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PriceRow(label = "Monthly Rent", value = property.price)
            PriceRow(label = "Security Deposit", value = "৳${property.price.replace("৳", "").replace(",", "").toIntOrNull()?.times(2)?.let { "%,d".format(it) } ?: "N/A"}", isHighlight = false)
            PriceRow(label = "Service Charge", value = "৳3,000", isHighlight = false)
            Divider(color = Color(0xFFE5E7EB))
            PriceRow(
                label = "Total (First Month)",
                value = "৳${property.price.replace("৳", "").replace(",", "").toIntOrNull()?.let { it * 2 + 3000 }?.let { "%,d".format(it) } ?: "N/A"}",
                isHighlight = true
            )
        }
    }
}

@Composable
fun PriceRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isHighlight) 15.sp else 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) Color(0xFF1F2937) else Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = if (isHighlight) 16.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) Color(0xFFEF4444) else Color(0xFF1F2937)
        )
    }
}

@Composable
fun OwnerContactSection(property: ToLetProperty) {
    SectionCard(title = "Owner Information") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEF4444),
                                    Color(0xFFF97316)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = property.ownerName.first().toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = property.ownerName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Property Owner",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContactButton(
                    icon = Icons.Default.Call,
                    text = "Call",
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                ContactButton(
                    icon = Icons.Default.Email,
                    text = "Email",
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                ContactButton(
                    icon = Icons.Default.Chat,
                    text = "Message",
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun ContactButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF3F4F6),
            contentColor = Color(0xFF374151)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LocationSection(property: ToLetProperty) {
    SectionCard(title = "Location") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Map Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "Map View",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${property.location}, ${property.area}",
                    fontSize = 14.sp,
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ReviewsSection(property: ToLetProperty) {
    SectionCard(title = "Reviews & Ratings") {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Overall Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = property.rating.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < property.rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${property.totalReviews} reviews",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Write Review", fontSize = 13.sp)
                }
            }

            // Sample Review
            ReviewItem(
                reviewerName = "John Doe",
                rating = 5f,
                comment = "Great property! The owner was very helpful and the apartment is exactly as described.",
                date = "2 weeks ago"
            )
        }
    }
}

@Composable
fun ReviewItem(
    reviewerName: String,
    rating: Float,
    comment: String,
    date: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9FAFB))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = reviewerName.first().toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    text = reviewerName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }
            Text(
                text = date,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(5) { index ->
                Icon(
                    imageVector = if (index < rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Text(
            text = comment,
            fontSize = 13.sp,
            color = Color(0xFF374151),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun BoxScope.BottomActionBar(
    price: String,
    onBookClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = price,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )
                Text(
                    text = "per month",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Button(
                onClick = onCallClick,
                modifier = Modifier
                    .height(56.dp)
                    .width(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F4F6)
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(24.dp)
                )
            }

            Button(
                onClick = onBookClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
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
                                    Color(0xFFEF4444),
                                    Color(0xFFF97316)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Book Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun BookingDialog(
    property: ToLetProperty,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var moveInDate by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("12 Months") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Book Property",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Please provide booking details",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )

                OutlinedTextField(
                    value = moveInDate,
                    onValueChange = { moveInDate = it },
                    label = { Text("Move-in Date") },
                    placeholder = { Text("DD/MM/YYYY") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Column {
                    Text(
                        text = "Contract Duration",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("6 Months", "12 Months", "24 Months").forEach { option ->
                            FilterChip(
                                selected = duration == option,
                                onClick = { duration = option },
                                label = { Text(option, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Price summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEF2F2)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Total First Payment",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                        Text(
                            text = property.price.replace("৳", "").replace(",", "")
                                .toIntOrNull()?.let { "৳${"%,d".format(it * 2 + 3000)}" } ?: "N/A",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                        Text(
                            text = "Includes: Rent + Security + Service Charge",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirm Booking")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF6B7280))
            }
        }
    )
}