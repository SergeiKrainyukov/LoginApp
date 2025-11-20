package ru.yandex.loginapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Заменяем основной диспатчер корутин на тестовый
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        // Восстанавливаем основной диспатчер после тестов
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        // Вызываем login с пустыми полями
        viewModel.login("", "")

        // Проверяем, что состояние стало EmptyFieldsError
        assertEquals(LoginScreenState.EmptyFieldsError, viewModel.state.value)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        // Вызываем login с некорректным email (без @ и точек)
        viewModel.login("invalidEmail", "password123")

        // Проверяем, что состояние стало EmailValidationError
        assertEquals(LoginScreenState.EmailValidationError, viewModel.state.value)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        // Вызываем login с корректными данными
        viewModel.login("test@email.com", "password123")

        // Запускаем все корутины, которые ожидают выполнения
        testDispatcher.scheduler.runCurrent()

        // Проверяем, что состояние перешло в Loading
        assertEquals(LoginScreenState.Loading, viewModel.state.value)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        // Вызываем login с корректными данными
        viewModel.login("test@email.com", "password123")

        // Запускаем корутины и проверяем, что состояние Loading
        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginScreenState.Loading, viewModel.state.value)

        // Перематываем время на 3000 мс (время delay в LoginViewModel)
        testDispatcher.scheduler.advanceTimeBy(3000)
        testDispatcher.scheduler.runCurrent()

        // Проверяем, что состояние перешло в Success
        assertEquals(LoginScreenState.Success, viewModel.state.value)
    }
}