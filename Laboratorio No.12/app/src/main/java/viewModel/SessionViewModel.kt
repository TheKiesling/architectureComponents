package com.example.architecturecomponents.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel: ViewModel() {

    private val _validAuthToken: MutableStateFlow<ActualSession> = MutableStateFlow(ActualSession(null))
    val validAuthToken: StateFlow<ActualSession> = _validAuthToken

    private lateinit var job: Job


    class ActualSession(val auth: Boolean?){
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return auth?.hashCode() ?: 0
        }
    }

    fun login(email: String, password: String, defaultEmail: String){
        viewModelScope.launch {
            delay(2000L)
            _validAuthToken.value = ActualSession(email == password && email == defaultEmail)
            if (_validAuthToken.value.auth == true) {
                job = viewModelScope.launch {
                    delay(30000L)
                    _validAuthToken.value = ActualSession(false)
                }
            }
        }
    }

    fun keepSessionActive(){
        if (this::job.isInitialized && job.isActive)
            job.cancel()
    }

    fun logout(){
        keepSessionActive()
        _validAuthToken.value = ActualSession(false)
    }
}