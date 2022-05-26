package com.example.repository.database

import androidx.room.*
import com.example.repository.database.model.Photo
import kotlinx.coroutines.flow.Flow


@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg photos: Photo): List<Long>

    @Delete
    suspend fun delete(photo: Photo): Int

    @Query("SELECT * FROM photo")
    fun observeAll(): Flow<List<Photo>>

    @Query("SELECT * FROM photo WHERE id = :id")
    fun observePhoto(id: String): Flow<Photo?>
}