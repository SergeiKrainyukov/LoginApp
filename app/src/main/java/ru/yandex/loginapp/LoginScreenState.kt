package ru.yandex.loginapp

sealed class LoginScreenState {
    data object Default : LoginScreenState()
    data object Loading : LoginScreenState()
    data object EmptyFieldsError : LoginScreenState()
    data object EmailValidationError : LoginScreenState()
    data object Success : LoginScreenState()
}
