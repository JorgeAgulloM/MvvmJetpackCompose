package com.softyorch.mvvmjetpackcompose.utils

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FakeRoomDataBase {
    fun getDB(): AppDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }
}
