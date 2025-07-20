package com.champox.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.champox.notes.data.local.dao.NoteDao
import com.champox.notes.data.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)

@androidx.room.TypeConverters(Converters::class) // ✅ Add this line
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

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
                .fallbackToDestructiveMigration(false) // Temporary for dev
                .build()
    }
}