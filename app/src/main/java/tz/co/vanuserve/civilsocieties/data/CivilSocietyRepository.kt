package tz.co.vanuserve.civilsocieties.data

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import tz.co.vanuserve.civilsocieties.api.CivilSocietyApi
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.util.FileUtils
import tz.co.vanuserve.civilsocieties.util.networkBoundResource
import java.io.File
import javax.inject.Inject

class CivilSocietyRepository @Inject constructor(
    private val api: CivilSocietyApi,
    private val db: CivilSocietyDatabase
) {

    private val civilSocietyDao=db.civilSocietyDao()

    fun getCivilSocieties(searchQuery: String)= networkBoundResource(
        query = {
            civilSocietyDao.getCivilSocieties(
                searchQuery
            )
        },
        fetch = {
            //delay(2000)
            api.getCivilSocieties()
        },
        saveFetchResult = { civilSocieties ->
            db.withTransaction {
                civilSocietyDao.deleteAllCivilSocieties()
                civilSocietyDao.insertCivilSocieties(civilSocieties)
            }
        }
    )

    suspend fun uploadDetails(csoName: String, csoDesc: String,region:String,latitude:String?,longitude:String?, file:File?)=
        safeApiCall {
            //Convert types to RequestBody
            val name: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), csoName)
            val description: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), csoDesc)
            val regionReqBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), region)
            val latReqBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude)
            val longReqBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude)
            //File file=new File(filepath);
            val requestFile:RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
           val csoImageToUpload: MultipartBody.Part  = MultipartBody.Part.createFormData("image", file!!.name, requestFile)
            api.uploadDetails(name, description,regionReqBody,latReqBody,longReqBody,csoImageToUpload) }

    suspend fun login(
        email: String,
        password:String
    )=safeApiCall {
        api.login(email,password)
    }

    suspend fun saveAuthToken(token:String,preferences:UserPreferences){
        preferences.saveAuthToken(token)
    }

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return  withContext(Dispatchers.IO){
            try{
                Resource.Success(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else->{
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }

    suspend fun register(email: String, password: String) =
         safeApiCall {
             api.register(email,password)
         }
}