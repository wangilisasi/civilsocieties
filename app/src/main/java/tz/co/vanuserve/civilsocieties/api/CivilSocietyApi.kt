package tz.co.vanuserve.civilsocieties.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import tz.co.vanuserve.civilsocieties.data.CivilSociety
import tz.co.vanuserve.civilsocieties.responses.LoginResponse
import tz.co.vanuserve.civilsocieties.responses.RegisterResponse

interface CivilSocietyApi {

    companion object {
        const val BASE_URL = "https://cso-app.herokuapp.com/"
    }

    @GET("csos")
    suspend fun getCivilSocieties(): List<CivilSociety>

    @Multipart
    @POST("csos")
    suspend fun uploadDetails(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("region") region: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part photo: MultipartBody.Part
    ): UploadResponse

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(
        @Field("email") email:String,
        @Field("password") password:String
    ): LoginResponse

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(
        @Field("email") email:String,
        @Field("password") password:String
    ): RegisterResponse
}