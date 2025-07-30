package com.champox.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.champox.notes.data.local.dao.NoteDao
import com.champox.notes.data.local.dao.UserDao
import com.champox.notes.data.model.Note
import com.champox.notes.data.model.User

@Database(
    entities = [Note::class, User::class], // ✅ Both entities here
    version = 3, // ⚠️ Incremented version!
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao // ✅ Add UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "notes.db"
            )
                .fallbackToDestructiveMigration() // ⚠️ Changed to true for development
                .build()
    }
}