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

class DeleteContactUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var deleteContactUseCase: DeleteContactUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        deleteContactUseCase = DeleteContactUseCase(repo)
    }

    @Test
    fun `when contact deleting one contact`() = runBlocking {

        //Given
        coEvery { repo.deleteContact(testContact) } returns Unit

        //When
        deleteContactUseCase.invoke(testContact.toDomain())

        //Then
        coVerify(exactly = 1) { repo.deleteContact(testContact) }
    }
}
