package com.zaher.navigationapplication.model.pojo


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Posts(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}