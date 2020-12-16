package com.maklumi.cats.model.sqlpart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kucing_table")
data class Cat(
    @ColumnInfo(name = "cat_id")
    val id: String,
    @ColumnInfo(name = "cat_name")
    val name: String,
    val lifeSpan: String,
    val temperament: String,
    val description: String,
    val origin: String,
    val image: String?,
    val weight: String,
) {
    /**
     * Room akan buat primary key. Sebab tu taruk kat class body
     */
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
