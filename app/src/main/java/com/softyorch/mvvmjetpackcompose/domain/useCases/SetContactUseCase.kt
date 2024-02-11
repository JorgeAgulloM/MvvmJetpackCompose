package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain
import com.softyorch.mvvmjetpackcompose.domain.models.ContactDomain.Companion.toEntity
import javax.inject.Inject

class SetContactUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(contact: ContactDomain) {
        repo.insertContacts(contact.toEntity())
    }
}
