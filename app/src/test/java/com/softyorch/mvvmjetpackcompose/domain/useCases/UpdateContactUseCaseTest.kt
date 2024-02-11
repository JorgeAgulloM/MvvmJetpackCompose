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

class UpdateContactUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var updateContactUseCase: UpdateContactUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        updateContactUseCase = UpdateContactUseCase(repo)
    }

    @Test
    fun `when updating contact data`() = runBlocking {
        //Given
        coEvery { repo.updateContact(testContact) } returns Unit

        //When
        updateContactUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.updateContact(testContact) }
    }
}
