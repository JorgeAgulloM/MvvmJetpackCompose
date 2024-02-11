package com.softyorch.mvvmjetpackcompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
