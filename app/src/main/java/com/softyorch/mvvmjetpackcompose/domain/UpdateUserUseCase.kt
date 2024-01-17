package com.softyorch.mvvmjetpackcompose.domain

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.UserDomain.Companion.toEntity
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(user: UserDomain) {
        repo.updateUser(user.toEntity())
    }
}
