package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID


interface IRepository {
    suspend fun getUsers(): Flow<List<UserEntity>>
    suspend fun getUser(userId: UUID): Flow<UserEntity>
    suspend fun insertUsers(users: UserEntity)
    suspend fun updateUser(user: UserEntity)
    suspend fun deleteUser(user: UserEntity)
}
