package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(userId: UUID): Flow<UserDomain> {
        return repo.getUser(userId).map { it.toDomain() }
    }
}
