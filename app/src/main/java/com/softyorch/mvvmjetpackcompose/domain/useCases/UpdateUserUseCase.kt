package com.softyorch.mvvmjetpackcompose.domain.useCases

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toEntity
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(user: UserDomain) {
        repo.updateUser(user.toEntity())
    }
}
