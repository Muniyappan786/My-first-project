package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppRole
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Navy900
import com.example.viewmodel.HeavyLinkViewModel
import com.example.viewmodel.NavTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HeavyLinkApp()
            }
        }
    }
}

@Composable
fun HeavyLinkApp(viewModel: HeavyLinkViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val vehicles by viewModel.vehicles.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val requirements by viewModel.requirements.collectAsStateWithLifecycle()
    val allUsers by viewModel.allRegisteredUsers.collectAsStateWithLifecycle(initialValue = emptyList())
    val myEquipment by viewModel.myEquipment.collectAsStateWithLifecycle()

    val registeredOperators = remember(allUsers) {
        allUsers.filter {
            it.role.equals("OPERATOR", ignoreCase = true) ||
            it.role.equals("DRIVER", ignoreCase = true)
        }
    }

    // Handle back button
    BackHandler(enabled = uiState.showAuthScreen || uiState.currentTab != NavTab.HOME) {
        if (uiState.showAuthScreen) {
            viewModel.closeAuthScreen()
        } else {
            viewModel.setTab(NavTab.HOME)
        }
    }

    if (uiState.showAuthScreen) {
        AuthScreen(
            language = uiState.language,
            isLoading = uiState.isAuthLoading,
            errorMessage = uiState.authErrorMessage,
            onLogin = { email, pass -> viewModel.login(email, pass) },
            onRegister = { name, email, pass, role, dist, taluk ->
                viewModel.register(name, email, pass, role, dist, taluk)
            },
            onContinueAsGuest = { viewModel.continueAsGuest() },
            onClearError = { viewModel.clearAuthError() }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Navy900),
            topBar = {
                HeavyLinkTopBar(
                    language = uiState.language,
                    currentRole = uiState.currentRole,
                    currentUser = uiState.currentUser,
                    isLoggedIn = uiState.isLoggedIn,
                    bannerNotice = uiState.bannerNotice,
                    onLanguageToggle = { viewModel.setLanguage(it) },
                    onRoleChange = { role ->
                        viewModel.setRole(role)
                        if (role == AppRole.OWNER) {
                            if (!uiState.isLoggedIn) {
                                viewModel.openAuthScreen()
                            } else {
                                viewModel.setTab(NavTab.OWNER_PANEL)
                            }
                        } else if (role == AppRole.ADMIN) {
                            if (!uiState.isLoggedIn) {
                                viewModel.openAuthScreen()
                            } else {
                                viewModel.setTab(NavTab.ADMIN_PANEL)
                            }
                        } else {
                            viewModel.setTab(NavTab.HOME)
                        }
                    },
                    onAuthClick = { viewModel.openAuthScreen() },
                    onLogoutClick = { viewModel.logout() },
                    onClearBanner = { viewModel.clearBanner() }
                )
            },
            bottomBar = {
                HeavyLinkBottomNav(
                    currentTab = uiState.currentTab,
                    currentRole = uiState.currentRole,
                    language = uiState.language,
                    onTabSelected = { tab ->
                        if ((tab == NavTab.OWNER_PANEL || tab == NavTab.ADMIN_PANEL) && !uiState.isLoggedIn) {
                            viewModel.openAuthScreen()
                        } else {
                            viewModel.setTab(tab)
                        }
                    }
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Navy900)
            ) {
                when (uiState.currentTab) {
                    NavTab.HOME -> HomeScreen(
                        vehicles = vehicles,
                        categories = categories,
                        language = uiState.language,
                        onVehicleClick = { viewModel.openVehicleDetail(it) },
                        onBookClick = { viewModel.startBooking(it) },
                        onNavigateToAiFinder = { viewModel.setTab(NavTab.AI_FINDER) },
                        onNavigateToExplore = { viewModel.setTab(NavTab.EXPLORE) },
                        onPostRequirementClick = { viewModel.openPostRequirementDialog() }
                    )

                    NavTab.EXPLORE -> ExploreScreen(
                        vehicles = vehicles,
                        categories = categories,
                        districts = viewModel.districts,
                        language = uiState.language,
                        searchQuery = uiState.searchQuery,
                        selectedDistrict = uiState.selectedDistrict,
                        selectedTaluk = uiState.selectedTaluk,
                        selectedCategoryId = uiState.selectedCategoryId,
                        driverIncludedOnly = uiState.driverIncludedOnly,
                        fuelIncludedOnly = uiState.fuelIncludedOnly,
                        availableOnly = uiState.availableOnly,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onDistrictChange = { viewModel.setDistrict(it) },
                        onTalukChange = { viewModel.setTaluk(it) },
                        onCategoryChange = { viewModel.setCategory(it) },
                        onToggleDriver = { viewModel.toggleDriverFilter() },
                        onToggleFuel = { viewModel.toggleFuelFilter() },
                        onToggleAvailable = { viewModel.toggleAvailableFilter() },
                        onVehicleClick = { viewModel.openVehicleDetail(it) },
                        onBookClick = { viewModel.startBooking(it) },
                        onPostRequirementClick = { viewModel.openPostRequirementDialog() }
                    )

                    NavTab.AI_FINDER -> AiFinderScreen(
                        messages = uiState.aiChatMessages,
                        inputText = uiState.aiInputText,
                        isThinking = uiState.isAiThinking,
                        language = uiState.language,
                        onInputTextChange = { viewModel.setAiInputText(it) },
                        onSendMessage = { viewModel.sendAiQuery(it) },
                        onVehicleClick = { viewModel.openVehicleDetail(it) },
                        onBookClick = { viewModel.startBooking(it) }
                    )

                    NavTab.BOOKINGS -> BookingsScreen(
                        bookings = bookings,
                        currentUser = uiState.currentUser,
                        language = uiState.language,
                        onViewReceipt = { b ->
                            val v = vehicles.firstOrNull { it.id == b.vehicleId }
                            if (v != null) viewModel.startBooking(v)
                        },
                        onExploreClick = { viewModel.setTab(NavTab.EXPLORE) },
                        onLoginClick = { viewModel.openAuthScreen() }
                    )

                    NavTab.DRIVERS -> DriversHubScreen(
                        registeredOperators = registeredOperators,
                        currentUser = uiState.currentUser,
                        language = uiState.language,
                        onRegisterOperatorClick = { viewModel.openAuthScreen() }
                    )

                    NavTab.OWNER_PANEL -> OwnerDashboardScreen(
                        currentUser = uiState.currentUser,
                        myEquipment = myEquipment,
                        myBookings = bookings.filter { b ->
                            myEquipment.any { eq -> eq.title.equals(b.vehicleTitle, ignoreCase = true) }
                        },
                        language = uiState.language,
                        onAddVehicleClick = { viewModel.openAddVehicleDialog() },
                        onToggleAvailability = { id, isAvailable -> viewModel.toggleEquipmentAvailability(id, isAvailable) },
                        onDeleteEquipment = { id -> viewModel.deleteEquipment(id) }
                    )

                    NavTab.ADMIN_PANEL -> AdminDashboardScreen(
                        vehicles = vehicles,
                        requirements = requirements,
                        users = allUsers,
                        bookings = bookings,
                        language = uiState.language
                    )

                    NavTab.SUPPORT -> SupportConfigScreen(
                        language = uiState.language
                    )

                    else -> HomeScreen(
                        vehicles = vehicles,
                        categories = categories,
                        language = uiState.language,
                        onVehicleClick = { viewModel.openVehicleDetail(it) },
                        onBookClick = { viewModel.startBooking(it) },
                        onNavigateToAiFinder = { viewModel.setTab(NavTab.AI_FINDER) },
                        onNavigateToExplore = { viewModel.setTab(NavTab.EXPLORE) },
                        onPostRequirementClick = { viewModel.openPostRequirementDialog() }
                    )
                }

                // Global Vehicle Detail Dialog
                uiState.selectedVehicleForDetail?.let { vehicle ->
                    VehicleDetailDialog(
                        vehicle = vehicle,
                        language = uiState.language,
                        onDismiss = { viewModel.closeVehicleDetail() },
                        onBookClick = { viewModel.startBooking(vehicle) }
                    )
                }

                // Global Booking Flow Dialog
                uiState.vehicleForBooking?.let { vehicle ->
                    BookingFlowDialog(
                        vehicle = vehicle,
                        language = uiState.language,
                        onDismiss = { viewModel.dismissBooking() },
                        onSubmit = { name, phone, village, worksite, startDate, durationDays, estimatedHours, notes ->
                            viewModel.submitBooking(
                                vehicle = vehicle,
                                customerName = name,
                                phone = phone,
                                village = village,
                                worksite = worksite,
                                startDate = startDate,
                                durationDays = durationDays,
                                estimatedHours = estimatedHours,
                                notes = notes
                            )
                        }
                    )
                }

                // Global Booking Confirmation Receipt Dialog
                uiState.confirmedBookingReceipt?.let { receipt ->
                    BookingReceiptDialog(
                        booking = receipt,
                        onDismiss = { viewModel.dismissReceipt() },
                        onViewBookings = { viewModel.setTab(NavTab.BOOKINGS) }
                    )
                }

                // Global Post Requirement Dialog
                if (uiState.showPostRequirementDialog) {
                    PostRequirementDialog(
                        language = uiState.language,
                        onDismiss = { viewModel.closePostRequirementDialog() },
                        onSubmit = { name, phone, district, taluk, village, category, workType, date, duration, budget, notes ->
                            viewModel.submitRequirement(
                                name = name,
                                phone = phone,
                                district = district,
                                taluk = taluk,
                                village = village,
                                category = category,
                                workType = workType,
                                date = date,
                                duration = duration,
                                budget = budget,
                                notes = notes
                            )
                        }
                    )
                }

                // Global Add Vehicle Dialog (for Owners)
                if (uiState.showAddVehicleDialog) {
                    AddVehicleDialog(
                        language = uiState.language,
                        onDismiss = { viewModel.closeAddVehicleDialog() },
                        onSubmit = { title, category, brand, model, dailyRate, hourlyRate, district, taluk, village, ownerName, phone, driverIncluded, fuelIncluded, description, selectedImage ->
                            viewModel.submitNewVehicleByOwner(
                                title = title,
                                category = category,
                                brand = brand,
                                model = model,
                                dailyRate = dailyRate,
                                hourlyRate = hourlyRate,
                                district = district,
                                taluk = taluk,
                                village = village,
                                ownerName = ownerName,
                                phone = phone,
                                driverIncluded = driverIncluded,
                                fuelIncluded = fuelIncluded,
                                description = description,
                                selectedImageFileName = selectedImage
                            )
                        }
                    )
                }
            }
        }
    }
}
