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

class SetUserUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var setUserUseCase: SetUserUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setUserUseCase = SetUserUseCase(repo)
    }

    @Test
    fun `when create new contact`() = runBlocking {

        //Given
        coEvery { repo.insertUsers(testContact) } returns Unit

        //When
        setUserUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.insertUsers(testContact) }
    }
}
