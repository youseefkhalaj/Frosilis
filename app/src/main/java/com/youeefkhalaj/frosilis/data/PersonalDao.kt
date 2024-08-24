package com.youeefkhalaj.frosilis.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalDao {
    @Query("SELECT * from personals ORDER BY name ASC")
    fun getAllItems(): Flow<List<Personal>>

    @Query("SELECT * from personals WHERE id = :id")
    fun getItem(id: Int): Flow<Personal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(personal: Personal)

    @Update
    suspend fun update(personal: Personal)

    @Delete
    suspend fun delete(personal: Personal)


}