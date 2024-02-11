package com.softyorch.mvvmjetpackcompose.ui.screen.main.newContact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetContactUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IContactValidator
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
class CreateContactViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var setContactUseCase: SetContactUseCase

    @RelaxedMockK
    private lateinit var validator: IContactValidator

    private lateinit var viewModel: CreateContactViewModel

    private val contact = testContact.toDomain()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = CreateContactViewModel(setContactUseCase, validator, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        clearAllMocks()
    }

    @Test
    fun testContactValid() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchFieldError(contactUi, any()) } returns ContactErrorModel()
        launch { viewModel.onDataChange(contactUi) }
        advanceUntilIdle()
        val contact = viewModel.contact.value
        coEvery { validator.searchError(contact) } returns ContactErrorModel()

        //When
        var isValid = false
        launch { isValid = viewModel.setContacts() }
        advanceUntilIdle()

        //Then
        assertTrue(isValid)
    }

    @Test
    fun testContactInvalid() = runTest {
        val invalidContact = contact.toUi().copy(name = "j")

        //Given
        coEvery { validator.searchFieldError(invalidContact, any()) } returns ContactErrorModel(name = true)
        launch { viewModel.onDataChange(invalidContact) }
        advanceUntilIdle()
        val contact = viewModel.contact.value
        coEvery { validator.searchError(contact) } returns ContactErrorModel(name = true)

        //When
        var isValid = false
        launch { isValid = viewModel.setContacts() }
        advanceUntilIdle()

        //Then
        assertFalse(isValid)
    }

    @Test
    fun testSetContact() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchError(contactUi) } returns ContactErrorModel()

        //When
        launch { viewModel.setContacts() }
        advanceUntilIdle()

        //Then
        val contactNew = viewModel.contact.value
        coVerify(exactly = 1) { setContactUseCase.invoke(contactNew.toDomain()) }
    }

    @Test
    fun testOnDataChange() = runTest {
        val contactUi = contact.toUi()
        //Given
        coEvery { validator.searchFieldError(contactUi, contactUi) } returns ContactErrorModel()

        //When
        launch { viewModel.onDataChange(contactUi) }
        advanceUntilIdle()

        //Then
        val contactNew = viewModel.contact.value
        assertTrue(contactUi.name == contactNew.name)
        assertTrue(contactUi.surName == contactNew.surName)
        assertTrue(contactUi.phoneNumber == contactNew.phoneNumber)
        assertTrue(contactUi.email == contactNew.email)
        assertTrue(contactUi.age == contactNew.age)
    }
}
