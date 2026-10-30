package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FleetRepository
import com.example.data.local.AppDatabase
import com.example.data.local.EquipmentEntity
import com.example.data.local.UserEntity
import com.example.data.local.toHeavyVehicle
import com.example.data.repository.UserRepository
import com.example.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class NavTab {
    HOME,
    EXPLORE,
    AI_FINDER,
    REQUIREMENTS,
    BOOKINGS,
    DRIVERS,
    OWNER_PANEL,
    ADMIN_PANEL,
    SUPPORT
}

data class HeavyLinkUiState(
    val language: AppLanguage = AppLanguage.EN,
    val currentRole: AppRole = AppRole.CUSTOMER,
    val currentTab: NavTab = NavTab.HOME,
    val currentUser: UserEntity? = null,
    val isLoggedIn: Boolean = false,
    val showAuthScreen: Boolean = false,
    val isAuthLoading: Boolean = false,
    val authErrorMessage: String? = null,
    val searchQuery: String = "",
    val selectedDistrict: String = "Villupuram",
    val selectedTaluk: String = "All Taluks",
    val selectedCategoryId: String = "all",
    val driverIncludedOnly: Boolean = false,
    val fuelIncludedOnly: Boolean = false,
    val availableOnly: Boolean = false,
    val maxHourlyBudget: Int = 5000,
    val selectedVehicleForDetail: HeavyVehicle? = null,
    val vehicleForBooking: HeavyVehicle? = null,
    val confirmedBookingReceipt: BookingRequest? = null,
    val showAddVehicleDialog: Boolean = false,
    val showPostRequirementDialog: Boolean = false,
    val bannerNotice: String? = null,
    val aiChatMessages: List<AiMessage> = emptyList(),
    val aiInputText: String = "",
    val isAiThinking: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class HeavyLinkViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val userRepository = UserRepository(db.userDao())
    private val equipmentDao = db.equipmentDao()

    private val _uiState = MutableStateFlow(HeavyLinkUiState())
    val uiState: StateFlow<HeavyLinkUiState> = _uiState.asStateFlow()

    // Real vehicles derived strictly from Room database — starts empty on fresh setup
    val vehicles: StateFlow<List<HeavyVehicle>> = equipmentDao.getAllEquipment()
        .map { list: List<EquipmentEntity> ->
            val mapped = list.map { it.toHeavyVehicle() }
            FleetRepository.setVehicles(mapped)
            mapped
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val categories: StateFlow<List<VehicleCategory>> = vehicles
        .map { vehicleList ->
            FleetRepository.getCategoriesWithCounts(vehicleList)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FleetRepository.getCategories()
        )

    val bookings = FleetRepository.bookings
    val requirements = FleetRepository.requirements
    val drivers = FleetRepository.drivers
    val districts = FleetRepository.getDistricts()
    val allRegisteredUsers = userRepository.allUsers

    // Reactive flow of equipment belonging strictly to the logged-in owner
    val myEquipment: StateFlow<List<EquipmentEntity>> = _uiState
        .flatMapLatest { state ->
            val email = state.currentUser?.emailOrPhone
            if (email.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                equipmentDao.getEquipmentByOwner(email)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Seed initial welcoming AI message
        val welcomeEn = "Vanakkam! I am HeavyLink AI. Tell me what machine you need, where in Tamil Nadu, and what work you have (e.g., 'Enakku Gingee pakkathula 2 naal-ku pond thondurathukku Hitachi venum' or 'I need a JCB near Villupuram tomorrow')."
        val initialMsg = AiMessage(
            id = "ai-init",
            text = welcomeEn,
            isUser = false,
            timestamp = "Just now"
        )
        _uiState.update { it.copy(aiChatMessages = listOf(initialMsg)) }
    }

    // --- Authentication Actions ---

    fun openAuthScreen() {
        _uiState.update { it.copy(showAuthScreen = true, authErrorMessage = null) }
    }

    fun closeAuthScreen() {
        _uiState.update { it.copy(showAuthScreen = false, authErrorMessage = null) }
    }

    fun clearAuthError() {
        _uiState.update { it.copy(authErrorMessage = null) }
    }

    fun login(emailOrPhone: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, authErrorMessage = null) }
            val result = userRepository.login(emailOrPhone, password)
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                val mappedRole = when (user.role.uppercase()) {
                    "OWNER" -> AppRole.OWNER
                    "ADMIN" -> AppRole.ADMIN
                    else -> AppRole.CUSTOMER
                }
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        isLoggedIn = true,
                        currentRole = mappedRole,
                        currentTab = if (mappedRole == AppRole.OWNER) NavTab.OWNER_PANEL else NavTab.HOME,
                        showAuthScreen = false,
                        isAuthLoading = false,
                        authErrorMessage = null,
                        bannerNotice = "Welcome back, ${user.fullName}!"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        authErrorMessage = result.exceptionOrNull()?.message ?: "Login failed."
                    )
                }
            }
        }
    }

    fun register(
        fullName: String,
        emailOrPhone: String,
        password: String,
        role: String,
        district: String,
        taluk: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, authErrorMessage = null) }
            val result = userRepository.register(
                fullName = fullName,
                emailOrPhone = emailOrPhone,
                password = password,
                role = role,
                district = district,
                taluk = taluk
            )
            if (result.isSuccess) {
                val user = result.getOrNull()!!
                val mappedRole = when (user.role.uppercase()) {
                    "OWNER" -> AppRole.OWNER
                    "ADMIN" -> AppRole.ADMIN
                    else -> AppRole.CUSTOMER
                }
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        isLoggedIn = true,
                        currentRole = mappedRole,
                        currentTab = if (mappedRole == AppRole.OWNER) NavTab.OWNER_PANEL else NavTab.HOME,
                        showAuthScreen = false,
                        isAuthLoading = false,
                        authErrorMessage = null,
                        bannerNotice = "Account created successfully! Welcome to HeavyLink, ${user.fullName}."
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        authErrorMessage = result.exceptionOrNull()?.message ?: "Registration failed."
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                currentUser = null,
                isLoggedIn = false,
                currentRole = AppRole.CUSTOMER,
                currentTab = NavTab.HOME,
                bannerNotice = "You have been logged out securely."
            )
        }
    }

    fun continueAsGuest() {
        _uiState.update { it.copy(showAuthScreen = false) }
    }

    // --- General App Actions ---

    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    fun setRole(role: AppRole) {
        _uiState.update { it.copy(currentRole = role) }
    }

    fun setTab(tab: NavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setDistrict(district: String) {
        _uiState.update { it.copy(selectedDistrict = district, selectedTaluk = "All Taluks") }
    }

    fun setTaluk(taluk: String) {
        _uiState.update { it.copy(selectedTaluk = taluk) }
    }

    fun setCategory(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun toggleDriverFilter() {
        _uiState.update { it.copy(driverIncludedOnly = !it.driverIncludedOnly) }
    }

    fun toggleFuelFilter() {
        _uiState.update { it.copy(fuelIncludedOnly = !it.fuelIncludedOnly) }
    }

    fun toggleAvailableFilter() {
        _uiState.update { it.copy(availableOnly = !it.availableOnly) }
    }

    fun openVehicleDetail(vehicle: HeavyVehicle) {
        _uiState.update { it.copy(selectedVehicleForDetail = vehicle) }
    }

    fun closeVehicleDetail() {
        _uiState.update { it.copy(selectedVehicleForDetail = null) }
    }

    fun startBooking(vehicle: HeavyVehicle) {
        _uiState.update { it.copy(vehicleForBooking = vehicle, selectedVehicleForDetail = null) }
    }

    fun dismissBooking() {
        _uiState.update { it.copy(vehicleForBooking = null) }
    }

    fun submitBooking(
        vehicle: HeavyVehicle,
        customerName: String,
        phone: String,
        village: String,
        worksite: String,
        startDate: String,
        durationDays: Int,
        estimatedHours: Int,
        notes: String
    ) {
        val calculatedTotal = (vehicle.dailyRate * durationDays) + if (!vehicle.transportIncluded) 3000 else 0
        val advanceToken = (calculatedTotal * 0.20).toInt()
        val bookingId = "HL-BK-${(1000..9999).random()}"
        val nowStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val newBooking = BookingRequest(
            id = bookingId,
            vehicleId = vehicle.id,
            vehicleTitle = vehicle.title,
            vehicleImagePath = vehicle.imageAssetPath,
            customerName = customerName.ifBlank { _uiState.value.currentUser?.fullName ?: "Customer" },
            customerPhone = phone.ifBlank { _uiState.value.currentUser?.emailOrPhone ?: "+91 98400 00000" },
            district = vehicle.locationDistrict,
            taluk = vehicle.taluk,
            village = village.ifBlank { vehicle.village },
            worksiteAddress = worksite.ifBlank { "${vehicle.taluk} Works Site" },
            startDate = startDate,
            durationDays = durationDays,
            estimatedHours = estimatedHours,
            rateApplied = vehicle.dailyRate,
            driverCost = if (vehicle.driverIncluded) 0 else 1000 * durationDays,
            transportCost = if (vehicle.transportIncluded) 0 else 3000,
            totalEstimatedPrice = calculatedTotal,
            advanceAmountPaid = advanceToken,
            paymentStatus = "Advance Paid (Demo UPI: success)",
            bookingStatus = BookingStatus.CONFIRMED,
            customerNotes = notes,
            createdAt = nowStr
        )

        FleetRepository.addBooking(newBooking)
        _uiState.update {
            it.copy(
                vehicleForBooking = null,
                confirmedBookingReceipt = newBooking,
                bannerNotice = "Booking #${newBooking.id} Confirmed! Vehicle reserved."
            )
        }
    }

    fun dismissReceipt() {
        _uiState.update { it.copy(confirmedBookingReceipt = null) }
    }

    fun submitRequirement(
        name: String,
        phone: String,
        district: String,
        taluk: String,
        village: String,
        category: String,
        workType: String,
        date: String,
        duration: String,
        budget: String,
        notes: String
    ) {
        val reqId = "HL-REQ-${(600..999).random()}"
        val nowStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val req = CustomerRequirement(
            id = reqId,
            customerName = name.ifBlank { _uiState.value.currentUser?.fullName ?: "Customer" },
            phone = phone.ifBlank { _uiState.value.currentUser?.emailOrPhone ?: "+91 98400 00000" },
            district = district,
            taluk = taluk,
            village = village,
            vehicleCategory = category,
            preferredModel = "Standard Heavy",
            workType = workType,
            requiredDate = date,
            duration = duration,
            budget = budget,
            notes = notes,
            createdAt = nowStr
        )
        FleetRepository.addRequirement(req)
        _uiState.update {
            it.copy(
                showPostRequirementDialog = false,
                bannerNotice = "Requirement posted! Local fleet owners notified."
            )
        }
    }

    fun submitNewVehicleByOwner(
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
        selectedImageFileName: String
    ) {
        val currentEmail = _uiState.value.currentUser?.emailOrPhone ?: phone
        val currentOwnerName = _uiState.value.currentUser?.fullName ?: ownerName

        viewModelScope.launch {
            val equipmentEntity = EquipmentEntity(
                ownerEmail = currentEmail,
                ownerName = currentOwnerName,
                ownerPhone = phone,
                title = title,
                categoryId = category,
                brand = brand,
                model = model,
                dailyRate = dailyRate,
                hourlyRate = hourlyRate,
                district = district,
                taluk = taluk,
                village = village,
                driverIncluded = driverIncluded,
                fuelIncluded = fuelIncluded,
                description = description,
                imageAssetPath = selectedImageFileName,
                isAvailable = true
            )
            equipmentDao.insertEquipment(equipmentEntity)

            _uiState.update {
                it.copy(
                    showAddVehicleDialog = false,
                    bannerNotice = "New machine '$title' successfully saved to your equipment!"
                )
            }
        }
    }

    fun deleteEquipment(id: Int) {
        viewModelScope.launch {
            equipmentDao.deleteEquipment(id)
            _uiState.update { it.copy(bannerNotice = "Equipment deleted from your fleet.") }
        }
    }

    fun toggleEquipmentAvailability(id: Int, isAvailable: Boolean) {
        viewModelScope.launch {
            equipmentDao.updateAvailability(id, isAvailable)
            _uiState.update {
                it.copy(bannerNotice = if (isAvailable) "Equipment marked available." else "Equipment marked busy on site.")
            }
        }
    }

    fun openAddVehicleDialog() {
        _uiState.update { it.copy(showAddVehicleDialog = true) }
    }

    fun closeAddVehicleDialog() {
        _uiState.update { it.copy(showAddVehicleDialog = false) }
    }

    fun openPostRequirementDialog() {
        _uiState.update { it.copy(showPostRequirementDialog = true) }
    }

    fun closePostRequirementDialog() {
        _uiState.update { it.copy(showPostRequirementDialog = false) }
    }

    fun clearBanner() {
        _uiState.update { it.copy(bannerNotice = null) }
    }

    fun setAiInputText(text: String) {
        _uiState.update { it.copy(aiInputText = text) }
    }

    fun sendAiQuery(userText: String) {
        if (userText.isBlank()) return
        val userMsg = AiMessage(
            id = "user-${System.currentTimeMillis()}",
            text = userText,
            isUser = true,
            timestamp = "Just now"
        )
        val updatedList = _uiState.value.aiChatMessages + userMsg
        _uiState.update { it.copy(aiChatMessages = updatedList, aiInputText = "", isAiThinking = true) }

        viewModelScope.launch {
            delay(700)
            val (extracted, matchedVehicles) = FleetRepository.parseNaturalLanguageQuery(userText)

            val replyText = buildString {
                if (matchedVehicles.isEmpty()) {
                    append("No heavy machines matching your request are currently listed in the database.\n")
                    if (extracted.category != null) append("• Requested Machine: ${extracted.category.uppercase()}\n")
                    if (extracted.location != null) append("• Location: ${extracted.location}\n")
                    append("\nBe the first to post a worksite requirement or list your equipment with zero brokerage.")
                } else {
                    append("Found ${matchedVehicles.size} available machines matching your request!\n")
                    if (extracted.category != null) append("• Machine: ${extracted.category.uppercase()}\n")
                    if (extracted.location != null) append("• Location: ${extracted.location}\n")
                    if (extracted.workType != null) append("• Work Type: ${extracted.workType}\n")
                    if (extracted.durationDays != null) append("• Duration: ${extracted.durationDays} day(s)\n")
                    append("\nCheck the matched vehicles below. You can book directly with verified owners.")
                }
            }

            val aiReply = AiMessage(
                id = "ai-${System.currentTimeMillis()}",
                text = replyText,
                isUser = false,
                timestamp = "Just now",
                matchedVehicles = matchedVehicles,
                extractedRequirement = extracted
            )

            _uiState.update {
                it.copy(
                    aiChatMessages = it.aiChatMessages + aiReply,
                    isAiThinking = false
                )
            }
        }
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        FleetRepository.updateBookingStatus(bookingId, status)
        _uiState.update { it.copy(bannerNotice = "Booking #$bookingId updated to ${status.displayNameEn}") }
    }
}
