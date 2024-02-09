package com.softyorch.mvvmjetpackcompose.ui.screen.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetSearchUsersUseCase
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var searchUsersUseCase: GetSearchUsersUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        viewModel = SearchViewModel(searchUsersUseCase, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        clearAllMocks()
    }

    @Test
    fun `test searching users with filter and get list users`() = runTest {
        val contactList = listOf(testContact.toDomain())
        val flowList = flowOf(contactList)
        val filter = "jorge"

        //Given
        coEvery { searchUsersUseCase.invoke(filter) } returns flowList

        //When
        launch { viewModel.searchEvent(filter) }
        advanceUntilIdle()
        //Then
        val stateFilter = viewModel.stateFilter.value
        assertTrue(stateFilter is StateFilter.Find)
        assertEquals(contactList.map { it.toUi() }, (stateFilter as StateFilter.Find).users)
    }

    @Test
    fun `test searching users with filter but get empty list`() = runTest {
        val filter = "Antonio"

        //Given
        coEvery { searchUsersUseCase.invoke(filter) } returns emptyFlow()

        //When
        launch { viewModel.searchEvent(filter) }
        advanceUntilIdle()

        //Then
        val stateFilter = viewModel.stateFilter.value
        assertTrue(stateFilter is StateFilter.Empty)
    }
}
