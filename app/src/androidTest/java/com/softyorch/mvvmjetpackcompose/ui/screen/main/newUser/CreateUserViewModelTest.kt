package com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
import com.softyorch.mvvmjetpackcompose.utils.testContact
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CreateUserViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var setUserUseCase: SetUserUseCase

    @RelaxedMockK
    private lateinit var validator: IUserValidator

    private lateinit var viewModel: CreateUserViewModel

    private val contact = testContact.toDomain()

    @get:Rule
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = CreateUserViewModel(setUserUseCase, validator, Dispatchers.Unconfined)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUserValid() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchError(contactUi, contactUi) } returns UserErrorModel()

        //When
        val isValid = viewModel.setUsers()

        //Then
        assertTrue(isValid)
    }

    @Test
    fun testSetUser() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchError(contactUi, contactUi) } returns UserErrorModel()

        //When
        viewModel.setUsers()

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
        viewModel.onDataChange(contactUi)

        //Then
        val contactNew = viewModel.user.value
        assertTrue(contactUi.name == contactNew.name)
        assertTrue(contactUi.surName == contactNew.surName)
        assertTrue(contactUi.phoneNumber == contactNew.phoneNumber)
        assertTrue(contactUi.email == contactNew.email)
        assertTrue(contactUi.age == contactNew.age)
    }
}
