package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.EquipmentEntity
import com.example.data.local.UserEntity
import com.example.model.AppLanguage
import com.example.model.BookingRequest
import com.example.model.BookingStatus
import com.example.ui.components.VehicleImage
import com.example.ui.theme.*

@Composable
fun OwnerDashboardScreen(
    currentUser: UserEntity?,
    myEquipment: List<EquipmentEntity>,
    myBookings: List<BookingRequest>,
    language: AppLanguage,
    onAddVehicleClick: () -> Unit,
    onToggleAvailability: (Int, Boolean) -> Unit,
    onDeleteEquipment: (Int) -> Unit
) {
    val totalMachines = myEquipment.size
    val activeOnSite = myEquipment.count { !it.isAvailable }
    val totalRevenue = myBookings.sumOf { it.totalEstimatedPrice }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header with Real Registered User Information
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.TA) "எனது எந்திரங்கள்" else "My Equipment",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = StatusGreenBg
                        ) {
                            Text(
                                text = "Active Account",
                                color = StatusGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (currentUser != null) {
                            "${currentUser.fullName} • ${currentUser.taluk}, ${currentUser.district}"
                        } else {
                            "Registered Fleet Owner"
                        },
                        color = ConstructionYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (currentUser != null) {
                        Text(
                            text = currentUser.emailOrPhone,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Button(
                    onClick = onAddVehicleClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("owner_add_equipment_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.TA) "எந்திரம் சேர்" else "Add Equipment",
                        color = Navy900,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Live Real Statistics Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total Equipment", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$totalMachines", color = ConstructionYellow, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("Real listed machines", color = TextMuted, fontSize = 10.sp)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Active On Site", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$activeOnSite", color = StatusGreen, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("Currently working", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Rental Inquiries", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${myBookings.size}", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("From verified clients", color = TextMuted, fontSize = 10.sp)
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Earned Revenue", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹$totalRevenue", color = StatusGreen, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("Your fleet earnings", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.TA) "என் எந்திரங்கள் பட்டியல் ($totalMachines)" else "My Equipment List ($totalMachines)",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if (myEquipment.isNotEmpty()) {
                    Text(
                        text = "Real Database Records",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Clean Empty State if no equipment has been added by this user yet
        if (myEquipment.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .testTag("empty_my_equipment_card"),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Navy700),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PrecisionManufacturing,
                                contentDescription = null,
                                tint = ConstructionYellow,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (language == AppLanguage.TA) "எந்திரங்கள் எதுவும் சேர்க்கப்படவில்லை" else "No Equipment Added Yet",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.TA)
                                "உங்கள் 'My Equipment' பகுதி சுத்தமாகவும் காலியாகவும் உள்ளது. உங்கள் வாகனங்களை சந்தையில் பட்டியலிட 'Add Equipment' பட்டனை அழுத்தவும்."
                            else
                                "Your equipment section is clean and empty. Tap 'Add Equipment' below to list your heavy vehicles and start receiving genuine rental bookings.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onAddVehicleClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                            modifier = Modifier.testTag("empty_state_add_equipment_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.TA) "முதல் எந்திரத்தை சேர்க்கவும்" else "Add Your First Equipment",
                                color = Navy900,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Display Only Real Equipment Added by User
            items(myEquipment) { eq ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                        .testTag("my_equipment_item_${eq.id}"),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                color = Navy800
                            ) {
                                VehicleImage(
                                    assetPath = eq.imageAssetPath,
                                    contentDescription = eq.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = eq.title,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${eq.brand} • ${eq.model}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "₹${eq.dailyRate} / day • ₹${eq.hourlyRate} / hr",
                                    color = ConstructionYellow,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Location: ${eq.taluk}, ${eq.district}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = BorderSubtle, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Controls: Availability Toggle and Remove Machine
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (eq.isAvailable) StatusGreenBg else StatusRedBg
                                ) {
                                    Text(
                                        text = if (eq.isAvailable) "Available for Rent" else "Currently On Site",
                                        color = if (eq.isAvailable) StatusGreen else StatusRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                TextButton(
                                    onClick = { onToggleAvailability(eq.id, !eq.isAvailable) }
                                ) {
                                    Text(
                                        text = if (eq.isAvailable) "Mark Busy" else "Mark Available",
                                        color = ConstructionYellow,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onDeleteEquipment(eq.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete Equipment",
                                    tint = StatusRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Real Inquiries / Bookings for this Owner's Equipment
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (language == AppLanguage.TA) "முன்பதிவு கோரிக்கைகள்" else "Equipment Booking Inquiries",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (myBookings.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No booking inquiries yet.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "When contractors request to rent your equipment, their orders will appear here.",
                            color = TextMuted,
                            fontSize = 11.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(myBookings) { b ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(b.id, color = ConstructionYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(b.createdAt, color = TextMuted, fontSize = 11.sp)
                        }
                        Text(b.vehicleTitle, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Customer: ${b.customerName} (${b.customerPhone})", color = TextSecondary, fontSize = 12.sp)
                        Text("Site: ${b.worksiteAddress}", color = TextMuted, fontSize = 11.sp)
                        Text("Amount: ₹${b.totalEstimatedPrice}", color = StatusGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
