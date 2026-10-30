package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao {

    @Query("SELECT * FROM equipment WHERE LOWER(ownerEmail) = LOWER(:ownerEmail) ORDER BY createdAt DESC")
    fun getEquipmentByOwner(ownerEmail: String): Flow<List<EquipmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: EquipmentEntity): Long

    @Query("DELETE FROM equipment WHERE id = :id")
    suspend fun deleteEquipment(id: Int)

    @Query("UPDATE equipment SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateAvailability(id: Int, isAvailable: Boolean)

    @Query("SELECT * FROM equipment ORDER BY createdAt DESC")
    fun getAllEquipment(): Flow<List<EquipmentEntity>>
}
