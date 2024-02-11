package com.softyorch.mvvmjetpackcompose

import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImplTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteContactUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetListContactUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetSearchContactsUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetContactUseCaseTest
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateContactUseCaseTest
import com.softyorch.mvvmjetpackcompose.ui.screen.main.newContact.CreateContactViewModelTest
import com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList.ContactsListViewModelTest
import com.softyorch.mvvmjetpackcompose.ui.screen.search.SearchViewModelTest
import com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details.DetailsViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    DeleteContactUseCaseTest::class,
    GetListContactUseCaseTest::class,
    GetSearchContactsUseCaseTest::class,
    GetContactUseCaseTest::class,
    SetContactUseCaseTest::class,
    UpdateContactUseCaseTest::class,
    RepositoryImplTest::class,
    CreateContactViewModelTest::class,
    ContactsListViewModelTest::class,
    SearchViewModelTest::class,
    DetailsViewModelTest::class
)
class MainTestSuite