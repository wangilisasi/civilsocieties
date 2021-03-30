package tz.co.vanuserve.civilsocieties.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CivilSociety::class], version = 1)
abstract class CivilSocietyDatabase : RoomDatabase() {

    abstract fun civilSocietyDao():CivilSocietyDao
}