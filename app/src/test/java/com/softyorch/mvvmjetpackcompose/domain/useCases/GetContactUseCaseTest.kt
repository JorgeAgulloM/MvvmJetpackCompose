package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toEntity
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

class GetContactUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var getContactUseCase: GetContactUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getContactUseCase = GetContactUseCase(repo)
    }

    @Test
    fun `whe calling the use case to get one contact`() = runBlocking {
        val contactID = UUID.randomUUID()
        val testContact = ContactEntity(
            id = contactID,
            name = "Test",
            surName = "Contact",
            phoneNumber = "1234567890",
            email = "test.contact@example.com",
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
        coEvery { repo.getContact(contactID) } returns contactFlow

        //When
        val result = getContactUseCase.invoke(contactID).first().toEntity()

        //Then
        assertEquals(testContact, result)
        coVerify(exactly = 1) { repo.getContact(contactID) }
    }
}
