package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.model.AppLanguage
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DriversHubScreen(
    registeredOperators: List<UserEntity>,
    currentUser: UserEntity?,
    language: AppLanguage,
    onRegisterOperatorClick: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("drivers_hub_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.TA) "சான்றளிக்கப்பட்ட எந்திர ஓட்டுநர்கள்" else "Certified Equipment Operators",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (language == AppLanguage.TA) "HeavyLink-ல் பதிவுசெய்த உண்மையான ஓட்டுநர்கள் மட்டுமே" else "Verified registered machinery drivers & equipment operators",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Navy700
                ) {
                    Text(
                        text = "${registeredOperators.size} Registered",
                        color = ConstructionYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        if (registeredOperators.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .testTag("empty_operators_card"),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
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
                                imageVector = Icons.Default.Engineering,
                                contentDescription = null,
                                tint = ConstructionYellow,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (language == AppLanguage.TA) "பதிவு செய்த ஓட்டுநர்கள் இல்லை" else "No Registered Operators Yet",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (language == AppLanguage.TA)
                                "பழைய மாதிரி தரவுகள் அனைத்தும் நீக்கப்பட்டு விட்டன. தளத்தில் பதிவு செய்யும் உண்மையான ஓட்டுநர்கள் மட்டுமே இங்கு காட்டப்படுவார்கள்."
                            else
                                "All dummy operator data has been cleared. Only genuine operators who register on HeavyLink appear here.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Navy900,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(20.dp))
                                Text(
                                    text = if (language == AppLanguage.TA) "Excavator, Crane, Tipper ஓட்டுநரா? இன்றே இலவசமாக பதிவு செய்யுங்கள்!" else "Heavy machinery driver? Register with your location to get daily hire requests.",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onRegisterOperatorClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("register_operator_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.TA) "ஓட்டுநராக பதிவு செய்க" else "Register as an Operator",
                                color = Navy900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        } else {
            items(registeredOperators) { op ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                        .testTag("operator_card_${op.id}"),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Navy700),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Engineering,
                                        contentDescription = null,
                                        tint = ConstructionYellow,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            op.fullName,
                                            color = TextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            Icons.Default.Verified,
                                            contentDescription = "Verified Operator",
                                            tint = StatusGreen,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Text(
                                        "${op.taluk}, ${op.district}",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Navy700
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    color = StatusGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(shape = RoundedCornerShape(4.dp), color = Navy700) {
                                Text(
                                    "Heavy Machinery Certified",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Surface(shape = RoundedCornerShape(4.dp), color = Navy700) {
                                Text(
                                    "Tamil Nadu Region",
                                    color = ConstructionYellow,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = BorderSubtle, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Contact: ${op.emailOrPhone}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                val joinedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(op.createdAt))
                                Text(
                                    "Registered: $joinedDate",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${op.emailOrPhone}"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("call_operator_${op.id}"),
                                contentPadding = PaddingValues(horizontal = 14.dp)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Navy900, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Operator", color = Navy900, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
