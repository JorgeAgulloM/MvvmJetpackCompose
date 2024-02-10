package com.softyorch.mvvmjetpackcompose.integrationTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.UserValidatorImpl
import com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details.DetailsViewModel
import com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details.EventDetails
import com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details.StateDetails
import com.softyorch.mvvmjetpackcompose.utils.FakeRoomDataBase
import com.softyorch.mvvmjetpackcompose.utils.testContact
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DetailsUserCrudIntegrationTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var repo: IRepository
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var validator: IUserValidator
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun onBefore() {
        db = FakeRoomDataBase().getDB()
        userDao = db.userDao()
        repo = RepositoryImpl(userDao)
        getUserUseCase = GetUserUseCase(repo)
        updateUserUseCase = UpdateUserUseCase(repo)
        deleteUserUseCase = DeleteUserUseCase(repo)
        validator = UserValidatorImpl()
        viewModel = DetailsViewModel(
            getUserUseCase,
            updateUserUseCase,
            deleteUserUseCase,
            validator,
            Dispatchers.Unconfined
        )
    }

    @After
    fun onAfter() {
        db.close()
    }

    @Test
    fun whenShownUserDetails_thenGetUserDetailsFromDB() = runTest {
        val user = testContact
        userDao.insertUsers(user)

        launch { viewModel.getUSer(user.id) }
        advanceUntilIdle()

        val viewModelUser = (viewModel.stateDetails.value as StateDetails.Success).user
        assertEquals(user.toDomain().toUi(), viewModelUser)
    }

    @Test
    fun whenEditDataOfShownUser_thenUpdateDbAndViewUpdate() = runTest {
        val user = testContact
        userDao.insertUsers(user)

        launch { viewModel.getUSer(user.id) }
        advanceUntilIdle()

        val viewModelUser = (viewModel.stateDetails.value as StateDetails.Success).user
        val updateUser = viewModelUser.copy(name = "NewTestUser")
        launch { viewModel.onDataChange(updateUser) }
        launch { viewModel.eventManager(EventDetails.Read) }
        advanceUntilIdle()

        val userDb = userDao.getUser(user.id).first().toDomain().toUi()
        assertEquals(updateUser, userDb)
    }

    @Test
    fun whenDeleteShownUser() = runTest {
        val user = testContact
        userDao.insertUsers(user)

        launch { viewModel.getUSer(user.id) }
        launch { viewModel.eventManager(EventDetails.Delete) }
        advanceUntilIdle()
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Error)
    }
}
