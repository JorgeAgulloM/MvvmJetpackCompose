package com.softyorch.mvvmjetpackcompose.integrationTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.UserValidatorImpl
import com.softyorch.mvvmjetpackcompose.ui.screen.main.newUser.CreateUserViewModel
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
class SetUserIntegrationTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var repo: IRepository
    private lateinit var setUserUseCase: SetUserUseCase
    private lateinit var validator: IUserValidator
    private lateinit var viewModel: CreateUserViewModel

    @Before
    fun onBefore() {
        db = FakeRoomDataBase().getDB()
        userDao = db.userDao()
        repo = RepositoryImpl(userDao)
        setUserUseCase = SetUserUseCase(repo)
        validator = UserValidatorImpl()
        viewModel = CreateUserViewModel(setUserUseCase, validator, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        db.close()
    }

    @Test
    fun whenCreatedNewUser_thenSaveInDB() = runTest {
        val user = testContact.toDomain().toUi()
        launch { viewModel.onDataChange(user) }
        launch { viewModel.setUsers() }
        advanceUntilIdle()

        val viewModelUser = viewModel.user.value
        assertEquals(user.name, viewModelUser.name)
        assertEquals(user.surName, viewModelUser.surName)
        assertEquals(user.phoneNumber, viewModelUser.phoneNumber)
        assertEquals(user.email, viewModelUser.email)
        assertEquals(user.age, viewModelUser.age)

        val userDb = userDao.getUser(user.id).first().toDomain().toUi()
        assertEquals(viewModelUser, userDb)
    }
}
