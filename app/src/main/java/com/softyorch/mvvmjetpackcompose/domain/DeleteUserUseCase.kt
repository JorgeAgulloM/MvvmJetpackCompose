package com.softyorch.mvvmjetpackcompose.domain

import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.UserDomain.Companion.toEntity
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(user: UserDomain) {
        repo.deleteUser(user.toEntity())
    }
}
