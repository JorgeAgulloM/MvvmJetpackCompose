package com.softyorch.mvvmjetpackcompose.core

import android.content.Context
import androidx.room.Room
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.UserDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.DeleteUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.GetListUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.GetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.SetUserUseCase
import com.softyorch.mvvmjetpackcompose.domain.UpdateUserUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IUserValidator
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.UserValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun providesIRepository(userDao: UserDao): IRepository = RepositoryImpl(userDao)

    @Singleton
    @Provides
    fun providesGetListUserUseCase(repo: IRepository): GetListUserUseCase = GetListUserUseCase(repo)

    @Singleton
    @Provides
    fun providesGetUserUseCase(repo: IRepository): GetUserUseCase = GetUserUseCase(repo)

    @Singleton
    @Provides
    fun providesSetListUserUseCase(repo: IRepository): SetUserUseCase = SetUserUseCase(repo)

    @Singleton
    @Provides
    fun providesUpdateUserUseCase(repo: IRepository): UpdateUserUseCase = UpdateUserUseCase(repo)

    @Singleton
    @Provides
    fun providesDeleteUserUseCase(repo: IRepository): DeleteUserUseCase = DeleteUserUseCase(repo)

    @Provides
    fun providesCoroutineDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun providesUserValidator(): IUserValidator = UserValidatorImpl()

}
