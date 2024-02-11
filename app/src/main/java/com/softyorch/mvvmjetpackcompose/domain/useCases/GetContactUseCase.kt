package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetContactUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(contactId: UUID): Flow<ContactDomain> {
        return repo.getContact(contactId).map { it.toDomain() }
    }
}
