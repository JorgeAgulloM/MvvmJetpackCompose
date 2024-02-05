package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.userID
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
    private lateinit var userDao: UserDao
    private lateinit var repo: RepositoryImpl

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        repo = RepositoryImpl(userDao)
    }

    @Test
    fun `when getUsers calls userDao getUsers`() = runBlocking {
        val testList = listOf<UserEntity>()
        val testFlow = flowOf(testList)

        //Given
        coEvery { userDao.getUsers() } returns testFlow

        //When
        val result = repo.getUsers().first()

        assertEquals(testList, result)
        coVerify(exactly = 1) { userDao.getUsers() }
    }

    @Test
    fun `when getUser calls userDao getUser`() = runBlocking {
        val testFlow = flowOf(testContact)

        //Given
        coEvery { userDao.getUser(userID) } returns testFlow

        //When
        val result = repo.getUser(userID).first()

        assertEquals(testFlow.first(), result)
        coVerify(exactly = 1) { userDao.getUser(userID) }
    }

    @Test
    fun `when insertUser calls userDao insertUser`() = runBlocking {
        //Given
        coEvery { userDao.insertUsers(testContact) } returns Unit

        //When
        repo.insertUsers(testContact)

        coVerify(exactly = 1) { userDao.insertUsers(testContact) }
    }

    @Test
    fun `when updateUser calls userDao updateUser`() = runBlocking {
        //Given
        coEvery { userDao.updateUser(testContact) } returns Unit

        //When
        repo.updateUser(testContact)

        coVerify(exactly = 1) { userDao.updateUser(testContact) }
    }

    @Test
    fun `when deleteUser calls userDao deleteUser`() = runBlocking {
        //Given
        coEvery { userDao.deleteUser(testContact) } returns Unit

        //When
        repo.deleteUser(testContact)

        coVerify(exactly = 1) { userDao.deleteUser(testContact) }
    }

}
