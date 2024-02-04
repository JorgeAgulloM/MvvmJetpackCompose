package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID

class UpdateUserUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var updateUserUseCase: UpdateUserUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        updateUserUseCase = UpdateUserUseCase(repo)
    }

    @Test
    fun `when updating contact data`() = runBlocking {
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

        //Given
        coEvery { repo.updateUser(testContact) } returns Unit

        //When
        updateUserUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.updateUser(testContact) }
    }
}
