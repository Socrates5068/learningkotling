package com.example.learningkotling.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.learningkotling.data.local.entities.NotificationEntity
import androidx.room.RoomDatabase
import com.example.learningkotling.data.local.dao.NotificationDao
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Definimos la regla de la versión 1 a la versión 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Ejecutamos SQL nativo para alterar la tabla existente sin perder los datos previos
        // En SQLite, los booleanos se manejan como INTEGER (0 o 1)
        db.execSQL("ALTER TABLE notifications_table ADD COLUMN isRead INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [NotificationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Exponemos el DAO
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notification_reader_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}