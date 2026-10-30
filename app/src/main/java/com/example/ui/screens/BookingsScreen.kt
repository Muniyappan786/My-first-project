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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.model.AppLanguage
import com.example.model.BookingRequest
import com.example.model.BookingStatus
import com.example.ui.components.VehicleImage
import com.example.ui.theme.*

@Composable
fun BookingsScreen(
    bookings: List<BookingRequest>,
    currentUser: UserEntity?,
    language: AppLanguage,
    onViewReceipt: (BookingRequest) -> Unit,
    onExploreClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current

    // Only display real bookings made by the currently registered / logged-in user
    val myBookings = remember(bookings, currentUser) {
        if (currentUser == null) {
            emptyList()
        } else {
            bookings.filter { b ->
                b.customerPhone.equals(currentUser.emailOrPhone, ignoreCase = true) ||
                b.customerName.equals(currentUser.fullName, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("bookings_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.TA) "எனது முன்பதிவுகள்" else "My Equipment Bookings",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (currentUser != null) {
                            "Logged in as ${currentUser.fullName} (${currentUser.emailOrPhone})"
                        } else {
                            "Real registered user machine bookings"
                        },
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Navy700
                ) {
                    Text(
                        text = "${myBookings.size} Active",
                        color = ConstructionYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        if (currentUser == null) {
            // User is not logged in: prompt login to view their actual bookings
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .testTag("bookings_login_required_card"),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Navy700),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = ConstructionYellow, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (language == AppLanguage.TA) "முன்பதிவுகளைப் பார்க்க உள்நுழையவும்" else "Log In to View Your Bookings",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.TA)
                                "மாதிரி தரவுகள் அனைத்தும் நீக்கப்பட்டு விட்டன. உங்கள் கணக்கில் பதிவுசெய்யப்பட்ட உண்மையான முன்பதிவுகள் மட்டுமே இங்கு காட்டப்படும்."
                            else
                                "All sample and dummy booking records have been removed. Log in with your registered HeavyLink account to view your bookings.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onLoginClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("bookings_login_button")
                        ) {
                            Icon(Icons.Default.Login, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.TA) "உள்நுழைக / பதிவு செய்க" else "Log In / Register",
                                color = Navy900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        } else if (myBookings.isEmpty()) {
            // User is logged in, but has 0 real bookings yet
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .testTag("empty_bookings_card"),
                    color = CardSurface
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Navy700),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EventBusy, contentDescription = null, tint = ConstructionYellow, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (language == AppLanguage.TA) "முன்பதிவுகள் எதுவும் இல்லை" else "No Equipment Bookings Yet",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.TA)
                                "நீங்கள் இன்னும் எந்த இயந்திரத்தையும் முன்பதிவு செய்யவில்லை. தேவையான எந்திரத்தை தேர்வு செய்து முன்பதிவு செய்யுங்கள்."
                            else
                                "You haven't booked any machines yet. Browse verified excavators, trailers, and cranes in your taluk.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onExploreClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("browse_fleet_button")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.TA) "எந்திரங்களை ஆராயுங்கள்" else "Browse Heavy Equipment",
                                color = Navy900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Real bookings created by the logged in user
            items(myBookings) { b ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                        .testTag("booking_card_${b.id}"),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Header with ID & Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = b.id,
                                color = ConstructionYellow,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when (b.bookingStatus) {
                                    BookingStatus.CONFIRMED -> StatusGreenBg
                                    BookingStatus.WORK_STARTED -> StatusGreenBg
                                    BookingStatus.COMPLETED -> Navy700
                                    BookingStatus.CANCELLED -> StatusRedBg
                                    else -> Navy700
                                }
                            ) {
                                Text(
                                    text = if (language == AppLanguage.TA) b.bookingStatus.displayNameTa else b.bookingStatus.displayNameEn,
                                    color = when (b.bookingStatus) {
                                        BookingStatus.CONFIRMED, BookingStatus.WORK_STARTED -> StatusGreen
                                        BookingStatus.CANCELLED -> StatusRed
                                        else -> ConstructionYellowLight
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Machine Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Navy800
                            ) {
                                VehicleImage(
                                    assetPath = b.vehicleImagePath,
                                    contentDescription = b.vehicleTitle,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = b.vehicleTitle,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Worksite: ${b.worksiteAddress}",
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Start: ${b.startDate} • Duration: ${b.durationDays} day(s)",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = BorderSubtle, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Price & Financials
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total Estimated: ₹${b.totalEstimatedPrice}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Advance Paid: ₹${b.advanceAmountPaid} (${b.paymentStatus})", color = StatusGreen, fontSize = 11.sp)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { onViewReceipt(b) },
                                    modifier = Modifier.height(34.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Receipt", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919842561720"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.height(34.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp)
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Navy900, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Contact", color = Navy900, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
