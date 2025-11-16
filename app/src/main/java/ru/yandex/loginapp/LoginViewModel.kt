package ru.yandex.loginapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Default)
    val state: StateFlow<LoginScreenState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = LoginScreenState.EmptyFieldsError
            return
        }
        if (!isEmailValid(email)) {
            _state.value = LoginScreenState.EmailValidationError
            return
        }
        viewModelScope.launch {
            _state.value = LoginScreenState.Loading
            delay(3000)
            // Имитация успешного входа
            _state.value = LoginScreenState.Success
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)
    }
}
