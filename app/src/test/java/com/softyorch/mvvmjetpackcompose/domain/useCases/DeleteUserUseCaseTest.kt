package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.utils.testContact
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteUserUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        deleteUserUseCase = DeleteUserUseCase(repo)
    }

    @Test
    fun `when user deleting one contact`() = runBlocking {

        //Given
        coEvery { repo.deleteUser(testContact) } returns Unit

        //When
        deleteUserUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.deleteUser(testContact) }
    }
}
