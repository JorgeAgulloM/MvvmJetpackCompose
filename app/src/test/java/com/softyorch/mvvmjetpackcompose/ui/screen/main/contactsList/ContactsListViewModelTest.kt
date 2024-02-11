package com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactsUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.utils.testContact
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ContactsListViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getContactsUseCase: GetContactsUseCase
    private lateinit var viewModel: ContactsListViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = ContactsListViewModel(getContactsUseCase, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        clearAllMocks()
    }

    @Test
    fun `test get contacts from use case`() = runTest {
        val contactList = listOf(testContact.toDomain())
        val flowList = flowOf(contactList)

        //Given
        coEvery { getContactsUseCase.invoke() } returns flowList

        //When
        launch { viewModel.onCreate() }
        advanceUntilIdle()

        //Then
        val state = viewModel.uiState.value
        assertTrue(state is ContactsListState.Success)
        assertEquals(contactList.map { it.toUi() }, (state as ContactsListState.Success).contacts)
    }

    @Test
    fun `test get error from use case`() = runTest {
        val message = "Error obteniendo usuarios"
        //Given
        coEvery { getContactsUseCase.invoke() } returns flow { throw Exception(message) }

        //When
        launch { viewModel.onCreate() }
        advanceUntilIdle()

        //Then
        val state = viewModel.uiState.value
        assertTrue(state is ContactsListState.Error)
        assertEquals(message, (state as ContactsListState.Error).msg)
    }
}
