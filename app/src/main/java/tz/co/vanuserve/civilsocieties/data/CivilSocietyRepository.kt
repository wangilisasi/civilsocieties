package tz.co.vanuserve.civilsocieties.data

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import tz.co.vanuserve.civilsocieties.api.CivilSocietyApi
import tz.co.vanuserve.civilsocieties.util.networkBoundResource
import javax.inject.Inject

class CivilSocietyRepository @Inject constructor(
    private val api:CivilSocietyApi,
    private val db:CivilSocietyDatabase
) {
    private val civilSocietyDao=db.civilSocietyDao()

    fun getCivilSocieties()= networkBoundResource(
        query={
            civilSocietyDao.getCivilSocieties()
        },
        fetch={
            //delay(2000)
            api.getCivilSocieties()
        },
        saveFetchResult = {civilSocieties->
            db.withTransaction {
                civilSocietyDao.deleteAllCivilSocieties()
                civilSocietyDao.insertCivilSocieties(civilSocieties)
            }
        }
    )
}