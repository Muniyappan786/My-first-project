package com.example.data.repository

import com.example.data.local.UserDao
import com.example.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun register(
        fullName: String,
        emailOrPhone: String,
        password: String,
        role: String = "CUSTOMER",
        district: String = "Villupuram",
        taluk: String = "Gingee"
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanName = fullName.trim()
        val cleanIdentifier = emailOrPhone.trim().lowercase()
        val cleanPassword = password.trim()

        if (cleanName.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter your full name."))
        }
        if (cleanIdentifier.isBlank() || cleanIdentifier.length < 5) {
            return@withContext Result.failure(IllegalArgumentException("Please enter a valid email or mobile number."))
        }
        if (cleanPassword.length < 6) {
            return@withContext Result.failure(IllegalArgumentException("Password must be at least 6 characters."))
        }

        val existing = userDao.getUserByEmailOrPhone(cleanIdentifier)
        if (existing != null) {
            return@withContext Result.failure(IllegalArgumentException("An account with this email/phone already exists."))
        }

        val passwordHash = hashPassword(cleanPassword)
        val newUser = UserEntity(
            fullName = cleanName,
            emailOrPhone = cleanIdentifier,
            passwordHash = passwordHash,
            role = role,
            district = district,
            taluk = taluk
        )

        try {
            val generatedId = userDao.insertUser(newUser)
            val createdUser = newUser.copy(id = generatedId.toInt())
            Result.success(createdUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(
        emailOrPhone: String,
        password: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanIdentifier = emailOrPhone.trim().lowercase()
        val cleanPassword = password.trim()

        if (cleanIdentifier.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter your email or phone number."))
        }
        if (cleanPassword.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter your password."))
        }

        val user = userDao.getUserByEmailOrPhone(cleanIdentifier)
            ?: return@withContext Result.failure(IllegalArgumentException("No account found with this email/phone. Please register first."))

        val inputHash = hashPassword(cleanPassword)
        if (user.passwordHash == inputHash) {
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Incorrect password. Please verify your credentials."))
        }
    }



    companion object {
        fun hashPassword(password: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest((password + "HeavyLinkSecretSalt2026").toByteArray(Charsets.UTF_8))
            return bytes.joinToString("") { "%02x".format(it) }
        }
    }
}
