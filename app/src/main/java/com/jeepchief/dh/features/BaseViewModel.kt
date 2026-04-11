package com.jeepchief.dh.features

import androidx.lifecycle.ViewModel
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class BaseViewModel : ViewModel() {
    private val _message = MutableSharedFlow<String>(
        extraBufferCapacity = 1
    )
    val message: SharedFlow<String> = _message.asSharedFlow()

    protected fun emitMessage(message: String) {
        _message.tryEmit(message)
    }
}
