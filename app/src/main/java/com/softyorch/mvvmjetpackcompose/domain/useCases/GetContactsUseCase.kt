package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(): Flow<List<ContactDomain>> {
        return repo.getContacts().map { list-> list.map { it.toDomain() } }
    }
}
