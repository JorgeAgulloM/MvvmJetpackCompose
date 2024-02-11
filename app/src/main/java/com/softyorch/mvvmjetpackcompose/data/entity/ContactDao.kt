package com.softyorch.mvvmjetpackcompose.data.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact_table")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contact_table WHERE id=:contactId")
    fun getContact(contactId: UUID): Flow<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contacts: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)
}
