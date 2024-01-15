package com.softyorch.mvvmjetpackcompose.data.repository

import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val userDao: UserDao) : IRepository {
    override suspend fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getUsers()
    }

    override suspend fun insertUsers(users: List<UserEntity>) {
        userDao.insertUsers(users)
    }
}
