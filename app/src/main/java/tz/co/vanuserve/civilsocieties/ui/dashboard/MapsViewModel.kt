package tz.co.vanuserve.civilsocieties.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tz.co.vanuserve.civilsocieties.data.CivilSocietyRepository
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    civilSocietyRepository: CivilSocietyRepository
        ): ViewModel() {

    val civilSocieties=civilSocietyRepository.getCivilSocieties("").asLiveData()   //aslivedata turns the flow we get from the network bound resource into live data

}