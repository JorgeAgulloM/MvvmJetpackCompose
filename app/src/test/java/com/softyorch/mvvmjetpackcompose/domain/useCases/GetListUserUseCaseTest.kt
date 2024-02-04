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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID

class GetListUserUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var getListUserUseCase: GetListUserUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getListUserUseCase = GetListUserUseCase(repo)
    }

    @Test
    fun `when get users list`() = runBlocking {
        val userID = UUID.randomUUID()
        val testContactList = listOf(
            UserEntity(
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
        )
        val contactFlow = flowOf(testContactList)

        //Given
        coEvery { repo.getUsers() } returns contactFlow

        //When
        val result = getListUserUseCase.invoke().map { list -> list.map { it.toEntity() } }

        //Then
        assertEquals(contactFlow.first(), result.first())
        coVerify(exactly = 1) { repo.getUsers() }
    }
}
