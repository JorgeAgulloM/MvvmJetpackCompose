package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.utils.FakeRoomDataBase
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.testContact2
import com.softyorch.mvvmjetpackcompose.utils.userID
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
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun onBefore() {
        //Created DB
        db = FakeRoomDataBase().getDB()
        userDao = db.userDao()
    }

    @After
    fun onAfter() {
        //Close DB
        db.close()
    }

    @Test
    fun testGetUsers() = runBlocking {
        userDao.insertUsers(testContact)
        userDao.insertUsers(testContact2)

        val result = userDao.getUsers().first()

        assertEquals(listOf(testContact, testContact2), result)
    }

    @Test
    fun testGetUser() = runBlocking {
        userDao.insertUsers(testContact)

        val loaded = userDao.getUser(userID).first()

        assertEquals(testContact, loaded)
    }

    @Test
    fun testInsertUser() = runBlocking {
        userDao.insertUsers(testContact)

        val loaded = userDao.getUser(userID).first()

        assertEquals(testContact, loaded)
    }

    @Test
    fun testUpdateUser() = runBlocking {
        userDao.insertUsers(testContact)

        val updateUSer = testContact.copy(name = "Jorge")
        userDao.updateUser(updateUSer)

        val loaded = userDao.getUser(userID).first()

        assertEquals(updateUSer, loaded)
    }

    @Test
    fun testDeleteUser() = runBlocking {
        userDao.insertUsers(testContact)
        userDao.deleteUser(testContact)

        val result = userDao.getUser(userID).firstOrNull()

        assertNull(result)
    }

}