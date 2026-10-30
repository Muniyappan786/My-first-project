package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.HeavyVehicle
import com.example.ui.theme.*

@Composable
fun VehicleCard(
    vehicle: HeavyVehicle,
    language: AppLanguage,
    onDetailClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .clickable { onDetailClick() }
            .testTag("vehicle_card_${vehicle.id}"),
        color = CardSurface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                VehicleImage(
                    assetPath = vehicle.imageAssetPath,
                    contentDescription = vehicle.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Availability Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (vehicle.isAvailable) StatusGreenBg.copy(alpha = 0.9f) else StatusRedBg.copy(alpha = 0.9f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (vehicle.isAvailable) StatusGreen else StatusRed)
                        )
                        Text(
                            text = if (vehicle.isAvailable) {
                                if (language == AppLanguage.TA) "இன்று கிடைக்கும்" else "Available"
                            } else {
                                if (language == AppLanguage.TA) "சைட்டில் உள்ளது" else "On Site"
                            },
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Verified Owner Badge
                if (vehicle.verifiedOwner) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Navy900.copy(alpha = 0.85f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = StatusGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Verified",
                                color = TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Rating Pill bottom-right
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Navy900.copy(alpha = 0.88f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = ConstructionYellow,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${vehicle.rating} (${vehicle.reviewCount})",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Vehicle Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Title
                Text(
                    text = if (language == AppLanguage.TA) vehicle.titleTa else vehicle.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location & Owner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = ConstructionYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${vehicle.taluk}, ${vehicle.locationDistrict}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = vehicle.ownerName.take(18),
                        color = TextMuted,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Inclusions Tags (Driver, Transport, Fuel)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (vehicle.driverIncluded) Navy700 else CardSurfaceVariant
                    ) {
                        Text(
                            text = if (vehicle.driverIncluded) "✓ Driver Included" else "Self-drive / Add Driver",
                            color = if (vehicle.driverIncluded) ConstructionYellowLight else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (vehicle.transportIncluded) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = StatusGreenBg
                        ) {
                            Text(
                                text = "✓ Free Transport",
                                color = StatusGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Divider(color = BorderSubtle, thickness = 0.8.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // Price and Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "₹${vehicle.dailyRate}",
                            color = ConstructionYellow,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "₹${vehicle.hourlyRate}/hr (min ${vehicle.minHours}h)",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Quick Call
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${vehicle.ownerPhone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Navy700)
                                .testTag("call_owner_${vehicle.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Call",
                                tint = StatusGreen,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Quick WhatsApp
                        IconButton(
                            onClick = {
                                val uri = Uri.parse("https://wa.me/${vehicle.ownerWhatsapp.replace(" ", "").replace("+", "")}?text=Hi,%20I%20am%20interested%20in%20renting%20your%20${vehicle.title}")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Navy700)
                                .testTag("whatsapp_owner_${vehicle.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp",
                                tint = ConstructionYellow,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Book Now
                        Button(
                            onClick = onBookClick,
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("book_button_${vehicle.id}"),
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.TA) "வாடகைக்கு எடு" else "Book",
                                color = Navy900,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
