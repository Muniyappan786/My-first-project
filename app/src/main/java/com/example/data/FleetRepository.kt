package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

object FleetRepository {

    private val _categories = listOf(
        VehicleCategory(
            id = "hitachi",
            nameEn = "Hitachi / Excavator",
            nameTa = "ஹிட்டாச்சி / எக்ஸ்கவேட்டர்",
            icon = "construction",
            description = "Heavy earthmoving, pond digging, rock breaking, trenching",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "jcb",
            nameEn = "JCB / Backhoe Loader",
            nameTa = "ஜேசிபி / பேக்ஹோ லோடர்",
            icon = "agriculture",
            description = "Land levelling, foundation digging, loading debris",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "trailer",
            nameEn = "Lowbed Semi Trailer",
            nameTa = "லோபெட் டிரெய்லர் (இயந்திர போக்குவரத்து)",
            icon = "local_shipping",
            description = "Heavy machinery shifting, excavator transport, long haul",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "tipper",
            nameEn = "Tipper / Dump Truck",
            nameTa = "டிப்பர் லாரி / மணல் லாரி",
            icon = "fire_truck",
            description = "Gravel, M-Sand, soil hauling and site clearance",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "tractor",
            nameEn = "Tractor & Trailer",
            nameTa = "டிராக்டர் & டிரெய்லர்",
            icon = "agriculture",
            description = "Agricultural plowing, rotavator, rural transport",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "crane",
            nameEn = "Heavy Mobile Crane",
            nameTa = "மொபைல் கிரேன்",
            icon = "build",
            description = "Pillar erection, heavy structure lifting 25T-100T",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "borewell",
            nameEn = "Borewell Drilling Rig",
            nameTa = "போர்வெல் ரிக்",
            icon = "precision_manufacturing",
            description = "Deep agricultural and residential borewell drilling",
            vehicleCount = 0
        ),
        VehicleCategory(
            id = "water_tanker",
            nameEn = "Water Tanker",
            nameTa = "தண்ணீர் டேங்கர்",
            icon = "water_drop",
            description = "Construction site curing and farm water supply",
            vehicleCount = 0
        )
    )

    private val initialVehicles = emptyList<HeavyVehicle>()

    private val _vehicles = MutableStateFlow<List<HeavyVehicle>>(initialVehicles)
    val vehicles: StateFlow<List<HeavyVehicle>> = _vehicles.asStateFlow()

    fun setVehicles(list: List<HeavyVehicle>) {
        _vehicles.value = list
    }

    fun addVehicle(vehicle: HeavyVehicle) {
        _vehicles.value = listOf(vehicle) + _vehicles.value
    }

    private val initialBookings = emptyList<BookingRequest>()

    private val _bookings = MutableStateFlow(initialBookings)
    val bookings: StateFlow<List<BookingRequest>> = _bookings.asStateFlow()

    private val initialRequirements = emptyList<CustomerRequirement>()

    private val _requirements = MutableStateFlow(initialRequirements)
    val requirements: StateFlow<List<CustomerRequirement>> = _requirements.asStateFlow()

    private val initialDrivers = emptyList<DriverOperator>()

    private val _drivers = MutableStateFlow(initialDrivers)
    val drivers: StateFlow<List<DriverOperator>> = _drivers.asStateFlow()

    fun getCategories(): List<VehicleCategory> = _categories.map { cat ->
        cat.copy(vehicleCount = _vehicles.value.count { it.categoryId == cat.id })
    }

    fun getCategoriesWithCounts(currentVehicles: List<HeavyVehicle>): List<VehicleCategory> = _categories.map { cat ->
        cat.copy(vehicleCount = currentVehicles.count { it.categoryId == cat.id })
    }

    fun getDistricts(): List<String> = listOf(
        "Villupuram",
        "Chennai",
        "Coimbatore",
        "Madurai",
        "Salem",
        "Tiruchirappalli",
        "Tiruvannamalai",
        "Cuddalore",
        "Kallakurichi"
    )

    fun getTaluksForDistrict(district: String): List<String> = when (district.lowercase()) {
        "villupuram" -> listOf("All Taluks", "Gingee", "Tindivanam", "Vikravandi", "Vanur", "Kandamangalam", "Marakkanam")
        "chennai" -> listOf("All Taluks", "Ambattur", "Guindy", "Sriperumbudur", "Poonamallee", "Tambaram")
        "coimbatore" -> listOf("All Taluks", "Pollachi", "Sulur", "Mettupalayam", "North Coimbatore")
        "salem" -> listOf("All Taluks", "Attur", "Omalur", "Sankagiri", "Mettur")
        "madurai" -> listOf("All Taluks", "Melur", "Thirumangalam", "Usilampatti", "Madurai South")
        else -> listOf("All Taluks", "Town Center", "Rural Block 1", "Rural Block 2")
    }

    fun addBooking(booking: BookingRequest) {
        val updated = listOf(booking) + _bookings.value
        _bookings.value = updated
    }

    fun updateBookingStatus(bookingId: String, newStatus: BookingStatus) {
        val updated = _bookings.value.map {
            if (it.id == bookingId) it.copy(bookingStatus = newStatus) else it
        }
        _bookings.value = updated
    }

    fun addRequirement(req: CustomerRequirement) {
        val updated = listOf(req) + _requirements.value
        _requirements.value = updated
    }

    // Natural Language Processing for Tamil and English queries
    fun parseNaturalLanguageQuery(query: String): Pair<ExtractedRequirement, List<HeavyVehicle>> {
        val q = query.lowercase()

        // Detect Location
        var location: String? = null
        if (q.contains("gingee") || q.contains("செஞ்சி") || q.contains("senji")) {
            location = "Gingee"
        } else if (q.contains("tindivanam") || q.contains("திண்டிவனம்")) {
            location = "Tindivanam"
        } else if (q.contains("villupuram") || q.contains("விழுப்புரம்")) {
            location = "Villupuram"
        } else if (q.contains("chennai") || q.contains("சென்னை") || q.contains("sriperumbudur")) {
            location = "Chennai"
        } else if (q.contains("coimbatore") || q.contains("கோவை") || q.contains("pollachi")) {
            location = "Coimbatore"
        }

        // Detect Category
        var category: String? = null
        if (q.contains("hitachi") || q.contains("ஹிட்டாச்சி") || q.contains("excavator") || q.contains("எக்ஸ்கவேட்டர்") || q.contains("sany")) {
            category = "hitachi"
        } else if (q.contains("jcb") || q.contains("ஜேசிபி") || q.contains("backhoe")) {
            category = "jcb"
        } else if (q.contains("trailer") || q.contains("டிரெய்லர்") || q.contains("lowbed") || q.contains("லோபெட்")) {
            category = "trailer"
        } else if (q.contains("tipper") || q.contains("டிப்பர்") || q.contains("lorry") || q.contains("லாரி")) {
            category = "tipper"
        } else if (q.contains("tractor") || q.contains("டிராக்டர்")) {
            category = "tractor"
        }

        // Detect Work type
        var workType: String? = null
        if (q.contains("pond") || q.contains("குளம்") || q.contains("kulam") || q.contains("thondurathukku") || q.contains("digging")) {
            workType = "Pond / Lake Digging"
        } else if (q.contains("level") || q.contains("சமப்படுத்த") || q.contains("levelling")) {
            workType = "Land Levelling"
        } else if (q.contains("shift") || q.contains("transport") || q.contains("மாற்ற") || q.contains("load")) {
            workType = "Heavy Equipment Transport"
        } else if (q.contains("canal") || q.contains("வாய்க்கால்") || q.contains("river") || q.contains("ஆறு")) {
            workType = "Canal / River Desilting"
        }

        // Detect Duration
        var durationDays: Int? = null
        val durationMatch = Regex("(\\d+)\\s*(naal|days|day|நாள்|நாட்கள்)").find(q)
        if (durationMatch != null) {
            durationDays = durationMatch.groupValues[1].toIntOrNull()
        } else if (q.contains("tomorrow") || q.contains("நாளை") || q.contains("one day") || q.contains("1 day")) {
            durationDays = 1
        }

        val extracted = ExtractedRequirement(
            category = category,
            location = location,
            workType = workType,
            durationDays = durationDays
        )

        // Match against database
        val matched = _vehicles.value.filter { vehicle ->
            var matches = true
            if (category != null && vehicle.categoryId != category) {
                matches = false
            }
            if (location != null && !vehicle.taluk.equals(location, ignoreCase = true) &&
                !vehicle.locationDistrict.equals(location, ignoreCase = true) &&
                vehicle.serviceRadiusKm < 50
            ) {
                matches = false
            }
            matches
        }

        // Return real matches only - no fake fallback data!
        return Pair(extracted, matched)
    }
}
