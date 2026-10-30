package com.example.model

enum class AppLanguage {
    EN, TA
}

enum class AppRole {
    CUSTOMER, OWNER, ADMIN
}

enum class BookingStatus(val displayNameEn: String, val displayNameTa: String) {
    REQUEST_SENT("Request Sent", "கோரிக்கை அனுப்பப்பட்டது"),
    OWNER_REVIEWING("Owner Reviewing", "உரிமையாளர் பரிசீலனை"),
    ACCEPTED("Accepted - Awaiting Advance", "ஏற்றுக்கொள்ளப்பட்டது"),
    CONFIRMED("Booking Confirmed", "முன்பதிவு உறுதி"),
    WORK_STARTED("Work In Progress", "பணி நடப்பில் உள்ளது"),
    COMPLETED("Work Completed", "பணி முடிந்தது"),
    CANCELLED("Cancelled", "ரத்து செய்யப்பட்டது")
}

data class VehicleCategory(
    val id: String,
    val nameEn: String,
    val nameTa: String,
    val icon: String,
    val description: String,
    val vehicleCount: Int
)

data class HeavyVehicle(
    val id: String,
    val title: String,
    val titleTa: String,
    val categoryId: String,
    val brand: String,
    val model: String,
    val hourlyRate: Int,
    val dailyRate: Int,
    val minHours: Int = 4,
    val minDays: Int = 1,
    val locationDistrict: String,
    val taluk: String,
    val village: String,
    val driverIncluded: Boolean = true,
    val fuelIncluded: Boolean = false,
    val transportIncluded: Boolean = false,
    val rating: Float = 4.8f,
    val reviewCount: Int = 24,
    val isAvailable: Boolean = true,
    val verifiedOwner: Boolean = true,
    val ownerName: String,
    val ownerPhone: String,
    val ownerWhatsapp: String,
    val ownerExperienceYears: Int = 8,
    val specs: Map<String, String>,
    val imageAssetPath: String, // Path in assets/vehicles/
    val imageGallery: List<String> = emptyList(),
    val description: String,
    val descriptionTa: String,
    val worksiteSuitability: List<String>,
    val serviceRadiusKm: Int = 45
)

data class BookingRequest(
    val id: String,
    val vehicleId: String,
    val vehicleTitle: String,
    val vehicleImagePath: String,
    val customerName: String,
    val customerPhone: String,
    val district: String,
    val taluk: String,
    val village: String,
    val worksiteAddress: String,
    val startDate: String,
    val durationDays: Int,
    val estimatedHours: Int,
    val rateApplied: Int,
    val driverCost: Int,
    val transportCost: Int,
    val totalEstimatedPrice: Int,
    val advanceAmountPaid: Int,
    val paymentStatus: String, // "Paid (Advance)", "Pending", "Refunded"
    val bookingStatus: BookingStatus,
    val customerNotes: String,
    val createdAt: String
)

data class CustomerRequirement(
    val id: String,
    val customerName: String,
    val phone: String,
    val district: String,
    val taluk: String,
    val village: String,
    val vehicleCategory: String,
    val preferredModel: String,
    val workType: String,
    val requiredDate: String,
    val duration: String,
    val budget: String,
    val notes: String,
    val status: String = "Active",
    val responsesCount: Int = 0,
    val createdAt: String
)

data class DriverOperator(
    val id: String,
    val name: String,
    val phone: String,
    val district: String,
    val taluk: String,
    val experienceYears: Int,
    val machineryHandled: List<String>,
    val licenseType: String,
    val dailyWage: Int,
    val rating: Float,
    val isAvailable: Boolean,
    val completedJobs: Int
)

data class ExtractedRequirement(
    val category: String? = null,
    val location: String? = null,
    val workType: String? = null,
    val durationDays: Int? = null,
    val budget: Int? = null
)

data class AiMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String,
    val matchedVehicles: List<HeavyVehicle> = emptyList(),
    val extractedRequirement: ExtractedRequirement? = null
)
