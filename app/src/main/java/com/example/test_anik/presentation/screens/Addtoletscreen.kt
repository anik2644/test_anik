package com.example.test_anik.presentation.screens



import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLetScreen(
    onBackClick: () -> Unit = {},
    onPostCreated: (ToLetProperty) -> Unit = {}
) {
    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 4

    // Form Fields - Step 1: Basic Info
    var title by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("Apartment") }
    var location by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var bedrooms by remember { mutableStateOf("") }
    var bathrooms by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }

    // Step 2: Details
    var floorNumber by remember { mutableStateOf("") }
    var furnishingStatus by remember { mutableStateOf("Semi-Furnished") }
    var availableFrom by remember { mutableStateOf("") }
    var parkingAvailable by remember { mutableStateOf(false) }
    var petFriendly by remember { mutableStateOf(false) }

    // Step 3: Pricing & Description
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Step 4: Amenities & Contact
    var selectedAmenities by remember { mutableStateOf(setOf<String>()) }
    var ownerName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AddToLetTopBar(
                currentStep = currentStep,
                totalSteps = totalSteps,
                onBackClick = {
                    if (currentStep > 1) {
                        currentStep--
                    } else {
                        onBackClick()
                    }
                }
            )
        },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Step Indicator
                StepIndicator(currentStep = currentStep, totalSteps = totalSteps)

                Spacer(modifier = Modifier.height(24.dp))

                // Form Content based on current step
                when (currentStep) {
                    1 -> BasicInfoStep(
                        title = title,
                        onTitleChange = { title = it },
                        propertyType = propertyType,
                        onPropertyTypeChange = { propertyType = it },
                        location = location,
                        onLocationChange = { location = it },
                        area = area,
                        onAreaChange = { area = it },
                        bedrooms = bedrooms,
                        onBedroomsChange = { bedrooms = it },
                        bathrooms = bathrooms,
                        onBathroomsChange = { bathrooms = it },
                        size = size,
                        onSizeChange = { size = it }
                    )

                    2 -> DetailsStep(
                        floorNumber = floorNumber,
                        onFloorNumberChange = { floorNumber = it },
                        furnishingStatus = furnishingStatus,
                        onFurnishingStatusChange = { furnishingStatus = it },
                        availableFrom = availableFrom,
                        onAvailableFromChange = { availableFrom = it },
                        parkingAvailable = parkingAvailable,
                        onParkingChange = { parkingAvailable = it },
                        petFriendly = petFriendly,
                        onPetFriendlyChange = { petFriendly = it }
                    )

                    3 -> PricingDescriptionStep(
                        price = price,
                        onPriceChange = { price = it },
                        description = description,
                        onDescriptionChange = { description = it }
                    )

                    4 -> AmenitiesContactStep(
                        selectedAmenities = selectedAmenities,
                        onAmenitiesChange = { selectedAmenities = it },
                        ownerName = ownerName,
                        onOwnerNameChange = { ownerName = it },
                        contactNumber = contactNumber,
                        onContactNumberChange = { contactNumber = it }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

            // Bottom Navigation Buttons
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFEF4444)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                Color(0xFFEF4444)
                            )
                        ) {
                            Text(
                                "Previous",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < totalSteps) {
                                currentStep++
                            } else {
                                // Submit the form
                                isSubmitting = true
                                val newProperty = ToLetProperty(
                                    id = (1000..9999).random(),
                                    title = title,
                                    location = location,
                                    area = area,
                                    price = "৳${price.replace(",", "")}",
                                    bedrooms = bedrooms.toIntOrNull() ?: 0,
                                    bathrooms = bathrooms.toIntOrNull() ?: 0,
                                    size = "$size sqft",
                                    type = propertyType,
                                    rating = 0f,
                                    totalReviews = 0,
                                    isVerified = false,
                                    isFeatured = false,
                                    floorNumber = floorNumber,
                                    furnishingStatus = furnishingStatus,
                                    availableFrom = availableFrom,
                                    parkingAvailable = parkingAvailable,
                                    petFriendly = petFriendly,
                                    description = description,
                                    amenities = selectedAmenities.toList(),
                                    postedDate = "Just now",
                                    ownerName = ownerName,
                                    ownerContact = contactNumber
                                )
                                showSuccessDialog = true
                                onPostCreated(newProperty)
                            }
                        },
                        modifier = Modifier
                            .weight(if (currentStep > 1) 1f else 1f)
                            .height(56.dp),
                        enabled = !isSubmitting && isStepValid(
                            currentStep,
                            title,
                            location,
                            area,
                            bedrooms,
                            bathrooms,
                            size,
                            floorNumber,
                            availableFrom,
                            price,
                            description,
                            ownerName,
                            contactNumber
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color(0xFFE5E7EB)
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (isStepValid(
                                            currentStep,
                                            title,
                                            location,
                                            area,
                                            bedrooms,
                                            bathrooms,
                                            size,
                                            floorNumber,
                                            availableFrom,
                                            price,
                                            description,
                                            ownerName,
                                            contactNumber
                                        )
                                    ) {
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFEF4444),
                                                Color(0xFFF97316)
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
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = if (currentStep < totalSteps) "Next" else "Post Property",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                onBackClick()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToLetTopBar(
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Add New Property",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "Step $currentStep of $totalSteps",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSteps) { index ->
            val stepNumber = index + 1
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        if (stepNumber <= currentStep) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFEF4444),
                                    Color(0xFFF97316)
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
                    )
            )
        }
    }
}

@Composable
fun BasicInfoStep(
    title: String,
    onTitleChange: (String) -> Unit,
    propertyType: String,
    onPropertyTypeChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    area: String,
    onAreaChange: (String) -> Unit,
    bedrooms: String,
    onBedroomsChange: (String) -> Unit,
    bathrooms: String,
    onBathroomsChange: (String) -> Unit,
    size: String,
    onSizeChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        StepTitle(
            title = "Basic Information",
            subtitle = "Tell us about your property"
        )

        FormTextField(
            value = title,
            onValueChange = onTitleChange,
            label = "Property Title",
            placeholder = "e.g., Spacious 3BHK Apartment",
            leadingIcon = Icons.Default.Home
        )

        PropertyTypeSelector(
            selectedType = propertyType,
            onTypeSelected = onPropertyTypeChange
        )

        FormTextField(
            value = location,
            onValueChange = onLocationChange,
            label = "Location",
            placeholder = "e.g., Gulshan-2",
            leadingIcon = Icons.Default.LocationOn
        )

        FormTextField(
            value = area,
            onValueChange = onAreaChange,
            label = "Area/Neighborhood",
            placeholder = "e.g., Dhaka",
            leadingIcon = Icons.Default.Map
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = bedrooms,
                onValueChange = onBedroomsChange,
                label = "Bedrooms",
                placeholder = "3",
                leadingIcon = Icons.Default.KingBed,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )

            FormTextField(
                value = bathrooms,
                onValueChange = onBathroomsChange,
                label = "Bathrooms",
                placeholder = "2",
                leadingIcon = Icons.Default.Shower,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )
        }

        FormTextField(
            value = size,
            onValueChange = onSizeChange,
            label = "Size (sq ft)",
            placeholder = "1500",
            leadingIcon = Icons.Default.SquareFoot,
            keyboardType = KeyboardType.Number
        )
    }
}

@Composable
fun DetailsStep(
    floorNumber: String,
    onFloorNumberChange: (String) -> Unit,
    furnishingStatus: String,
    onFurnishingStatusChange: (String) -> Unit,
    availableFrom: String,
    onAvailableFromChange: (String) -> Unit,
    parkingAvailable: Boolean,
    onParkingChange: (Boolean) -> Unit,
    petFriendly: Boolean,
    onPetFriendlyChange: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        StepTitle(
            title = "Property Details",
            subtitle = "Add more specific information"
        )

        FormTextField(
            value = floorNumber,
            onValueChange = onFloorNumberChange,
            label = "Floor Number",
            placeholder = "e.g., 5th Floor",
            leadingIcon = Icons.Default.Layers
        )

        Text(
            text = "Furnishing Status",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151)
        )

        FurnishingSelector(
            selectedStatus = furnishingStatus,
            onStatusSelected = onFurnishingStatusChange
        )

        FormTextField(
            value = availableFrom,
            onValueChange = onAvailableFromChange,
            label = "Available From",
            placeholder = "e.g., Immediate / 1st June",
            leadingIcon = Icons.Default.CalendarToday
        )

        SwitchOption(
            label = "Parking Available",
            description = "Is parking space included?",
            checked = parkingAvailable,
            onCheckedChange = onParkingChange,
            icon = Icons.Default.LocalParking
        )

        SwitchOption(
            label = "Pet Friendly",
            description = "Are pets allowed in this property?",
            checked = petFriendly,
            onCheckedChange = onPetFriendlyChange,
            icon = Icons.Default.Pets
        )
    }
}

@Composable
fun PricingDescriptionStep(
    price: String,
    onPriceChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        StepTitle(
            title = "Pricing & Description",
            subtitle = "Set your price and describe your property"
        )

        FormTextField(
            value = price,
            onValueChange = onPriceChange,
            label = "Monthly Rent (৳)",
            placeholder = "45000",
            leadingIcon = Icons.Default.AttachMoney,
            keyboardType = KeyboardType.Number
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFEF2F2)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Price Breakdown",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }

                val priceInt = price.replace(",", "").toIntOrNull() ?: 0
                if (priceInt > 0) {
                    Divider(color = Color(0xFFE5E7EB))
                    PriceBreakdownItem("Security Deposit (2x)", "৳${"%,d".format(priceInt * 2)}")
                    PriceBreakdownItem("Service Charge", "৳3,000")
                    Divider(color = Color(0xFFE5E7EB))
                    PriceBreakdownItem(
                        "Total First Payment",
                        "৳${"%,d".format(priceInt * 2 + 3000)}",
                        isTotal = true
                    )
                }
            }
        }

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Property Description") },
            placeholder = { Text("Describe your property in detail...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFEF4444),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedLabelColor = Color(0xFFEF4444),
                cursorColor = Color(0xFFEF4444)
            ),
            maxLines = 8
        )

        Text(
            text = "${description.length}/500 characters",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF),
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun AmenitiesContactStep(
    selectedAmenities: Set<String>,
    onAmenitiesChange: (Set<String>) -> Unit,
    ownerName: String,
    onOwnerNameChange: (String) -> Unit,
    contactNumber: String,
    onContactNumberChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        StepTitle(
            title = "Amenities & Contact",
            subtitle = "Final details about your property"
        )

        Text(
            text = "Select Amenities",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151)
        )

        AmenitiesGrid(
            selectedAmenities = selectedAmenities,
            onAmenitiesChange = onAmenitiesChange
        )

        Divider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Owner Contact Information",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        FormTextField(
            value = ownerName,
            onValueChange = onOwnerNameChange,
            label = "Owner Name",
            placeholder = "John Doe",
            leadingIcon = Icons.Default.Person
        )

        FormTextField(
            value = contactNumber,
            onValueChange = onContactNumberChange,
            label = "Contact Number",
            placeholder = "+880 1XXX-XXXXXX",
            leadingIcon = Icons.Default.Phone,
            keyboardType = KeyboardType.Phone
        )
    }
}

@Composable
fun PropertyTypeSelector(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val types = listOf("Apartment", "House", "Studio", "Villa", "Commercial")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Property Type",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(types.size) { index ->
                val type = types[index]
                FilterChip(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) },
                    label = {
                        Text(
                            text = type,
                            fontSize = 14.sp,
                            fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEF4444),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color(0xFF6B7280)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedType == type,
                        borderColor = Color(0xFFE5E7EB),
                        selectedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
fun FurnishingSelector(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    val statuses = listOf("Fully Furnished", "Semi-Furnished", "Unfurnished")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        statuses.forEach { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        text = status,
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFEF4444),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF6B7280)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedStatus == status,
                    borderColor = Color(0xFFE5E7EB),
                    selectedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun AmenitiesGrid(
    selectedAmenities: Set<String>,
    onAmenitiesChange: (Set<String>) -> Unit
) {
    val amenitiesList = listOf(
        "Wi-Fi",
        "Air Conditioning",
        "Heating",
        "Gym",
        "Swimming Pool",
        "Elevator",
        "Security",
        "Balcony",
        "Garden",
        "Playground",
        "Generator",
        "CCTV"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        amenitiesList.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { amenity ->
                    AmenitySelectionChip(
                        text = amenity,
                        isSelected = selectedAmenities.contains(amenity),
                        onToggle = {
                            val newSet = selectedAmenities.toMutableSet()
                            if (newSet.contains(amenity)) {
                                newSet.remove(amenity)
                            } else {
                                newSet.add(amenity)
                            }
                            onAmenitiesChange(newSet)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun AmenitySelectionChip(
    text: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFEF4444).copy(alpha = 0.1f) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.5.dp,
            color = if (isSelected) Color(0xFFEF4444) else Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFFEF4444),
                    uncheckedColor = Color(0xFF9CA3AF)
                ),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                fontSize = 13.sp,
                color = if (isSelected) Color(0xFFEF4444) else Color(0xFF374151),
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun SwitchOption(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEF4444).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFEF4444),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE5E7EB)
                )
            )
        }
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF9CA3AF)
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFEF4444),
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedLabelColor = Color(0xFFEF4444),
            cursorColor = Color(0xFFEF4444),
            focusedLeadingIconColor = Color(0xFFEF4444)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

@Composable
fun StepTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
fun PriceBreakdownItem(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 14.sp else 13.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isTotal) Color(0xFF1F2937) else Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = if (isTotal) 15.sp else 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isTotal) Color(0xFFEF4444) else Color(0xFF374151)
        )
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
        delay(2000)
        onDismiss()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f)
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            icon = {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Success!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Your property has been posted successfully and is now visible to potential renters.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {}
        )
    }
}

fun isStepValid(
    step: Int,
    title: String,
    location: String,
    area: String,
    bedrooms: String,
    bathrooms: String,
    size: String,
    floorNumber: String,
    availableFrom: String,
    price: String,
    description: String,
    ownerName: String,
    contactNumber: String
): Boolean {
    return when (step) {
        1 -> title.isNotBlank() && location.isNotBlank() && area.isNotBlank() &&
                bedrooms.isNotBlank() && bathrooms.isNotBlank() && size.isNotBlank()
        2 -> floorNumber.isNotBlank() && availableFrom.isNotBlank()
        3 -> price.isNotBlank() && description.isNotBlank()
        4 -> ownerName.isNotBlank() && contactNumber.isNotBlank()
        else -> false
    }
}