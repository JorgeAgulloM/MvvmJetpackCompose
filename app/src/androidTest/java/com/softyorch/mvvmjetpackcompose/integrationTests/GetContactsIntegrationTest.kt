package com.softyorch.mvvmjetpackcompose.integrationTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactsUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList.ContactsListState
import com.softyorch.mvvmjetpackcompose.ui.screen.main.contactsList.ContactsListViewModel
import com.softyorch.mvvmjetpackcompose.utils.FakeRoomDataBase
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.testContact2
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class GetContactsIntegrationTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var contactDao: ContactDao
    private lateinit var repo: IRepository
    private lateinit var getContactsUseCase: GetContactsUseCase
    private lateinit var viewModel: ContactsListViewModel

    @Before
    fun onBefore() {
        db = FakeRoomDataBase().getDB()
        contactDao = db.contactDao()
        repo = RepositoryImpl(contactDao)
        getContactsUseCase = GetContactsUseCase(repo)
        viewModel = ContactsListViewModel(getContactsUseCase, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        db.close()
    }

    @Test
    fun whenStartApp_thenGetContactListFromDataBase() = runTest {
        val contactList = listOf(testContact, testContact2)
        contactList.forEach { contactDao.insertContact(it) }

        launch { viewModel.onCreate() }
        advanceUntilIdle()

        val contacts = (viewModel.uiState.value as ContactsListState.Success).contacts
        assertEquals(contactList.map { it.toDomain().toUi() }, contacts)
    }

    @Test
    fun whenStatApp_thenHasNotContacts() = runTest {
        launch { viewModel.onCreate() }
        advanceUntilIdle()

        val contacts = (viewModel.uiState.value as ContactsListState.Success).contacts
        assert(contacts == emptyList<ContactUi>())
    }
}
