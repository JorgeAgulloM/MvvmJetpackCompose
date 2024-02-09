package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateUserUseCase
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
    private lateinit var getUserUseCase: GetUserUseCase

    @RelaxedMockK
    private lateinit var updateUserUseCase: UpdateUserUseCase

    @RelaxedMockK
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    @RelaxedMockK
    private lateinit var validator: IUserValidator
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DetailsViewModel(
            getUserUseCase,
            updateUserUseCase,
            deleteUserUseCase,
            validator,
            Dispatchers.Unconfined
        )
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `test getting user by id`() = runTest {
        val contact = testContact.toDomain()
        val userId = contact.id

        //Given
        coEvery { getUserUseCase.invoke(userId) } returns flowOf(contact)

        //When
        launch { viewModel.getUSer(userId) }
        advanceUntilIdle()

        //Then
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Success)
        assertEquals(contact.toUi(), (state as StateDetails.Success).user)
        coVerify(exactly = 1) { getUserUseCase.invoke(userId) }
    }

    @Test
    fun `test editing user`() = runTest {
        val contact = testContact.toDomain()
        val userId = contact.id

        //Give
        coEvery { getUserUseCase.invoke(userId) } returns flowOf(contact)
        coEvery { validator.searchError(contact.toUi()) } returns UserErrorModel()

        launch { viewModel.getUSer(userId) }
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
        assertEquals(newDataContact, (state as StateDetails.Success).user)
        coVerify(exactly = 1) { updateUserUseCase.invoke(newDataContact.toDomain()) }
    }

    @Test
    fun `test deleting user`() = runTest {
        val contact = testContact.toDomain()
        val userId = contact.id

        //Given
        coEvery { getUserUseCase.invoke(userId) } returns flowOf(contact)
        launch { viewModel.getUSer(userId) }
        advanceUntilIdle()

        val newState = viewModel.stateDetails.value
        assert(newState is StateDetails.Success)

        //When
        viewModel.eventManager(EventDetails.Delete)

        //Then
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Success)
        coVerify(exactly = 1) { deleteUserUseCase.invoke(contact) }
    }

}