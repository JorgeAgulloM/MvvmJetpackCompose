package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toEntity
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
import java.util.UUID

class GetUserUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getUserUseCase = GetUserUseCase(repo)
    }

    @Test
    fun `whe calling the use case to get one user`() = runBlocking {
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
        val contactFlow = flowOf(testContact)

        //Given
        coEvery { repo.getUser(userID) } returns contactFlow

        //When
        val result = getUserUseCase.invoke(userID).first().toEntity()

        //Then
        assertEquals(testContact, result)
        coVerify(exactly = 1) { repo.getUser(userID) }
    }
}
