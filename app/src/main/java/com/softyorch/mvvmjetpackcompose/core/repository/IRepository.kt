package com.softyorch.mvvmjetpackcompose.core.repository

import com.softyorch.mvvmjetpackcompose.core.entity.UserEntity
import kotlinx.coroutines.flow.Flow


interface IRepository {
    suspend fun getUsers(): Flow<List<UserEntity>>
    suspend fun insertUsers(users: List<UserEntity>)
}
