package com.softyorch.mvvmjetpackcompose.core

import android.content.Context
import androidx.room.Room
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.GetListUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.SetListUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "MyDatabase"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Singleton
    @Provides
    fun providesGetListUserUseCase(repo: IRepository): GetListUserUseCase = GetListUserUseCase(repo)

    @Singleton
    @Provides
    fun providesSetListUserUseCase(repo: IRepository): SetListUserUseCase = SetListUserUseCase(repo)

}
