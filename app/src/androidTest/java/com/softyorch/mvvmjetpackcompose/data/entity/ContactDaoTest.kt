package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.utils.FakeRoomDataBase
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.testContact2
import com.softyorch.mvvmjetpackcompose.utils.contactID
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactDaoTest {

    private lateinit var contactDao: ContactDao
    private lateinit var db: AppDatabase

    @Before
    fun onBefore() {
        //Created DB
        db = FakeRoomDataBase().getDB()
        contactDao = db.contactDao()
    }

    @After
    fun onAfter() {
        //Close DB
        db.close()
    }

    @Test
    fun testGetContacts() = runBlocking {
        contactDao.insertContact(testContact)
        contactDao.insertContact(testContact2)

        val result = contactDao.getContacts().first()

        assertEquals(listOf(testContact, testContact2), result)
    }

    @Test
    fun testGetContact() = runBlocking {
        contactDao.insertContact(testContact)

        val loaded = contactDao.getContact(contactID).first()

        assertEquals(testContact, loaded)
    }

    @Test
    fun testInsertContact() = runBlocking {
        contactDao.insertContact(testContact)

        val loaded = contactDao.getContact(contactID).first()

        assertEquals(testContact, loaded)
    }

    @Test
    fun testUpdateContact() = runBlocking {
        contactDao.insertContact(testContact)

        val updateContact = testContact.copy(name = "Jorge")
        contactDao.updateContact(updateContact)

        val loaded = contactDao.getContact(contactID).first()

        assertEquals(updateContact, loaded)
    }

    @Test
    fun testDeleteContact() = runBlocking {
        contactDao.insertContact(testContact)
        contactDao.deleteContact(testContact)

        val result = contactDao.getContact(contactID).firstOrNull()

        assertNull(result)
    }

}