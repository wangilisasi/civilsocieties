package tz.co.vanuserve.civilsocieties.api

import retrofit2.http.GET
import tz.co.vanuserve.civilsocieties.data.CivilSociety

interface CivilSocietyApi {

    companion object{
        const val BASE_URL="https://cso-app.com/api/"
    }

    @GET("csos")
    suspend fun getCivilSocieties():List<CivilSociety>
}