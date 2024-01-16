package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow


interface IRepository {
    suspend fun getUsers(): Flow<List<UserEntity>>
    suspend fun insertUsers(users: UserEntity)
}
