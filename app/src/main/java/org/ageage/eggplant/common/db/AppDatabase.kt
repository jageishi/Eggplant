package org.ageage.eggplant.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.ageage.eggplant.common.db.dao.UserDao
import org.ageage.eggplant.common.db.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {

        private var database: AppDatabase? = null

        fun setUp(context: Context) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "eggplant"
            ).build()
        }

        fun getInstance(): AppDatabase {
            return database ?: throw IllegalStateException("You must call setUp method at first.")
        }
    }
}