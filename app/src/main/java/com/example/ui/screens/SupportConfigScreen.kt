package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.*

data class FaqItem(val question: String, val questionTa: String, val answer: String, val answerTa: String)

@Composable
fun SupportConfigScreen(language: AppLanguage) {
    val context = LocalContext.current
    var expandedFaqIndex by remember { mutableIntStateOf(0) }

    val faqs = listOf(
        FaqItem(
            question = "How does HeavyLink vehicle booking work?",
            questionTa = "ஹெவிலிங்க் மூலம் எந்திர முன்பதிவு எவ்வாறு இயங்குகிறது?",
            answer = "1. Select vehicle & dates. 2. Pay 20% advance token to reserve. 3. Owner dispatches machine via trailer to your worksite. 4. Pay remaining 80% balance upon machine arrival.",
            answerTa = "1. எந்திரம் மற்றும் தேதிகளைத் தேர்ந்தெடுக்கவும். 2. முன்பதிவு செய்ய 20% டோக்கன் தொகை செலுத்தவும். 3. உரிமையாளர் டிரெய்லர் மூலம் இயந்திரத்தை அனுப்புவார். 4. எந்திரம் சைட் வந்ததும் மீதி 80% செலுத்தலாம்."
        ),
        FaqItem(
            question = "Is the driver/operator included in the price?",
            questionTa = "வாடகை கட்டணத்தில் டிரைவர் கட்டணம் அடங்குமா?",
            answer = "Yes! 90% of machines listed on HeavyLink include a certified master operator. Any exceptions are clearly labeled before booking.",
            answerTa = "ஆம்! 90% எந்திரங்களுடன் அனுபவமிக்க ஓட்டுநர் இணைத்தே வழங்கப்படுகிறது. விதிவிலக்குகள் தெளிவாக குறிப்பிடப்படும்."
        ),
        FaqItem(
            question = "What is the cancellation policy?",
            questionTa = "முன்பதிவை ரத்து செய்வதற்கான விதிமுறைகள் என்ன?",
            answer = "You can cancel up to 24 hours before the dispatch start date for a 100% full refund of your advance token.",
            answerTa = "இயந்திரம் கிளம்புவதற்கு 24 மணி நேரத்திற்கு முன் ரத்து செய்தால் செலுத்திய முன்பணம் 100% முழுமையாக திருப்பித் தரப்படும்."
        ),
        FaqItem(
            question = "How can machine owners list their equipment?",
            questionTa = "உரிமையாளர்கள் தங்கள் எந்திரங்களை எவ்வாறு பட்டியலிடலாம்?",
            answer = "Switch to 'Fleet Owner' role from the top right menu, open the Manager Console, and click 'Add Machine' with your photos, specs, and daily rates.",
            answerTa = "மேல் வலதுபுற மெனுவில் 'Fleet Owner' என்பதைத் தேர்ந்தெடுத்து, 'Add Machine' பட்டனை அழுத்தி உங்கள் வாகன தகவல்களை பதிவிடலாம்."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = if (language == AppLanguage.TA) "உதவி மற்றும் வழிகாட்டுதல்" else "Help & Integration Center",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Customer support, FAQs, and enterprise system connectivity",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Direct Helpline Action Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, ConstructionYellow, RoundedCornerShape(14.dp)),
                color = CardSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("24/7 Heavy Equipment Helpline", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("+91 98425 61720 • Villupuram & Gingee Hub", color = ConstructionYellow, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("Speak with a human rental specialist", color = TextSecondary, fontSize = 11.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919842561720"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.size(38.dp).clip(CircleShape).background(Navy700)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = StatusGreen)
                        }

                        IconButton(
                            onClick = {
                                val uri = Uri.parse("https://wa.me/919842561720?text=Hi%20HeavyLink%20Support")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.size(38.dp).clip(CircleShape).background(Navy700)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = ConstructionYellow)
                        }
                    }
                }
            }
        }

        // Section: System Integrations Status
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Backend Integration & Architecture", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                color = CardSurface
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("UPI / Razorpay Payment Gateway", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Test mode active (Instant token receipts)", color = TextSecondary, fontSize = 11.sp)
                        }
                        Surface(shape = RoundedCornerShape(4.dp), color = StatusGreenBg) {
                            Text("Connected", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Divider(color = BorderSubtle)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("AI NLP Inference Engine", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("Tamil & English dual language parser", color = TextSecondary, fontSize = 11.sp)
                        }
                        Surface(shape = RoundedCornerShape(4.dp), color = StatusGreenBg) {
                            Text("Active", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Divider(color = BorderSubtle)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("High Resolution Asset Engine", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("8 user machinery assets pre-cached on device", color = TextSecondary, fontSize = 11.sp)
                        }
                        Surface(shape = RoundedCornerShape(4.dp), color = StatusGreenBg) {
                            Text("100% Loaded", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // Section: FAQs
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Frequently Asked Questions", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        items(faqs) { faq ->
            val idx = faqs.indexOf(faq)
            val isExpanded = expandedFaqIndex == idx

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .clickable { expandedFaqIndex = if (isExpanded) -1 else idx },
                color = CardSurface
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.TA) faq.questionTa else faq.question,
                            color = if (isExpanded) ConstructionYellow else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = BorderSubtle)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == AppLanguage.TA) faq.answerTa else faq.answer,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}
