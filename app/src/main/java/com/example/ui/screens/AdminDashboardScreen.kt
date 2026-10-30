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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.model.AppLanguage
import com.example.model.BookingRequest
import com.example.model.CustomerRequirement
import com.example.model.HeavyVehicle
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    vehicles: List<HeavyVehicle>,
    requirements: List<CustomerRequirement>,
    users: List<UserEntity>,
    bookings: List<BookingRequest>,
    language: AppLanguage
) {
    val totalRevenue = bookings.sumOf { it.totalEstimatedPrice }
    val registeredOwners = remember(users) {
        users.filter { it.role.equals("OWNER", ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = StatusRed, modifier = Modifier.size(22.dp))
                        Text("HeavyLink Admin Console", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }
                    Text("Statewide Fleet Governance • Tamil Nadu Region", color = TextSecondary, fontSize = 12.sp)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StatusGreenBg
                ) {
                    Text("Live System", color = StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
        }

        // 4 Real Database Statistics Cards (Initial values are 0 for fresh app)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = CardSurface) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Active Listings", color = TextSecondary, fontSize = 11.sp)
                            Text("${vehicles.size}", color = ConstructionYellow, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("Real listed machines", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = CardSurface) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Registered Users", color = TextSecondary, fontSize = 11.sp)
                            Text("${users.size}", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("${registeredOwners.size} Fleet Owners", color = StatusGreen, fontSize = 10.sp)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = CardSurface) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Customer Requirements", color = TextSecondary, fontSize = 11.sp)
                            Text("${requirements.size}", color = ConstructionYellow, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("Broadcasted requests", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), color = CardSurface) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Platform GMV", color = TextSecondary, fontSize = 11.sp)
                            Text("₹$totalRevenue", color = StatusGreen, fontSize = 20.sp, fontWeight = FontWeight.Black)
                            Text("${bookings.size} Confirmed bookings", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        // Section: Verification Queue of Registered Fleet Owners
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Fleet Owner Directory", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("${registeredOwners.size} Total", color = TextSecondary, fontSize = 12.sp)
            }
        }

        if (registeredOwners.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Engineering, contentDescription = null, tint = TextMuted, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No Fleet Owners Registered Yet", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("When equipment owners sign up, their accounts will appear here.", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        } else {
            items(registeredOwners) { owner ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(owner.fullName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${owner.taluk}, ${owner.district} • Contact: ${owner.emailOrPhone}", color = TextSecondary, fontSize = 12.sp)
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StatusGreenBg
                            ) {
                                Text("Verified", color = StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }

        // Section: Active Customer Requirements to Moderate
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Broadcast Customer Requirements", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("${requirements.size} Total", color = TextSecondary, fontSize = 12.sp)
            }
        }

        if (requirements.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.PostAdd, contentDescription = null, tint = TextMuted, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No Customer Requirements Posted Yet", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Customer worksite requirements will appear here for administrative moderation.", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        } else {
            items(requirements) { req ->
                Surface(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(req.id, color = ConstructionYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(req.createdAt, color = TextMuted, fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${req.vehicleCategory} - ${req.workType}", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Customer: ${req.customerName} • ${req.taluk}, ${req.district}", color = TextSecondary, fontSize = 12.sp)
                        Text("Budget: ${req.budget} • Duration: ${req.duration}", color = ConstructionYellowLight, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Notes: ${req.notes}", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
