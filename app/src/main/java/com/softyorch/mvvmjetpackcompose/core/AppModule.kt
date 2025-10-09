package com.softyorch.mvvmjetpackcompose.core

import android.content.Context
import androidx.room.Room
import com.softyorch.mvvmjetpackcompose.data.AppDatabase
import com.softyorch.mvvmjetpackcompose.data.entity.ContactDao
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.data.repository.RepositoryImpl
import com.softyorch.mvvmjetpackcompose.domain.useCases.DeleteContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactsUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetSearchContactsUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.GetContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.SetContactUseCase
import com.softyorch.mvvmjetpackcompose.domain.useCases.UpdateContactUseCase
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.IContactValidator
import com.softyorch.mvvmjetpackcompose.ui.models.errorValidator.ContactValidatorImpl
import com.softyorch.mvvmjetpackcompose.utils.textUtilsWrapper.TextUtilsWrapper
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
            .fallbackToDestructiveMigration(false)
            .build()

    @Singleton
    @Provides
    fun providesContactDao(appDatabase: AppDatabase): ContactDao = appDatabase.contactDao()

    @Singleton
    @Provides
    fun providesIRepository(contactDao: ContactDao): IRepository = RepositoryImpl(contactDao)

    @Singleton
    @Provides
    fun providesGetContactsUseCase(repo: IRepository): GetContactsUseCase = GetContactsUseCase(repo)

    @Singleton
    @Provides
    fun providesGetContactUseCase(repo: IRepository): GetContactUseCase = GetContactUseCase(repo)

    @Singleton
    @Provides
    fun providesSetContactUseCase(repo: IRepository): SetContactUseCase = SetContactUseCase(repo)

    @Singleton
    @Provides
    fun providesUpdateContactUseCase(repo: IRepository): UpdateContactUseCase = UpdateContactUseCase(repo)

    @Singleton
    @Provides
    fun providesDeleteContactUseCase(repo: IRepository): DeleteContactUseCase = DeleteContactUseCase(repo)

    @Provides
    fun providesCoroutineDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun providesContactValidator(): IContactValidator = ContactValidatorImpl()

    @Singleton
    @Provides
    fun providesTextUtilsWrapper(): TextUtilsWrapper = TextUtilsWrapper()

    @Singleton
    @Provides
    fun providesGetSearchContactsUseCase(
        repo: IRepository,
        textUtils: TextUtilsWrapper
    ): GetSearchContactsUseCase = GetSearchContactsUseCase(repo, textUtils)

}
