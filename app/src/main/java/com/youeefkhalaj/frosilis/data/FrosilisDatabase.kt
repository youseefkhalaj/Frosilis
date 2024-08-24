package com.youeefkhalaj.frosilis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Personal::class], version = 1, exportSchema = false)
@TypeConverters(ImageConvertor::class)
abstract class FrosilisDatabase : RoomDatabase() {
    abstract fun itemDao(): PersonalDao

    companion object {
        @Volatile
        private var Instance: FrosilisDatabase? = null

        fun getDatabase(context: Context): FrosilisDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {

                Room.databaseBuilder(context, FrosilisDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}