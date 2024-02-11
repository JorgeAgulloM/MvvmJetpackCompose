package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val contactDao: ContactDao) : IRepository {
    override suspend fun getContacts(): Flow<List<ContactEntity>> {
        return contactDao.getContacts()
    }

    override suspend fun getContact(contactId: UUID): Flow<ContactEntity> {
        return contactDao.getContact(contactId)
    }

    override suspend fun insertContacts(contacts: ContactEntity) {
        contactDao.insertContact(contacts)
    }

    override suspend fun updateContact(contact: ContactEntity) {
        contactDao.updateContact(contact)
    }

    override suspend fun deleteContact(contact: ContactEntity) {
        contactDao.deleteContact(contact)
    }
}
