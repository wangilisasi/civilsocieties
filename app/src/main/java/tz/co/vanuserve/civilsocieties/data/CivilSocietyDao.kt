package tz.co.vanuserve.civilsocieties.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface CivilSocietyDao {
    @Query("SELECT * FROM civil_societies WHERE description LIKE '%'||:searchQuery||'%' OR name LIKE '%'||:searchQuery||'%' OR region LIKE '%'||:searchQuery||'%'")  //WHERE description LIKE '%'||:searchQuery||'%'
    fun getCivilSocieties(searchQuery:String): Flow<List<CivilSociety>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCivilSocieties(civilSocieties: List<CivilSociety>)

    @Query("DELETE FROM civil_societies")
    suspend fun deleteAllCivilSocieties()
}