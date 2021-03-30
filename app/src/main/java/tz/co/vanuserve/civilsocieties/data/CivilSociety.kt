package tz.co.vanuserve.civilsocieties.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "civil_societies")
data class CivilSociety (
    @PrimaryKey val name:String,
    val description:String,
    val avatar:String
        )