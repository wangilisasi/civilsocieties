package tz.co.vanuserve.civilsocieties.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import tz.co.vanuserve.civilsocieties.api.CivilSocietyApi
import tz.co.vanuserve.civilsocieties.data.CivilSociety
import tz.co.vanuserve.civilsocieties.data.CivilSocietyDao
import tz.co.vanuserve.civilsocieties.data.CivilSocietyRepository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CivilSocietyViewModel @Inject constructor(
    //api:CivilSocietyApi
    civilSocietyRepository: CivilSocietyRepository

) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val civilSocietyFlow=searchQuery.flatMapLatest {
        civilSocietyRepository.getCivilSocieties(it)
    }

    //val civilSocieties = civilSocietyRepository.getCivilSocieties().asLiveData()

    val civilSocieties=civilSocietyFlow.asLiveData()



}


