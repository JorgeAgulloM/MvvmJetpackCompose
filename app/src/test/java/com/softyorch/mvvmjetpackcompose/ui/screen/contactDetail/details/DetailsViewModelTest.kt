package com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateContactUseCase
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
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getContactUseCase: GetContactUseCase

    @RelaxedMockK
    private lateinit var updateContactUseCase: UpdateContactUseCase

    @RelaxedMockK
    private lateinit var deleteContactUseCase: DeleteContactUseCase

    @RelaxedMockK
    private lateinit var validator: IContactValidator
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DetailsViewModel(
            getContactUseCase,
            updateContactUseCase,
            deleteContactUseCase,
            validator,
            Dispatchers.Unconfined
        )
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `test getting contact by id`() = runTest {
        val contact = testContact.toDomain()
        val contactId = contact.id

        //Given
        coEvery { getContactUseCase.invoke(contactId) } returns flowOf(contact)

        //When
        launch { viewModel.getContact(contactId) }
        advanceUntilIdle()

        //Then
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Success)
        assertEquals(contact.toUi(), (state as StateDetails.Success).contact)
        coVerify(exactly = 1) { getContactUseCase.invoke(contactId) }
    }

    @Test
    fun `test editing contact`() = runTest {
        val contact = testContact.toDomain()
        val contactId = contact.id

        //Give
        coEvery { getContactUseCase.invoke(contactId) } returns flowOf(contact)
        coEvery { validator.searchError(contact.toUi()) } returns ContactErrorModel()

        launch { viewModel.getContact(contactId) }
        advanceUntilIdle()

        val newState = viewModel.stateDetails.value
        assert(newState is StateDetails.Success)

        //When
        val newDataContact = contact.toUi().copy(name = "Geremias")
        viewModel.onDataChange(newDataContact)
        viewModel.eventManager(EventDetails.Read)

        //Then
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Success)
        assertEquals(newDataContact, (state as StateDetails.Success).contact)
        coVerify(exactly = 1) { updateContactUseCase.invoke(newDataContact.toDomain()) }
    }

    @Test
    fun `test deleting contact`() = runTest {
        val contact = testContact.toDomain()
        val contactId = contact.id

        //Given
        coEvery { getContactUseCase.invoke(contactId) } returns flowOf(contact)
        launch { viewModel.getContact(contactId) }
        advanceUntilIdle()

        val newState = viewModel.stateDetails.value
        assert(newState is StateDetails.Success)

        //When
        viewModel.eventManager(EventDetails.Delete)

        //Then
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Success)
        coVerify(exactly = 1) { deleteContactUseCase.invoke(contact) }
    }

}