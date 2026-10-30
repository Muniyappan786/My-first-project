package com.example.ui.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.ui.theme.*

@Composable
fun AddVehicleDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        category: String,
        brand: String,
        model: String,
        dailyRate: Int,
        hourlyRate: Int,
        district: String,
        taluk: String,
        village: String,
        ownerName: String,
        phone: String,
        driverIncluded: Boolean,
        fuelIncluded: Boolean,
        description: String,
        selectedImage: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("hitachi") }
    var brand by remember { mutableStateOf("Tata Hitachi") }
    var model by remember { mutableStateOf("ZX210") }
    var dailyRateStr by remember { mutableStateOf("16000") }
    var hourlyRateStr by remember { mutableStateOf("2000") }
    var district by remember { mutableStateOf("Villupuram") }
    var taluk by remember { mutableStateOf("Gingee") }
    var village by remember { mutableStateOf("Town Area") }
    var ownerName by remember { mutableStateOf("My Heavy Earthworks") }
    var phone by remember { mutableStateOf("+91 98400 12345") }
    var driverIncluded by remember { mutableStateOf(true) }
    var fuelIncluded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("Well maintained machine with certified operator available for contract.") }

    val availableImages = listOf(
        "zx-890-lch-mob-banner.jpg_3a1b2034.jpeg",
        "1915113_1001153-200600_1eded621.webp",
        "desi-machines-tata-hitachi-excavator-nx30-featured.jpg_66f5b2f7.jpeg",
        "3-Axle-lowbed-Semi-Trailer_5923408c.webp",
        "hitachi_2336c9fc.webp",
        "hitachi2_1bb07280.jpeg",
        "hitachi3_2747a010.jpeg",
        "scania-r560-truck-and-sany-sy390h-large-excavator-loaded-on-lowboy-semi-trailer-on-asphalt-yard-salo-finland-july-28-2022-2MXKNFK.jpg_7365a6df.jpeg"
    )
    var selectedImage by remember { mutableStateOf(availableImages.first()) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .testTag("add_vehicle_dialog"),
            color = Navy900
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp).clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Navy700)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }

                    Text(
                        text = if (language == AppLanguage.TA) "புதிய எந்திரத்தை சேர்க்க" else "List New Vehicle / Machine",
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
                    Text(
                        text = "Select Machine Photo from Fleet Library:",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableImages) { img ->
                            val isChosen = img == selectedImage
                            Surface(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        width = if (isChosen) 2.5.dp else 1.dp,
                                        color = if (isChosen) ConstructionYellow else BorderSubtle,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedImage = img },
                                color = CardSurface
                            ) {
                                VehicleImage(
                                    assetPath = img,
                                    contentDescription = "Select photo",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Machine Title") },
                        placeholder = { Text("e.g. Tata Hitachi 210 Heavy Excavator") },
                        modifier = Modifier.fillMaxWidth().testTag("add_vehicle_title_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = brand,
                            onValueChange = { brand = it },
                            label = { Text("Brand") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = model,
                            onValueChange = { model = it },
                            label = { Text("Model") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = dailyRateStr,
                            onValueChange = { dailyRateStr = it },
                            label = { Text("Daily Rate (₹)") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = hourlyRateStr,
                            onValueChange = { hourlyRateStr = it },
                            label = { Text("Hourly Rate (₹)") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("District") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = taluk,
                            onValueChange = { taluk = it },
                            label = { Text("Taluk") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Driver / Operator Included in Rate", color = TextPrimary, fontSize = 13.sp)
                        Switch(
                            checked = driverIncluded,
                            onCheckedChange = { driverIncluded = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = ConstructionYellow)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Fuel / Diesel Included in Rate", color = TextPrimary, fontSize = 13.sp)
                        Switch(
                            checked = fuelIncluded,
                            onCheckedChange = { fuelIncluded = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = ConstructionYellow)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Equipment Description & Working Condition") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        maxLines = 3
                    )

                    if (errorMsg != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMsg!!, color = StatusRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Navy800
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    errorMsg = "Please enter machine title."
                                    return@Button
                                }
                                val dRate = dailyRateStr.toIntOrNull() ?: 15000
                                val hRate = hourlyRateStr.toIntOrNull() ?: 1800
                                errorMsg = null
                                onSubmit(
                                    title,
                                    category,
                                    brand,
                                    model,
                                    dRate,
                                    hRate,
                                    district,
                                    taluk,
                                    village,
                                    ownerName,
                                    phone,
                                    driverIncluded,
                                    fuelIncluded,
                                    description,
                                    selectedImage
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("submit_add_vehicle_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Publish Machine to Marketplace", color = Navy900, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}
