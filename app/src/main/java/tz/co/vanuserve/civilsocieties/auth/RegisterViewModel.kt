package tz.co.vanuserve.civilsocieties.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.data.CivilSocietyRepository
import tz.co.vanuserve.civilsocieties.responses.RegisterResponse
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: CivilSocietyRepository
): ViewModel(){

    private val _registerResponse:MutableLiveData<Resource<RegisterResponse>> =MutableLiveData()
    val registerResponse:LiveData<Resource<RegisterResponse>>
    get()=_registerResponse



    fun register(
        email: String,
        password: String) =viewModelScope.launch {
            _registerResponse.value=Resource.Loading
            _registerResponse.value=repository.register(email,password)
    }




}