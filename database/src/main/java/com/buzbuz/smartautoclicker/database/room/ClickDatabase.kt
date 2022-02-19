/*
 * Copyright (C) 2021 Nain57
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */
package com.buzbuz.smartautoclicker.database.room

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.buzbuz.smartautoclicker.database.room.dao.ConditionDao
import com.buzbuz.smartautoclicker.database.room.dao.EventDao
import com.buzbuz.smartautoclicker.database.room.dao.ScenarioDao
import com.buzbuz.smartautoclicker.database.room.entity.ActionEntity
import com.buzbuz.smartautoclicker.database.room.entity.ActionTypeStringConverter
import com.buzbuz.smartautoclicker.database.room.entity.ConditionEntity
import com.buzbuz.smartautoclicker.database.room.entity.EventEntity
import com.buzbuz.smartautoclicker.database.room.entity.ScenarioEntity
import com.buzbuz.smartautoclicker.database.room.migrations.Migration1to2
import com.buzbuz.smartautoclicker.database.room.migrations.Migration2to3
import com.buzbuz.smartautoclicker.database.room.migrations.Migration3to4
import com.buzbuz.smartautoclicker.database.room.migrations.Migration4to5

/**
 * Database for the scenario and their events.
 *
 * Declare the singleton structure for the database as shown in Android Developer code lab in order to make it
 * available for the whole application. Application should uses the [scenarioDao], [eventDao] and [conditionDao] to
 * access it by using [ClickDatabase.getDatabase].
 *
 * Actual implementation is automatically generated by AndroidX Room.
 */
@Database(
    entities = [
        ActionEntity::class,
        EventEntity::class,
        ScenarioEntity::class,
        ConditionEntity::class,
    ],
    version = 5,
    exportSchema = true
)
@TypeConverters(ActionTypeStringConverter::class)
internal abstract class ClickDatabase : RoomDatabase() {

    /** The data access object for the scenario in the database. */
    abstract fun scenarioDao(): ScenarioDao
    /** The data access object for the events in the database. */
    abstract fun eventDao(): EventDao
    /** The data access object for the conditions in the database. */
    abstract fun conditionDao(): ConditionDao

    companion object {

        /** Singleton preventing multiple instances of database opening at the same time. */
        @Volatile
        private var INSTANCE: ClickDatabase? = null

        /**
         * Get the Room database singleton, or instantiates it if it wasn't yet.
         * <p>
         * @param context the Android context.
         * <p>
         * @return the Room database singleton.
         */
        fun getDatabase(context: Context): ClickDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClickDatabase::class.java,
                    "click_database"
                )
                    .addMigrations(Migration1to2)
                    .addMigrations(Migration2to3)
                    .addMigrations(Migration3to4)
                    .addMigrations(Migration4to5)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}