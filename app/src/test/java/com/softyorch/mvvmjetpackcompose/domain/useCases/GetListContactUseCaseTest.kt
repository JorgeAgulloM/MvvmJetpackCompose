package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toEntity
import com.softyorch.mvvmjetpackcompose.utils.testContact
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

class GetListContactUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository
    private lateinit var getContactsUseCase: GetContactsUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getContactsUseCase = GetContactsUseCase(repo)
    }

    @Test
    fun `when get contacts list`() = runBlocking {
        val testContactList = listOf(testContact)
        val contactFlow = flowOf(testContactList)

        //Given
        coEvery { repo.getContacts() } returns contactFlow

        //When
        val result = getContactsUseCase.invoke().map { list -> list.map { it.toEntity() } }

        //Then
        assertEquals(contactFlow.first(), result.first())
        coVerify(exactly = 1) { repo.getContacts() }
    }
}
