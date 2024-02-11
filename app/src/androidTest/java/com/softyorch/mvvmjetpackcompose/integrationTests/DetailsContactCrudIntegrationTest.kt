package com.softyorch.mvvmjetpackcompose.integrationTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateContactUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi.Companion.toUi
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IContactValidator
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.ContactValidatorImpl
import com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details.DetailsViewModel
import com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details.EventDetails
import com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details.StateDetails
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
class DetailsContactCrudIntegrationTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var contactDao: ContactDao
    private lateinit var repo: IRepository
    private lateinit var getContactUseCase: GetContactUseCase
    private lateinit var updateContactUseCase: UpdateContactUseCase
    private lateinit var deleteContactUseCase: DeleteContactUseCase
    private lateinit var validator: IContactValidator
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun onBefore() {
        db = FakeRoomDataBase().getDB()
        contactDao = db.contactDao()
        repo = RepositoryImpl(contactDao)
        getContactUseCase = GetContactUseCase(repo)
        updateContactUseCase = UpdateContactUseCase(repo)
        deleteContactUseCase = DeleteContactUseCase(repo)
        validator = ContactValidatorImpl()
        viewModel = DetailsViewModel(
            getContactUseCase,
            updateContactUseCase,
            deleteContactUseCase,
            validator,
            Dispatchers.Unconfined
        )
    }

    @After
    fun onAfter() {
        db.close()
    }

    @Test
    fun whenShownContactDetails_thenGetContactDetailsFromDB() = runTest {
        val contact = testContact
        contactDao.insertContact(contact)

        launch { viewModel.getContact(contact.id) }
        advanceUntilIdle()

        val viewModelContact = (viewModel.stateDetails.value as StateDetails.Success).contact
        assertEquals(contact.toDomain().toUi(), viewModelContact)
    }

    @Test
    fun whenEditDataOfShownContact_thenUpdateDbAndViewUpdate() = runTest {
        val contact = testContact
        contactDao.insertContact(contact)

        launch { viewModel.getContact(contact.id) }
        advanceUntilIdle()

        val viewModelContact = (viewModel.stateDetails.value as StateDetails.Success).contact
        val updateContact = viewModelContact.copy(name = "NewTestContact")
        launch { viewModel.onDataChange(updateContact) }
        launch { viewModel.eventManager(EventDetails.Read) }
        advanceUntilIdle()

        val contactDb = contactDao.getContact(contact.id).first().toDomain().toUi()
        assertEquals(updateContact, contactDb)
    }

    @Test
    fun whenDeleteShownContact() = runTest {
        val contact = testContact
        contactDao.insertContact(contact)

        launch { viewModel.getContact(contact.id) }
        launch { viewModel.eventManager(EventDetails.Delete) }
        advanceUntilIdle()
        val state = viewModel.stateDetails.value
        assert(state is StateDetails.Error)
    }
}
