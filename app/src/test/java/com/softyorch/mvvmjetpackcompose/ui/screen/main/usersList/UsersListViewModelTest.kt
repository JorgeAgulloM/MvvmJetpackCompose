package com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetListUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class UsersListViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getListUserUseCase: GetListUserUseCase
    private lateinit var viewModel: UsersListViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = UsersListViewModel(getListUserUseCase, Dispatchers.Unconfined)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `test get users from use case`() = runTest {
        val contactList = listOf(testContact.toDomain())
        val flowList = flowOf(contactList)

        //Given
        coEvery { getListUserUseCase.invoke() } returns flowList

        //When
        viewModel.onCreate()

        //Then
        val state = viewModel.uiState.value
        assertTrue(state is UsersListState.Success)
        assertEquals(contactList.map { it.toUi() }, (state as UsersListState.Success).users)
    }

    @Test
    fun `test get error from use case`() = runTest {
        val message = "Error obteniendo usuarios"
        //Given
        coEvery { getListUserUseCase.invoke() } returns flow { throw Exception(message) }

        //When
        viewModel.onCreate()

        //Then
        val state = viewModel.uiState.value
        assertTrue(state is UsersListState.Error)
        assertEquals(message, (state as UsersListState.Error).msg)
    }
}
