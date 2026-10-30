package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.HeavyVehicle
import com.example.ui.theme.*

@Composable
fun VehicleDetailDialog(
    vehicle: HeavyVehicle,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onBookClick: () -> Unit
) {
    val context = LocalContext.current
    var activeImage by remember { mutableStateOf(vehicle.imageAssetPath) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp) // Leave room for status bar
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .testTag("vehicle_detail_dialog"),
            color = Navy900
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Action Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Navy700)
                            .testTag("close_detail_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }

                    Text(
                        text = if (language == AppLanguage.TA) "எந்திர விபரம்" else "Vehicle Details",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Check out ${vehicle.title} on HeavyLink! ₹${vehicle.dailyRate}/day at ${vehicle.taluk}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Vehicle"))
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Navy700)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = TextPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 20.dp)
                ) {
                    // Main Hero Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        VehicleImage(
                            assetPath = activeImage,
                            contentDescription = vehicle.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Availability chip overlay
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (vehicle.isAvailable) StatusGreenBg else StatusRedBg,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (vehicle.isAvailable) StatusGreen else StatusRed)
                                )
                                Text(
                                    text = if (vehicle.isAvailable) "Ready for Deployment" else "Currently On Site",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Gallery Thumbnails if more than 1 image
                    if (vehicle.imageGallery.size > 1) {
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(vehicle.imageGallery) { galleryImg ->
                                val isSelected = galleryImg == activeImage
                                Surface(
                                    modifier = Modifier
                                        .size(65.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) ConstructionYellow else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { activeImage = galleryImg },
                                    color = CardSurface
                                ) {
                                    VehicleImage(
                                        assetPath = galleryImg,
                                        contentDescription = "Gallery item",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    // Title & Location
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.TA) vehicle.titleTa else vehicle.title,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = ConstructionYellow, modifier = Modifier.size(16.dp))
                            Text(
                                text = "${vehicle.village}, ${vehicle.taluk}, ${vehicle.locationDistrict} District (Service radius: ${vehicle.serviceRadiusKm} km)",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Pricing Grid Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = CardSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("DAILY RENTAL", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("₹${vehicle.dailyRate}", color = ConstructionYellow, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    Text("Per 8-hour shift", color = TextSecondary, fontSize = 10.sp)
                                }

                                Divider(
                                    modifier = Modifier
                                        .height(45.dp)
                                        .width(1.dp),
                                    color = BorderSubtle
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("HOURLY RATE", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("₹${vehicle.hourlyRate}", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    Text("Min ${vehicle.minHours} hours", color = TextSecondary, fontSize = 10.sp)
                                }

                                Divider(
                                    modifier = Modifier
                                        .height(45.dp)
                                        .width(1.dp),
                                    color = BorderSubtle
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("OPERATOR", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(if (vehicle.driverIncluded) "Included" else "+₹1,000", color = StatusGreen, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                    Text("Certified master", color = TextSecondary, fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Machine Description
                        Text(
                            text = if (language == AppLanguage.TA) "இயந்திர பயன்பாட்டு குறிப்புகள்" else "Machine Overview & Description",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.TA) vehicle.descriptionTa else vehicle.description,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Technical Specifications
                        Text(
                            text = if (language == AppLanguage.TA) "தொழில்நுட்ப விபரங்கள்" else "Technical Specifications",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = CardSurface
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                vehicle.specs.entries.forEachIndexed { idx, entry ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(entry.key, color = TextSecondary, fontSize = 13.sp)
                                        Text(entry.value, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    if (idx < vehicle.specs.size - 1) {
                                        Divider(color = BorderSubtle, thickness = 0.5.dp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Worksite Suitability
                        Text(
                            text = if (language == AppLanguage.TA) "பொருத்தமான பணிகள்" else "Ideal Worksite Applications",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            vehicle.worksiteSuitability.forEach { work ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Navy700
                                ) {
                                    Text(
                                        text = "• $work",
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Owner Profile Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = CardSurfaceVariant
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
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Navy800),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Engineering, contentDescription = null, tint = ConstructionYellow)
                                    }
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(vehicle.ownerName, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = StatusGreen, modifier = Modifier.size(15.dp))
                                        }
                                        Text("${vehicle.ownerExperienceYears} years fleet owner • ${vehicle.locationDistrict}", color = TextSecondary, fontSize = 11.sp)
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${vehicle.ownerPhone}"))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Navy700)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = StatusGreen, modifier = Modifier.size(20.dp))
                                    }

                                    IconButton(
                                        onClick = {
                                            val uri = Uri.parse("https://wa.me/${vehicle.ownerWhatsapp.replace(" ", "").replace("+", "")}")
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Navy700)
                                    ) {
                                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = ConstructionYellow, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Sticky Bottom CTA Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Navy800,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "₹${vehicle.dailyRate} / day",
                                color = ConstructionYellow,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Min ${vehicle.minDays} day rental",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = onBookClick,
                            modifier = Modifier
                                .height(46.dp)
                                .testTag("detail_request_booking_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.TA) "முன்பதிவு செய்" else "Request Booking",
                                color = Navy900,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
