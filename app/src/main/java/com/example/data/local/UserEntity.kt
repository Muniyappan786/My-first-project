package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["emailOrPhone"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val emailOrPhone: String,
    val passwordHash: String,
    val role: String = "CUSTOMER", // "CUSTOMER", "OWNER", "ADMIN"
    val district: String = "Villupuram",
    val taluk: String = "Gingee",
    val createdAt: Long = System.currentTimeMillis()
)
