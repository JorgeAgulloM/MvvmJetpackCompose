package com.softyorch.mvvmjetpackcompose.ui.models.errorValidator

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.softyorch.mvvmjetpackcompose.ui.models.UserErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.UserUi
import com.softyorch.mvvmjetpackcompose.utils.MIN_NAME_LENGTH
import javax.inject.Inject

class UserValidatorImpl @Inject constructor() : IUserValidator {

    private val userError: UserErrorModel = UserErrorModel()

    override fun searchError(user: UserUi, oldDataUser: UserUi): UserErrorModel {

        val errorName = !isNameCorrect(user.name, MIN_NAME_LENGTH)
        val errorSurName = user.surName?.let {
            if (it.isEmpty()) false else !isNameCorrect(it, MIN_NAME_LENGTH)
        } ?: false
        val errorPhoneNumber = !isPhoneNumberCorrect(user.phoneNumber)
        val errorEmail =
            user.email?.let { if (it.isEmpty()) false else !isEmailCorrect(it) } ?: false
        val errorAge =
            user.age?.let { if (it.isEmpty()) false else !isAgeCorrect(it) } ?: false

        return userError.copy(
            name = errorName,
            surName = errorSurName,
            phoneNumber = errorPhoneNumber,
            email = errorEmail,
            age = errorAge
        )
    }

    override fun searchFieldError(user: UserUi, oldDataUser: UserUi): UserErrorModel {

        if (user.name != oldDataUser.name)
            return userError.copy(name = !isNameCorrect(user.name, MIN_NAME_LENGTH))

        if (user.surName != oldDataUser.surName)
            return userError.copy(surName = user.surName?.let {
                if (it.isEmpty()) false else !isNameCorrect(it, MIN_NAME_LENGTH)
            } ?: false)

        if (user.phoneNumber != oldDataUser.phoneNumber)
            return userError.copy(phoneNumber = !isPhoneNumberCorrect(user.phoneNumber))

        if (user.email != oldDataUser.email)
            return userError.copy(email = user.email?.let {
                if (it.isEmpty()) false else !isEmailCorrect(it)
            } ?: false)

        if (user.age != oldDataUser.age)
            return userError.copy(age = user.age?.let {
                if (it.isEmpty()) false else !isAgeCorrect(it)
            } ?: false)

        return userError
    }

    private fun isNameCorrect(name: String, size: Int): Boolean {
        return (name.length >= size && !name.isDigitsOnly())
    }

    private fun isPhoneNumberCorrect(phoneNumber: String): Boolean {
        return phoneNumber.isNotEmpty() && phoneNumber.isDigitsOnly()
    }

    private fun isEmailCorrect(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isAgeCorrect(age: String): Boolean {
        return try {
            if (age.isEmpty()) return false
            if (!age.isDigitsOnly()) return false
            if (age[0].toString() <= "0") return false
            val intAge = age.toInt()
            intAge in 1..110
        } catch (e: Exception) {
            false
        }
    }
}
