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
        //Given
        coEvery { repo.updateUser(testContact) } returns Unit

        //When
        updateUserUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.updateUser(testContact) }
    }
}
