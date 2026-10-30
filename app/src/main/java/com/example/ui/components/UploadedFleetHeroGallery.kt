package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.HeavyVehicle
import com.example.ui.theme.*

@Composable
fun UploadedFleetHeroGallery(
    vehicles: List<HeavyVehicle>,
    language: AppLanguage,
    onVehicleClick: (HeavyVehicle) -> Unit,
    onBookClick: (HeavyVehicle) -> Unit
) {
    if (vehicles.isEmpty()) return

    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentVehicle = vehicles[selectedIndex % vehicles.size]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(StatusGreen)
                )
                Text(
                    text = if (language == AppLanguage.TA) "பிரத்யேக கனரக எந்திரங்கள்" else "FEATURED FLEET GALLERY",
                    color = ConstructionYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Navy700
            ) {
                Text(
                    text = "${selectedIndex + 1} / ${vehicles.size}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Featured Machine Showcase Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
                .clickable { onVehicleClick(currentVehicle) }
                .testTag("hero_fleet_card"),
            color = CardSurface
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // High Quality Machine Image
                VehicleImage(
                    assetPath = currentVehicle.imageAssetPath,
                    contentDescription = currentVehicle.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark vignette gradient for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.88f)
                                )
                            )
                        )
                )

                // Top Floating Chips: Brand, Status & Verified
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Navy900.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = StatusGreen,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Verified Owner",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (currentVehicle.isAvailable) StatusGreenBg.copy(alpha = 0.9f) else StatusRedBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (currentVehicle.isAvailable) StatusGreen else StatusRed)
                            )
                            Text(
                                text = if (currentVehicle.isAvailable) "Available Today" else "On Site",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Bottom Overlay: Title, Location, Price, Quick Action
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.TA) currentVehicle.titleTa else currentVehicle.title,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = ConstructionYellow,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${currentVehicle.taluk}, ${currentVehicle.locationDistrict}",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Rate tag
                        Text(
                            text = "₹${currentVehicle.dailyRate} / day",
                            color = ConstructionYellow,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onVehicleClick(currentVehicle) },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("hero_view_specs_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Full Specs", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onBookClick(currentVehicle) },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("hero_book_now_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = Navy900)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Book Now", color = Navy900, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Thumbnail Strip of all 8 uploaded images
        Text(
            text = if (language == AppLanguage.TA) "அனைத்து எந்திரங்களை காண்க (8 வாகனங்கள்)" else "Tap any machine to inspect & rent:",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            itemsIndexed(vehicles) { index, vehicle ->
                val isSelected = index == selectedIndex
                Surface(
                    modifier = Modifier
                        .width(110.dp)
                        .height(85.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) ConstructionYellow else BorderSubtle,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedIndex = index }
                        .testTag("hero_thumbnail_$index"),
                    color = CardSurface
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        VehicleImage(
                            assetPath = vehicle.imageAssetPath,
                            contentDescription = vehicle.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Bottom label
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Navy900.copy(alpha = 0.85f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = vehicle.brand + " " + vehicle.model.take(8),
                                color = if (isSelected) ConstructionYellow else TextPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
