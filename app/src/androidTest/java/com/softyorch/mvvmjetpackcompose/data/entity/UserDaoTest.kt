package com.softyorch.mvvmjetpackcompose.data.entity

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun onBefore() {
        //Created DB
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    fun onAfter() {
        //Close DB
        db.close()
    }

    @Test
    fun insertAndGetUser() = runBlocking {
        val userID = UUID.randomUUID()
        val testContact = UserEntity(
            id = userID,
            name = "Test",
            surName = "User",
            phoneNumber = "1234567890",
            email = "test.user@example.com",
            age = 30,
            photoUri = null,
            logo = null,
            logoColor = null,
            lastCall = null,
            typeCall = null,
            favorite = false,
            phoneBlocked = false
        )
        userDao.insertUsers(testContact)

        val loaded = userDao.getUser(userID).first()

        assertEquals(testContact, loaded)
    }

}