package com.zaher.navigationapplication.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zaher.navigationapplication.model.pojo.Posts

@Database(entities = arrayOf(Posts::class), version = 1)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun dao(): PostsDAO

    //like static
    companion object {
        // Volatile meaning that writes to this field are immediately made visible to other threads.
        @Volatile
        private var instance: PostsDatabase? = null
        private val LOCK = Any()

        //synchronized to make only one thread access the database at the current time
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PostsDatabase::class.java,
            "posts_database"
        ).build()
    }
}
