package com.softyorch.mvvmjetpackcompose.domain.useCases

import androidx.core.text.isDigitsOnly
import com.softyorch.mvvmjetpackcompose.data.repository.IRepository
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain
import com.softyorch.mvvmjetpackcompose.domain.models.UserDomain.Companion.toDomain
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import com.softyorch.mvvmjetpackcompose.utils.deleteAccents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchUsersUseCase @Inject constructor(private val repo: IRepository) {
    suspend operator fun invoke(filter: String): Flow<List<UserDomain>> {
        return filterUsers(filter)
    }

    private suspend fun filterUsers(filter: String): Flow<List<UserDomain>> =
        repo.getUsers().map { list ->
            if (filter.isDigitsOnly()) {
                list.filter { it.phoneNumber.startsWith(filter) }
            } else {
                list.filter {
                    val name = it.name.lowercase().deleteAccents()
                    val processedSurName = it.surName?.let { value -> splitSurNames(value) }
                    val surName = processedSurName?.first ?: EMPTY_STRING
                    val lastName = processedSurName?.second ?: EMPTY_STRING
                    val completeName = "$name $surName $lastName"
                    val email = it.email?.lowercase()?.deleteAccents() ?: EMPTY_STRING

                    name.startsWith(filter) ||
                            surName.startsWith(filter) ||
                            lastName.startsWith(filter) ||
                            completeName.startsWith(filter) ||
                            email.startsWith(filter)
                }
            }
        }.map { listF -> listF.map { it.toDomain() } }

    private fun splitSurNames(surNames: String): Pair<String, String> {
        val surName = surNames.lowercase().deleteAccents()
        return if (" " in surName) {
            Pair(surName.split(" ")[0], surName.split(" ")[1])
        } else {
            Pair(surName, EMPTY_STRING)
        }
    }

}
