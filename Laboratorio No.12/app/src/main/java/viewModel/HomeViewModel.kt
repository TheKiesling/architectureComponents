package com.example.architecturecomponents.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Default)
    val state: StateFlow<State> = _state

    private val _loading: MutableStateFlow<State?> = MutableStateFlow(null)
    val loading: StateFlow<State?> = _loading

    sealed class State(){
        object Default: State()
        object Success: State()
        object Failure: State()
        object Empty: State()
    }

    fun setState(state: State){
        viewModelScope.launch {
            _loading.value = state
            delay(2000L)
            _state.value = state
            _loading.value = null
        }
    }
}