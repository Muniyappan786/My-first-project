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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiMessage
import com.example.model.AppLanguage
import com.example.model.HeavyVehicle
import com.example.ui.components.VehicleCard
import com.example.ui.theme.*

@Composable
fun AiFinderScreen(
    messages: List<AiMessage>,
    inputText: String,
    isThinking: Boolean,
    language: AppLanguage,
    onInputTextChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onVehicleClick: (HeavyVehicle) -> Unit,
    onBookClick: (HeavyVehicle) -> Unit
) {
    val quickPrompts = listOf(
        "Enakku Gingee pakkathula 2 naal-ku pond thondurathukku Hitachi venum",
        "I need a JCB near Villupuram tomorrow for land levelling",
        "Need lowbed semi trailer to shift excavator from Tindivanam",
        "Mini Hitachi needed for house basement in Gingee town"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        // Chat Header Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ConstructionYellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = Navy900,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = if (language == AppLanguage.TA) "ஹெவிலிங்க் AI வாகன உதவியாளர்" else "HeavyLink AI Vehicle Finder",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.TA) "தமிழ் & ஆங்கிலம் இரண்டிலும் புரிந்து கொள்ளும்" else "Understands Tamil & English natural speech",
                        color = StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(quickPrompts) { prompt ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Navy700,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSendMessage(prompt) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = ConstructionYellow, modifier = Modifier.size(13.dp))
                        Text(
                            text = prompt.take(35) + "...",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(messages) { msg ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (msg.isUser) Alignment.End else Alignment.Start
                ) {
                    // Bubble
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (msg.isUser) 16.dp else 4.dp,
                            bottomEnd = if (msg.isUser) 4.dp else 16.dp
                        ),
                        color = if (msg.isUser) ConstructionYellow else CardSurface,
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                color = if (msg.isUser) Navy900 else TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                fontWeight = if (msg.isUser) FontWeight.SemiBold else FontWeight.Normal
                            )

                            // If AI extracted requirements, show structured summary tags
                            if (!msg.isUser && msg.extractedRequirement != null) {
                                val req = msg.extractedRequirement
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = BorderSubtle)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (req.location != null) {
                                        Surface(shape = RoundedCornerShape(4.dp), color = Navy700) {
                                            Text("📍 ${req.location}", color = ConstructionYellow, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    if (req.category != null) {
                                        Surface(shape = RoundedCornerShape(4.dp), color = Navy700) {
                                            Text("🚜 ${req.category.uppercase()}", color = StatusGreen, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    if (req.durationDays != null) {
                                        Surface(shape = RoundedCornerShape(4.dp), color = Navy700) {
                                            Text("⏱ ${req.durationDays} Days", color = TextPrimary, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Matched Vehicles Cards Carousel inside the chat!
                    if (msg.matchedVehicles.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Verified Machines Found in Database:",
                            color = ConstructionYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        msg.matchedVehicles.forEach { v ->
                            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                                VehicleCard(
                                    vehicle = v,
                                    language = language,
                                    onDetailClick = { onVehicleClick(v) },
                                    onBookClick = { onBookClick(v) }
                                )
                            }
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = ConstructionYellow,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "AI is querying verified fleet database...",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Bottom Chat Input Field
        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 75.dp),
            color = CardSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputTextChange,
                    placeholder = {
                        Text(
                            if (language == AppLanguage.TA) "உங்கள் தேவையை தட்டச்சு செய்யவும்..." else "Tell AI what machine you need...",
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_chat_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ConstructionYellow,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ConstructionYellow)
                        .testTag("ai_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Navy900,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
