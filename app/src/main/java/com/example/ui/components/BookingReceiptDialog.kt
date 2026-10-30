package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BookingRequest
import com.example.ui.theme.*

@Composable
fun BookingReceiptDialog(
    booking: BookingRequest,
    onDismiss: () -> Unit,
    onViewBookings: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, BorderHighlight, RoundedCornerShape(20.dp))
                .testTag("booking_receipt_dialog"),
            color = Navy900
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(StatusGreenBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = StatusGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Booking Confirmed!",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "HeavyLink Reservation Ref: ${booking.id}",
                    color = ConstructionYellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Receipt Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = CardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Machine", color = TextSecondary, fontSize = 12.sp)
                            Text(booking.vehicleTitle.take(22) + "...", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Customer", color = TextSecondary, fontSize = 12.sp)
                            Text(booking.customerName, color = TextPrimary, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Worksite", color = TextSecondary, fontSize = 12.sp)
                            Text("${booking.taluk}, ${booking.district}", color = TextPrimary, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Start Date", color = TextSecondary, fontSize = 12.sp)
                            Text("${booking.startDate} (${booking.durationDays} days)", color = TextPrimary, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = BorderSubtle)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Advance Token Paid", color = StatusGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("₹${booking.advanceAmountPaid}", color = StatusGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Balance Due on Site", color = TextSecondary, fontSize = 12.sp)
                            Text("₹${booking.totalEstimatedPrice - booking.advanceAmountPaid}", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Owner is preparing the equipment and dispatch trailer. You will receive an SMS and WhatsApp update with driver live GPS tracking.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Close")
                    }

                    Button(
                        onClick = {
                            onDismiss()
                            onViewBookings()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("My Bookings", color = Navy900, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
