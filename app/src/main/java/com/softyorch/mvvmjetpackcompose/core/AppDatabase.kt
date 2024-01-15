package com.softyorch.mvvmjetpackcompose.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softyorch.mvvmjetpackcompose.core.entity.UserDao
import com.softyorch.mvvmjetpackcompose.core.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): Flow<UserDao>
}
