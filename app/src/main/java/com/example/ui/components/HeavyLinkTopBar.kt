package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.AppRole
import com.example.ui.theme.*

@Composable
fun HeavyLinkTopBar(
    language: AppLanguage,
    currentRole: AppRole,
    currentUser: UserEntity?,
    isLoggedIn: Boolean,
    bannerNotice: String?,
    onLanguageToggle: (AppLanguage) -> Unit,
    onRoleChange: (AppRole) -> Unit,
    onAuthClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onClearBanner: () -> Unit
) {
    var showUserMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Navy900)
    ) {
        // Main Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Titles
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ConstructionYellow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PrecisionManufacturing,
                        contentDescription = "HeavyLink Logo",
                        tint = Navy900,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "HEAVY",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "LINK",
                            color = ConstructionYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = if (language == AppLanguage.TA) "உங்கள் உள்ளூர் கனரக எந்திர சந்தை" else "Heavy Equipment Rental",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Controls: Language Toggle & User / Auth Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language toggle pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Navy700,
                    modifier = Modifier
                        .testTag("language_toggle_button")
                        .clickable {
                            onLanguageToggle(if (language == AppLanguage.EN) AppLanguage.TA else AppLanguage.EN)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = ConstructionYellow,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (language == AppLanguage.EN) "தமிழ்" else "EN",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Authentication Status / Profile Menu
                if (isLoggedIn && currentUser != null) {
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when (currentRole) {
                                AppRole.CUSTOMER -> Navy700
                                AppRole.OWNER -> ConstructionYellow
                                AppRole.ADMIN -> StatusRedBg
                            },
                            modifier = Modifier
                                .testTag("user_profile_button")
                                .clickable { showUserMenu = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = when (currentRole) {
                                        AppRole.CUSTOMER -> Icons.Default.AccountCircle
                                        AppRole.OWNER -> Icons.Default.Engineering
                                        AppRole.ADMIN -> Icons.Default.AdminPanelSettings
                                    },
                                    contentDescription = "User",
                                    tint = if (currentRole == AppRole.OWNER) Navy900 else TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = currentUser.fullName.split(" ").firstOrNull() ?: "User",
                                    color = if (currentRole == AppRole.OWNER) Navy900 else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = if (currentRole == AppRole.OWNER) Navy900 else TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showUserMenu,
                            onDismissRequest = { showUserMenu = false },
                            modifier = Modifier.background(Navy800)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(currentUser.fullName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(currentUser.emailOrPhone, color = TextSecondary, fontSize = 11.sp)
                                        Text("Role: ${currentUser.role} • ${currentUser.taluk}", color = ConstructionYellow, fontSize = 11.sp)
                                    }
                                },
                                onClick = { showUserMenu = false }
                            )

                            Divider(color = BorderSubtle)

                            DropdownMenuItem(
                                text = { Text("Customer View", color = TextPrimary) },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary) },
                                onClick = {
                                    onRoleChange(AppRole.CUSTOMER)
                                    showUserMenu = false
                                }
                            )

                            if (currentUser.role.equals("OWNER", ignoreCase = true) || currentUser.role.equals("ADMIN", ignoreCase = true)) {
                                DropdownMenuItem(
                                    text = { Text("Fleet Owner View", color = TextPrimary) },
                                    leadingIcon = { Icon(Icons.Default.Engineering, contentDescription = null, tint = ConstructionYellow) },
                                    onClick = {
                                        onRoleChange(AppRole.OWNER)
                                        showUserMenu = false
                                    }
                                )
                            }

                            if (currentUser.role.equals("ADMIN", ignoreCase = true)) {
                                DropdownMenuItem(
                                    text = { Text("Admin Console", color = TextPrimary) },
                                    leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = StatusRed) },
                                    onClick = {
                                        onRoleChange(AppRole.ADMIN)
                                        showUserMenu = false
                                    }
                                )
                            }

                            Divider(color = BorderSubtle)

                            DropdownMenuItem(
                                text = { Text("Log Out", color = StatusRed, fontWeight = FontWeight.Bold) },
                                leadingIcon = { Icon(Icons.Default.Logout, contentDescription = null, tint = StatusRed) },
                                onClick = {
                                    showUserMenu = false
                                    onLogoutClick()
                                }
                            )
                        }
                    }
                } else {
                    // Not Logged In -> Show Log In / Register Button
                    Button(
                        onClick = onAuthClick,
                        colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("topbar_login_register_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Login",
                            tint = Navy900,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.TA) "உள்நுழை / பதிவு" else "Log In / Register",
                            color = Navy900,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // Optional Live Banner Alert
        if (bannerNotice != null) {
            Surface(
                color = ConstructionYellow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Notice",
                            tint = Navy900,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = bannerNotice,
                            color = Navy900,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(
                        onClick = onClearBanner,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = Navy900,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
