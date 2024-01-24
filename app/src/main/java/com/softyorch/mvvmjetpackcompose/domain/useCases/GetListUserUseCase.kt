package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetListUserUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(): Flow<List<UserDomain>> {
        return repo.getUsers().map { list-> list.map { it.toDomain() } }
    }
}
