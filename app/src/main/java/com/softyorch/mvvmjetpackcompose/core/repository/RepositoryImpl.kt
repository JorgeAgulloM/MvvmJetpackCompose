package com.softyorch.mvvmjetpackcompose.core.repository

import com.softyorch.mvvmjetpackcompose.core.entity.UserDao
import com.softyorch.mvvmjetpackcompose.core.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val userDao: UserDao) : IRepository {
    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return flow {
            emit(userDao.getUsers())
        }
    }

    override suspend fun insertUsers(users: List<UserEntity>) {
        userDao.insertUsers(users)
    }
}
