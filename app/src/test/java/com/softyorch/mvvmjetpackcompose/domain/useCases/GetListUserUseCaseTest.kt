package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toEntity
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
        val testContactList = listOf(testContact)
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
