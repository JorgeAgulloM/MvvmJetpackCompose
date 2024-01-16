package com.softyorch.mvvmjetpackcompose.domain

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.UserDomain.Companion.toDomain
import java.util.UUID
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(userId: UUID): UserDomain {
        return repo.getUser(userId).toDomain()
    }
}
