package com.softyorch.mvvmjetpackcompose.utils

import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImplTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteUserUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetListUserUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetSearchUsersUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetUserUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetUserUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateUserUseCaseTest
import com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList.UsersListState
import com.softyorch.mvvmjetpackcompose.ui.screen.main.usersList.UsersListViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DeleteUserUseCaseTest::class,
    GetListUserUseCaseTest::class,
    GetSearchUsersUseCaseTest::class,
    GetUserUseCaseTest::class,
    SetUserUseCaseTest::class,
    UpdateUserUseCaseTest::class,
    RepositoryImplTest::class,
    UsersListViewModelTest::class
)
class MainTestSuite