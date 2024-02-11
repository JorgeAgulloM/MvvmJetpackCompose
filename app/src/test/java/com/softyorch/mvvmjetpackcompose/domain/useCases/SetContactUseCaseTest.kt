package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.utils.testContact
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SetContactUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var setContactUseCase: SetContactUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setContactUseCase = SetContactUseCase(repo)
    }

    @Test
    fun `when create new contact`() = runBlocking {

        //Given
        coEvery { repo.insertContacts(testContact) } returns Unit

        //When
        setContactUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.insertContacts(testContact) }
    }
}
