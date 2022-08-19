package com.zaher.navigationapplication.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zaher.navigationapplication.model.pojo.Posts

@Dao
interface PostsDAO {
    // suspend can execute a long running operation and wait for it to complete without blocking (run in a seperate thread).
    // vararg represented as an array of type T
    @Insert
    suspend fun insertAllItems(vararg items: Posts): List<Long>

    @Query("SELECT * FROM Posts")
    suspend fun getAllItems(): List<Posts>

    @Query("SELECT * FROM Posts WHERE uuid = :id")
    suspend fun getItemByID(id: Int): Posts

    @Query("DELETE FROM Posts")
    suspend fun deleteAllItems()
}