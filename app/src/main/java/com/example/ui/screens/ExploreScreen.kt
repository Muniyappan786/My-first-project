package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FleetRepository
import com.example.model.AppLanguage
import com.example.model.HeavyVehicle
import com.example.model.VehicleCategory
import com.example.ui.components.VehicleCard
import com.example.ui.theme.*

@Composable
fun ExploreScreen(
    vehicles: List<HeavyVehicle>,
    categories: List<VehicleCategory>,
    districts: List<String>,
    language: AppLanguage,
    searchQuery: String,
    selectedDistrict: String,
    selectedTaluk: String,
    selectedCategoryId: String,
    driverIncludedOnly: Boolean,
    fuelIncludedOnly: Boolean,
    availableOnly: Boolean,
    onSearchChange: (String) -> Unit,
    onDistrictChange: (String) -> Unit,
    onTalukChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onToggleDriver: () -> Unit,
    onToggleFuel: () -> Unit,
    onToggleAvailable: () -> Unit,
    onVehicleClick: (HeavyVehicle) -> Unit,
    onBookClick: (HeavyVehicle) -> Unit,
    onPostRequirementClick: () -> Unit
) {
    var showDistrictMenu by remember { mutableStateOf(false) }
    var showTalukMenu by remember { mutableStateOf(false) }
    val taluks = remember(selectedDistrict) { FleetRepository.getTaluksForDistrict(selectedDistrict) }

    // Live filtering
    val filteredVehicles = remember(
        vehicles, searchQuery, selectedDistrict, selectedTaluk, selectedCategoryId,
        driverIncludedOnly, fuelIncludedOnly, availableOnly
    ) {
        vehicles.filter { v ->
            val matchesQuery = searchQuery.isBlank() ||
                v.title.contains(searchQuery, ignoreCase = true) ||
                v.brand.contains(searchQuery, ignoreCase = true) ||
                v.model.contains(searchQuery, ignoreCase = true) ||
                v.village.contains(searchQuery, ignoreCase = true)

            val matchesDistrict = selectedDistrict == "All Districts" ||
                v.locationDistrict.equals(selectedDistrict, ignoreCase = true)

            val matchesTaluk = selectedTaluk == "All Taluks" ||
                v.taluk.equals(selectedTaluk, ignoreCase = true)

            val matchesCategory = selectedCategoryId == "all" ||
                v.categoryId == selectedCategoryId

            val matchesDriver = !driverIncludedOnly || v.driverIncluded
            val matchesFuel = !fuelIncludedOnly || v.fuelIncluded
            val matchesAvailable = !availableOnly || v.isAvailable

            matchesQuery && matchesDistrict && matchesTaluk && matchesCategory &&
                matchesDriver && matchesFuel && matchesAvailable
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Search & Location Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.TA) "எந்திர சந்தை தேடல்" else "Find Equipment & Compare Rates",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_input"),
                    placeholder = { Text("Search by model, brand, or village...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = ConstructionYellow)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConstructionYellow,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // District & Taluk Selector Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // District dropdown
                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { showDistrictMenu = true },
                            color = CardSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(selectedDistrict, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ConstructionYellow)
                            }
                        }

                        DropdownMenu(
                            expanded = showDistrictMenu,
                            onDismissRequest = { showDistrictMenu = false },
                            modifier = Modifier.background(Navy800)
                        ) {
                            (listOf("All Districts") + districts).forEach { dist ->
                                DropdownMenuItem(
                                    text = { Text(dist, color = TextPrimary) },
                                    onClick = {
                                        onDistrictChange(dist)
                                        showDistrictMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Taluk dropdown
                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { showTalukMenu = true },
                            color = CardSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(selectedTaluk, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ConstructionYellow)
                            }
                        }

                        DropdownMenu(
                            expanded = showTalukMenu,
                            onDismissRequest = { showTalukMenu = false },
                            modifier = Modifier.background(Navy800)
                        ) {
                            taluks.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t, color = TextPrimary) },
                                    onClick = {
                                        onTalukChange(t)
                                        showTalukMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Chips Row (Driver included, Fuel included, Available today)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = driverIncludedOnly,
                        onClick = onToggleDriver,
                        label = { Text("Driver Included", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ConstructionYellow,
                            selectedLabelColor = Navy900
                        )
                    )

                    FilterChip(
                        selected = fuelIncludedOnly,
                        onClick = onToggleFuel,
                        label = { Text("Fuel Included", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ConstructionYellow,
                            selectedLabelColor = Navy900
                        )
                    )

                    FilterChip(
                        selected = availableOnly,
                        onClick = onToggleAvailable,
                        label = { Text("Available Now", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StatusGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Category Pills
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    val isAll = selectedCategoryId == "all"
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (isAll) ConstructionYellow else BorderSubtle, RoundedCornerShape(20.dp))
                            .clickable { onCategoryChange("all") },
                        color = if (isAll) ConstructionYellow else CardSurface
                    ) {
                        Text(
                            text = "All Equipment",
                            color = if (isAll) Navy900 else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                items(categories) { cat ->
                    val isSelected = selectedCategoryId == cat.id
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, if (isSelected) ConstructionYellow else BorderSubtle, RoundedCornerShape(20.dp))
                            .clickable { onCategoryChange(cat.id) },
                        color = if (isSelected) ConstructionYellow else CardSurface
                    ) {
                        Text(
                            text = if (language == AppLanguage.TA) cat.nameTa else cat.nameEn,
                            color = if (isSelected) Navy900 else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Matching Vehicles (${filteredVehicles.size})",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sorted by Nearest",
                    color = ConstructionYellow,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Empty state handling
        if (filteredVehicles.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = ConstructionYellow,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matching machines in this taluk right now.",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing filters, selecting 'All Taluks', or post your requirement to broadcast to nearby district owners.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onPostRequirementClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Post a Requirement", color = Navy900, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        } else {
            items(filteredVehicles) { vehicle ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    VehicleCard(
                        vehicle = vehicle,
                        language = language,
                        onDetailClick = { onVehicleClick(vehicle) },
                        onBookClick = { onBookClick(vehicle) }
                    )
                }
            }
        }
    }
}
