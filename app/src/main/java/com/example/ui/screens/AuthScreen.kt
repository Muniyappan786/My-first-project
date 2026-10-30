package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppRole
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    language: AppLanguage,
    isLoading: Boolean,
    errorMessage: String?,
    onLogin: (String, String) -> Unit,
    onRegister: (name: String, emailOrPhone: String, password: String, role: String, district: String, taluk: String) -> Unit,
    onContinueAsGuest: () -> Unit,
    onClearError: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("CUSTOMER") }
    var selectedDistrict by remember { mutableStateOf("Villupuram") }
    var selectedTaluk by remember { mutableStateOf("Gingee") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand Logo
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ConstructionYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PrecisionManufacturing,
                    contentDescription = "HeavyLink Logo",
                    tint = Navy900,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "HEAVY",
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 26.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "LINK",
                    color = ConstructionYellow,
                    fontWeight = FontWeight.Black,
                    fontSize = 26.sp,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = if (language == AppLanguage.TA) "உங்கள் உள்ளூர் கனரக எந்திர சந்தை" else "Find the Right Machine. Right in Your Area.",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tab Switcher (Login vs Register)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = CardSurface
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (!isRegisterMode) ConstructionYellow else Color.Transparent)
                            .clickable {
                                isRegisterMode = false
                                onClearError()
                            }
                            .testTag("tab_login"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.TA) "உள்நுழைக" else "Log In",
                            color = if (!isRegisterMode) Navy900 else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (isRegisterMode) ConstructionYellow else Color.Transparent)
                            .clickable {
                                isRegisterMode = true
                                onClearError()
                            }
                            .testTag("tab_register"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.TA) "பதிவு செய்க" else "Register",
                            color = if (isRegisterMode) Navy900 else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Card Container
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
                color = CardSurface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isRegisterMode) {
                        // Full Name
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name / Business Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ConstructionYellow) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_fullname_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ConstructionYellow,
                                unfocusedBorderColor = BorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Email or Phone
                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = { emailOrPhone = it },
                        label = { Text(if (isRegisterMode) "Email or Mobile (+91)" else "Email or Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ConstructionYellow) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password (min 6 characters)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ConstructionYellow) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ConstructionYellow,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )

                    if (isRegisterMode) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // Role Selector
                        Text(
                            text = "Account Role:",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "CUSTOMER" to "Contractor",
                                "OWNER" to "Fleet Owner",
                                "OPERATOR" to "Driver / Operator"
                            ).forEach { (role, label) ->
                                val isSelected = selectedRole == role
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, if (isSelected) ConstructionYellow else BorderSubtle, RoundedCornerShape(8.dp))
                                        .clickable { selectedRole = role },
                                    color = if (isSelected) Navy700 else Navy900
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = label,
                                            color = if (isSelected) ConstructionYellow else TextSecondary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Location
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = selectedDistrict,
                                onValueChange = { selectedDistrict = it },
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
                                value = selectedTaluk,
                                onValueChange = { selectedTaluk = it },
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
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusRedBg
                        ) {
                            Text(
                                text = errorMessage,
                                color = StatusRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (isRegisterMode) {
                                onRegister(fullName, emailOrPhone, password, selectedRole, selectedDistrict, selectedTaluk)
                            } else {
                                onLogin(emailOrPhone, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("auth_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ConstructionYellow),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Navy900)
                        } else {
                            Text(
                                text = if (isRegisterMode) "Register Secure Account" else "Log In to HeavyLink",
                                color = Navy900,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(20.dp))

            // Browse as Guest Button
            TextButton(
                onClick = onContinueAsGuest,
                modifier = Modifier.testTag("continue_as_guest_button")
            ) {
                Text(
                    text = if (language == AppLanguage.TA) "விருந்தினராக தொடரவும் →" else "Explore Marketplace as Guest →",
                    color = ConstructionYellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
