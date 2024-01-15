package com.softyorch.mvvmjetpackcompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
