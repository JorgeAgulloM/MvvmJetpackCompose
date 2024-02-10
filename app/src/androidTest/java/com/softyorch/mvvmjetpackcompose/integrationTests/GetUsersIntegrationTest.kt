package com.softyorch.mvvmjetpackcompose.integrationTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetListUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList.UsersListState
import com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList.UsersListViewModel
import com.softyorch.mvvmjetpackcompose.utils.FakeRoomDataBase
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.testContact2
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class GetUsersIntegrationTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var repo: IRepository
    private lateinit var getListUserUseCase: GetListUserUseCase
    private lateinit var viewModel: UsersListViewModel

    @Before
    fun onBefore() {
        db = FakeRoomDataBase().getDB()
        userDao = db.userDao()
        repo = RepositoryImpl(userDao)
        getListUserUseCase = GetListUserUseCase(repo)
        viewModel = UsersListViewModel(getListUserUseCase, Dispatchers.Unconfined)
    }

    @Test
    fun whenStartApp_thenGetUserListFromDataBase() = runTest {
        val userList = listOf(testContact, testContact2)
        userList.forEach { userDao.insertUsers(it) }

        launch { viewModel.onCreate() }
        advanceUntilIdle()

        val users = (viewModel.uiState.value as UsersListState.Success).users
        assertEquals(userList.map { it.toDomain().toUi() }, users)
    }

    @Test
    fun whenStatApp_thenHasNotUsers() = runTest {
        launch { viewModel.onCreate() }
        advanceUntilIdle()

        val users = (viewModel.uiState.value as UsersListState.Success).users
        assert(users == emptyList<UserUi>())
    }
}
