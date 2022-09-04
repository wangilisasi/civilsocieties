package tz.co.vanuserve.civilsocieties.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tz.co.vanuserve.civilsocieties.api.Resource
import tz.co.vanuserve.civilsocieties.data.CivilSocietyRepository
import tz.co.vanuserve.civilsocieties.data.UserPreferences
import tz.co.vanuserve.civilsocieties.responses.LoginResponse
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CivilSocietyRepository
): ViewModel() {

    private val _loginResponse:MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse:LiveData<Resource<LoginResponse>>
    get()=_loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }


    suspend fun saveAuthToken(token: String,preferences: UserPreferences) {
        repository.saveAuthToken(token,preferences)
    }
}