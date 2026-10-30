package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun BookingFlowDialog(
    vehicle: HeavyVehicle,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        phone: String,
        village: String,
        worksite: String,
        startDate: String,
        durationDays: Int,
        estimatedHours: Int,
        notes: String
    ) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf(vehicle.village) }
    var worksiteAddress by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("2026-11-01") }
    var durationDays by remember { mutableIntStateOf(2) }
    var estimatedHours by remember { mutableIntStateOf(16) }
    var customerNotes by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("UPI (GPay / PhonePe)") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Live calculation
    val baseRent = vehicle.dailyRate * durationDays
    val driverCost = if (vehicle.driverIncluded) 0 else 1000 * durationDays
    val transportCost = if (vehicle.transportIncluded) 0 else 3000
    val totalAmount = baseRent + driverCost + transportCost
    val advanceToken = (totalAmount * 0.20).toInt()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .testTag("booking_flow_dialog"),
            color = Navy900
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Navy700)
                            .testTag("close_booking_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }

                    Text(
                        text = if (language == AppLanguage.TA) "முன்பதிவு படிவம்" else "Reserve Equipment",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.size(36.dp))
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Machine Summary Banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Navy800
                            ) {
                                VehicleImage(
                                    assetPath = vehicle.imageAssetPath,
                                    contentDescription = vehicle.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = vehicle.title,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Base rate: ₹${vehicle.dailyRate}/day (${vehicle.taluk})",
                                    color = ConstructionYellow,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Owner: ${vehicle.ownerName}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (language == AppLanguage.TA) "வாடிக்கையாளர் தகவல்கள்" else "1. Contact & Worksite Location",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Your Full Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Mobile Number (+91)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("customer_phone_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = worksiteAddress,
                        onValueChange = { worksiteAddress = it },
                        label = { Text("Exact Worksite Location / Survey No.") },
                        placeholder = { Text("e.g. Survey No. 42, Melmalayanur Road") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("worksite_address_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (language == AppLanguage.TA) "பணி நாட்கள் & காலம்" else "2. Dates & Rental Duration",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { startDate = it },
                            label = { Text("Start Date") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        // Duration selector (days)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .border(1.dp, BorderSubtle, RoundedCornerShape(4.dp)),
                            color = Navy900
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { if (durationDays > 1) durationDays-- },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = TextPrimary)
                                }
                                Text("$durationDays Days", color = TextPrimary, fontWeight = FontWeight.Bold)
                                IconButton(
                                    onClick = { durationDays++ },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = ConstructionYellow)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customerNotes,
                        onValueChange = { customerNotes = it },
                        label = { Text("Special requirements or soil conditions") },
                        placeholder = { Text("e.g. Clay soil pond digging, need experienced operator") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Transparent Price Breakdown
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CardSurface
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Transparent Price Calculation",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Machine Rental ($durationDays days × ₹${vehicle.dailyRate})", color = TextSecondary, fontSize = 12.sp)
                                Text("₹$baseRent", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Certified Operator", color = TextSecondary, fontSize = 12.sp)
                                Text(if (vehicle.driverIncluded) "FREE (Included)" else "₹$driverCost", color = if (vehicle.driverIncluded) StatusGreen else TextPrimary, fontSize = 12.sp)
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Lowbed Trailer Delivery", color = TextSecondary, fontSize = 12.sp)
                                Text(if (vehicle.transportIncluded) "FREE (Included)" else "₹$transportCost", color = if (vehicle.transportIncluded) StatusGreen else TextPrimary, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = BorderSubtle)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Estimated Amount", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("₹$totalAmount", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Advance Token to Reserve (20%)", color = ConstructionYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("₹$advanceToken", color = ConstructionYellow, fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment Method Options
                    Text(
                        text = "3. Payment Integration (Test Mode Supported)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    listOf("UPI (GPay / PhonePe / Paytm)", "Net Banking / NEFT", "Pay Balance on Delivery").forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedPaymentMethod == method) Navy700 else CardSurface)
                                .clickable { selectedPaymentMethod = method }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedPaymentMethod == method),
                                onClick = { selectedPaymentMethod = method },
                                colors = RadioButtonDefaults.colors(selectedColor = ConstructionYellow)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(method, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMessage!!, color = StatusRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Bottom Action Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Navy800
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Pay Advance Token", color = TextSecondary, fontSize = 11.sp)
                            Text("₹$advanceToken", color = ConstructionYellow, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        }

                        Button(
                            onClick = {
                                if (customerName.isBlank()) {
                                    errorMessage = "Please enter your full name."
                                    return@Button
                                }
                                if (customerPhone.isBlank() || customerPhone.length < 8) {
                                    errorMessage = "Please enter a valid phone number."
                                    return@Button
                                }
                                errorMessage = null
                                onSubmit(
                                    customerName,
                                    customerPhone,
                                    village,
                                    worksiteAddress,
                                    startDate,
                                    durationDays,
                                    estimatedHours,
                                    customerNotes
                                )
                            },
                            modifier = Modifier
                                .height(46.dp)
                                .testTag("submit_booking_payment_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Confirm & Reserve",
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
