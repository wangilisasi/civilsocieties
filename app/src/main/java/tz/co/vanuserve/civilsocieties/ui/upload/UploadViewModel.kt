package tz.co.vanuserve.civilsocieties.ui.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tz.co.vanuserve.civilsocieties.api.CivilSocietyApi
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.api.UploadResponse
import tz.co.vanuserve.civilsocieties.data.CivilSocietyRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val csoRepository:CivilSocietyRepository
):ViewModel() {

    private val _uploadResponse: MutableLiveData<Resource<UploadResponse>> = MutableLiveData()
    val uploadResponse: LiveData<Resource<UploadResponse>>
        get() = _uploadResponse

    fun uploadDetails(
        name: String,
        description: String,
        region:String,
        latitude:String?,
        longitude:String?,
        file: File?
    ) = viewModelScope.launch {
        _uploadResponse.value = Resource.Loading
        _uploadResponse.value = csoRepository.uploadDetails(name, description,region,latitude,longitude,file)
        //Log.d(TAG, "login: AuthViewModelCalled " + _loginResponse.value)
    }
}