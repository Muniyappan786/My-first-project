package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.HeavyVehicle
import com.example.model.VehicleCategory
import com.example.ui.components.UploadedFleetHeroGallery
import com.example.ui.components.VehicleCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    vehicles: List<HeavyVehicle>,
    categories: List<VehicleCategory>,
    language: AppLanguage,
    onVehicleClick: (HeavyVehicle) -> Unit,
    onBookClick: (HeavyVehicle) -> Unit,
    onNavigateToAiFinder: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onPostRequirementClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900),
        contentPadding = PaddingValues(bottom = 90.dp) // Room for bottom nav bar
    ) {
        // Hero Header Title
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Navy800
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = ConstructionYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Serving Villupuram • Gingee • Tindivanam • All Tamil Nadu",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (language == AppLanguage.TA) "உங்கள் அருகிலுள்ள கனரக எந்திரங்களை கண்டறியவும்" else "Find Heavy Vehicles Near You",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (language == AppLanguage.TA)
                        "ஹிட்டாச்சி, ஜேசிபி, டிராக்டர், டிரெய்லர் மற்றும் கட்டுமான எந்திரங்களை உள்ளூர் உரிமையாளர்களிடம் இருந்து நேரடியாக முன்பதிவு செய்யுங்கள்."
                    else
                        "Book Hitachi, JCB, trailers, tippers, and construction equipment from verified local vehicle owners.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // Prominent Uploaded Fleet Showcase Gallery (All 8 images)
        item {
            UploadedFleetHeroGallery(
                vehicles = vehicles,
                language = language,
                onVehicleClick = onVehicleClick,
                onBookClick = onBookClick
            )
        }

        // Quick AI Search Bar Action
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, ConstructionYellow, RoundedCornerShape(14.dp))
                    .clickable { onNavigateToAiFinder() }
                    .testTag("ai_finder_hero_banner"),
                color = CardSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ConstructionYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = Navy900,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (language == AppLanguage.TA) "AI மூலம் எந்திரம் தேடுங்கள்" else "Find a Machine Using AI",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == AppLanguage.TA) "தமிழ் அல்லது ஆங்கிலத்தில் டைப் செய்து உடனடியாக பெறுங்கள்" else "Type in Tamil or English e.g. 'Gingee Hitachi 2 days'",
                                color = ConstructionYellow,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Go",
                        tint = ConstructionYellow
                    )
                }
            }
        }

        // Category Horizontal Carousel
        item {
            Column(modifier = Modifier.padding(top = 14.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.TA) "எந்திர வகைகள்" else "Equipment Categories",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "See All",
                        color = ConstructionYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToExplore() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(categories) { cat ->
                        Surface(
                            modifier = Modifier
                                .width(135.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { onNavigateToExplore() },
                            color = CardSurface
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Navy700),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (cat.id) {
                                            "hitachi" -> Icons.Default.PrecisionManufacturing
                                            "jcb" -> Icons.Default.Construction
                                            "trailer" -> Icons.Default.LocalShipping
                                            "tipper" -> Icons.Default.FireTruck
                                            "crane" -> Icons.Default.Build
                                            else -> Icons.Default.Agriculture
                                        },
                                        contentDescription = cat.nameEn,
                                        tint = ConstructionYellow,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (language == AppLanguage.TA) cat.nameTa else cat.nameEn,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )

                                Text(
                                    text = "${vehicles.count { it.categoryId == cat.id }} Available",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Real Marketplace Stats Counter
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                shape = RoundedCornerShape(14.dp),
                color = CardSurfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${vehicles.size}", color = ConstructionYellow, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text("Equipment Listed", color = TextSecondary, fontSize = 11.sp)
                    }
                    Divider(modifier = Modifier.height(35.dp).width(1.dp), color = BorderSubtle)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val verifiedOwnersCount = vehicles.map { it.ownerPhone }.distinct().size
                        Text("$verifiedOwnersCount", color = StatusGreen, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text("Verified Owners", color = TextSecondary, fontSize = 11.sp)
                    }
                    Divider(modifier = Modifier.height(35.dp).width(1.dp), color = BorderSubtle)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("0%", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text("Broker Fee", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Available Machines Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.TA) "இன்று கிடைக்கும் எந்திரங்கள்" else "Available Today in Tamil Nadu",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${vehicles.size} Machines",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // List of Available Heavy Equipment Cards or Empty State
        if (vehicles.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .testTag("home_empty_equipment_card"),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Navy700),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Construction,
                                contentDescription = null,
                                tint = ConstructionYellow,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (language == AppLanguage.TA) "எந்திரங்கள் எதுவும் பட்டியலிடப்படவில்லை" else "No Equipment Listed Yet",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.TA)
                                "அனைத்து மாதிரி தகவல்களும் நீக்கப்பட்டுவிட்டன. பதிவு செய்த வாகன உரிமையாளர்கள் தங்கள் எந்திரங்களை பதிவேற்றும் போது இங்கு காண்பிக்கப்படும்."
                            else
                                "All mock equipment has been removed. When registered fleet owners add machines to their fleet, they will appear here live.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        } else {
            items(vehicles) { vehicle ->
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

        // Post a Requirement Call to Action
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Navy800
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.TA) "தேவையான எந்திரம் கிடைக்கவில்லையா?" else "Need a Specific Machine or Urgent Work?",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.TA)
                            "உங்கள் தேவை மற்றும் பட்ஜெட்டை பதிவிடுங்கள். அருகிலுள்ள எந்திர உரிமையாளர்கள் உங்களை தொடர்புகொள்வார்கள்."
                        else
                            "Post your worksite requirement, location, and budget. Local registered fleet owners will send competitive offers directly.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onPostRequirementClick,
                        modifier = Modifier.fillMaxWidth().testTag("home_post_requirement_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PostAdd, contentDescription = null, tint = Navy900)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.TA) "தேவையை பதிவிடவும்" else "Post a Requirement Now",
                            color = Navy900,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
