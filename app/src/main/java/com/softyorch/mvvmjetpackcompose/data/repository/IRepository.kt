package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID


interface IRepository {
    suspend fun getContacts(): Flow<List<ContactEntity>>
    suspend fun getContact(contactId: UUID): Flow<ContactEntity>
    suspend fun insertContacts(contacts: ContactEntity)
    suspend fun updateContact(contact: ContactEntity)
    suspend fun deleteContact(contact: ContactEntity)
}
