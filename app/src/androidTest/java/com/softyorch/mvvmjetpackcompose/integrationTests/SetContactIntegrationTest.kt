package com.softyorch.mvvmjetpackcompose.integrationTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetContactUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IContactValidator
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.ContactValidatorImpl
import com.softyorch.mvvmjetpackcompose.ui.screen.main.newContact.CreateContactViewModel
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
class SetContactIntegrationTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var contactDao: ContactDao
    private lateinit var repo: IRepository
    private lateinit var setContactUseCase: SetContactUseCase
    private lateinit var validator: IContactValidator
    private lateinit var viewModel: CreateContactViewModel

    @Before
    fun onBefore() {
        db = FakeRoomDataBase().getDB()
        contactDao = db.contactDao()
        repo = RepositoryImpl(contactDao)
        setContactUseCase = SetContactUseCase(repo)
        validator = ContactValidatorImpl()
        viewModel = CreateContactViewModel(setContactUseCase, validator, Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        db.close()
    }

    @Test
    fun whenCreatedNewContact_thenSaveInDB() = runTest {
        val contact = testContact.toDomain().toUi()
        launch { viewModel.onDataChange(contact) }
        launch { viewModel.setContacts() }
        advanceUntilIdle()

        val viewModelContact = viewModel.contact.value
        assertEquals(contact.name, viewModelContact.name)
        assertEquals(contact.surName, viewModelContact.surName)
        assertEquals(contact.phoneNumber, viewModelContact.phoneNumber)
        assertEquals(contact.email, viewModelContact.email)
        assertEquals(contact.age, viewModelContact.age)

        val contactDb = contactDao.getContact(contact.id).first().toDomain().toUi()
        assertEquals(viewModelContact, contactDb)
    }
}
