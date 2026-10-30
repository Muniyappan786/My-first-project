package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.model.HeavyVehicle

@Entity(
    tableName = "equipment",
    indices = [Index(value = ["ownerEmail"])]
)
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerEmail: String,
    val ownerName: String,
    val ownerPhone: String,
    val title: String,
    val categoryId: String,
    val brand: String,
    val model: String,
    val dailyRate: Int,
    val hourlyRate: Int,
    val district: String,
    val taluk: String,
    val village: String,
    val driverIncluded: Boolean,
    val fuelIncluded: Boolean,
    val description: String,
    val imageAssetPath: String,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

fun EquipmentEntity.toHeavyVehicle(): HeavyVehicle = HeavyVehicle(
    id = "eq-${this.id}",
    title = this.title,
    titleTa = this.title,
    categoryId = this.categoryId,
    brand = this.brand,
    model = this.model,
    hourlyRate = this.hourlyRate,
    dailyRate = this.dailyRate,
    minHours = 4,
    minDays = 1,
    locationDistrict = this.district,
    taluk = this.taluk,
    village = this.village,
    driverIncluded = this.driverIncluded,
    fuelIncluded = this.fuelIncluded,
    transportIncluded = false,
    rating = 5.0f,
    reviewCount = 0,
    isAvailable = this.isAvailable,
    verifiedOwner = true,
    ownerName = this.ownerName,
    ownerPhone = this.ownerPhone,
    ownerWhatsapp = this.ownerPhone,
    ownerExperienceYears = 5,
    specs = mapOf(
        "Brand" to this.brand,
        "Model" to this.model,
        "Daily Rate" to "₹${this.dailyRate} / day",
        "Hourly Rate" to "₹${this.hourlyRate} / hr",
        "Location" to "${this.taluk}, ${this.district}"
    ),
    imageAssetPath = this.imageAssetPath.ifBlank { "hitachi_2336c9fc.webp" },
    imageGallery = listOf(this.imageAssetPath.ifBlank { "hitachi_2336c9fc.webp" }),
    description = this.description,
    descriptionTa = this.description,
    worksiteSuitability = listOf("Site Excavation", "Transport", "Construction"),
    serviceRadiusKm = 50
)
