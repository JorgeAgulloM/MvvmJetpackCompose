package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
import com.softyorch.mvvmjetpackcompose.utils.testContact
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateUserViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var setUserUseCase: SetUserUseCase

    @RelaxedMockK
    private lateinit var validator: IUserValidator

    private lateinit var viewModel: CreateUserViewModel

    private val contact = testContact.toDomain()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = CreateUserViewModel(setUserUseCase, validator, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        clearAllMocks()
    }

    @Test
    fun testUserValid() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchFieldError(contactUi, any()) } returns UserErrorModel()
        launch { viewModel.onDataChange(contactUi) }
        advanceUntilIdle()
        val user = viewModel.user.value
        coEvery { validator.searchError(user) } returns UserErrorModel()

        //When
        var isValid = false
        launch { isValid = viewModel.setUsers() }
        advanceUntilIdle()

        //Then
        assertTrue(isValid)
    }

    @Test
    fun testUserInvalid() = runTest {
        val invalidUser = contact.toUi().copy(name = "j")

        //Given
        coEvery { validator.searchFieldError(invalidUser, any()) } returns UserErrorModel(name = true)
        launch { viewModel.onDataChange(invalidUser) }
        advanceUntilIdle()
        val user = viewModel.user.value
        coEvery { validator.searchError(user) } returns UserErrorModel(name = true)

        //When
        var isValid = false
        launch { isValid = viewModel.setUsers() }
        advanceUntilIdle()

        //Then
        assertFalse(isValid)
    }

    @Test
    fun testSetUser() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchError(contactUi) } returns UserErrorModel()

        //When
        launch { viewModel.setUsers() }
        advanceUntilIdle()

        //Then
        val contactNew = viewModel.user.value
        coVerify(exactly = 1) { setUserUseCase.invoke(contactNew.toDomain()) }
    }

    @Test
    fun testOnDataChange() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchFieldError(contactUi, contactUi) } returns UserErrorModel()

        //When
        launch { viewModel.onDataChange(contactUi) }
        advanceUntilIdle()

        //Then
        val contactNew = viewModel.user.value
        assertTrue(contactUi.name == contactNew.name)
        assertTrue(contactUi.surName == contactNew.surName)
        assertTrue(contactUi.phoneNumber == contactNew.phoneNumber)
        assertTrue(contactUi.email == contactNew.email)
        assertTrue(contactUi.age == contactNew.age)
    }
}
