package tz.co.vanuserve.civilsocieties.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "civil_societies")
data class CivilSociety (
    @PrimaryKey val name:String,
    val description:String,
    val avatar:String,
    val latitude:String,
    val longitude:String,
    val region:String,
        ) :Parcelable