package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toEntity
import com.softyorch.mvvmjetpackcompose.utils.testContact
import com.softyorch.mvvmjetpackcompose.utils.textUtilsWrapper.TextUtilsWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetSearchUsersUseCaseTest {

    @RelaxedMockK
    private lateinit var repo: IRepository

    @RelaxedMockK
    private lateinit var textUtils: TextUtilsWrapper
    private lateinit var getSearchUsersUseCase: GetSearchUsersUseCase


    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getSearchUsersUseCase = GetSearchUsersUseCase(repo, textUtils)
    }

    private val testContactList = listOf(testContact)

    @Test
    fun `when searching for filtered contacts and at least one matching contact is returned`() =
        runBlocking {
            val filter = "test"

            val contactFlow = flowOf(testContactList)

            //Given
            coEvery { textUtils.isDigitsOnly(filter) } returns false
            coEvery { repo.getUsers() } returns contactFlow

            //When
            val result = getSearchUsersUseCase.invoke(filter).first().map { it.toEntity() }

            //Then
            assertEquals(testContactList, result)
            coVerify(exactly = 1) { repo.getUsers() }
        }

    @Test
    fun `when searching for filtered contacts and and no match`() =
        runBlocking {
            val filter = "myTest"

            val contactFlow = flowOf(testContactList)

            //Given
            coEvery { textUtils.isDigitsOnly(filter) } returns true
            coEvery { repo.getUsers() } returns contactFlow

            //When
            val result = getSearchUsersUseCase.invoke(filter).first().map { it.toEntity() }

            //Then
            assertFalse(contactFlow == result)
            coVerify(exactly = 1) { repo.getUsers() }
        }
}
