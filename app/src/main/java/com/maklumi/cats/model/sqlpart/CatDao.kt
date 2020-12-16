package com.maklumi.cats.model.sqlpart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSemuaKucing(vararg cat: Cat): List<Long>

    @Query("SELECT * FROM kucing_table")
    suspend fun getSemuaKucing(): List<Cat>

    @Query("SELECT * FROM kucing_table WHERE uuid = :catId")
    suspend fun getKucing(catId: Int): Cat

    @Query("DELETE FROM kucing_table ")
    suspend fun deleteTableKucing()
}