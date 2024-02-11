package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.contactID
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryImplTest {

    @RelaxedMockK
    private lateinit var contactDao: ContactDao
    private lateinit var repo: RepositoryImpl

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        repo = RepositoryImpl(contactDao)
    }

    @Test
    fun `when getContacts calls contactDao getContacts`() = runBlocking {
        val testList = listOf<ContactEntity>()
        val testFlow = flowOf(testList)

        //Given
        coEvery { contactDao.getContacts() } returns testFlow

        //When
        val result = repo.getContacts().first()

        assertEquals(testList, result)
        coVerify(exactly = 1) { contactDao.getContacts() }
    }

    @Test
    fun `when getContact calls contactDao getContact`() = runBlocking {
        val testFlow = flowOf(testContact)

        //Given
        coEvery { contactDao.getContact(contactID) } returns testFlow

        //When
        val result = repo.getContact(contactID).first()

        assertEquals(testFlow.first(), result)
        coVerify(exactly = 1) { contactDao.getContact(contactID) }
    }

    @Test
    fun `when insertContact calls contactDao insertContact`() = runBlocking {
        //Given
        coEvery { contactDao.insertContact(testContact) } returns Unit

        //When
        repo.insertContacts(testContact)

        coVerify(exactly = 1) { contactDao.insertContact(testContact) }
    }

    @Test
    fun `when updateContact calls contactDao updateContact`() = runBlocking {
        //Given
        coEvery { contactDao.updateContact(testContact) } returns Unit

        //When
        repo.updateContact(testContact)

        coVerify(exactly = 1) { contactDao.updateContact(testContact) }
    }

    @Test
    fun `when deleteContact calls contactDao deleteContact`() = runBlocking {
        //Given
        coEvery { contactDao.deleteContact(testContact) } returns Unit

        //When
        repo.deleteContact(testContact)

        coVerify(exactly = 1) { contactDao.deleteContact(testContact) }
    }

}
