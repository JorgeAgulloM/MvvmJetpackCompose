package com.softyorch.mvvmjetpackcompose.ui.models.errorManager

import androidx.core.text.isDigitsOnly
import java.util.regex.Pattern
import javax.inject.Inject

class UserValidatorImpl @Inject constructor() : IUserValidator {
    override fun isNameCorrect(name: String, size: Int): Boolean {
        return name.length > size && !name.isDigitsOnly()
    }

    override fun isPhoneNumberCorrect(phoneNumber: String): Boolean {
        return phoneNumber.isNotEmpty() && phoneNumber.isDigitsOnly()
    }

    override fun isEmailCorrect(email: String): Boolean {
        val regex = "^[A-Za-z](.*)('@')('+')(\\.)('+')"
        val pattern: Pattern = Pattern.compile(regex)
        return pattern.matcher(email).matches()
    }

    override fun isAgeCorrect(age: String): Boolean {
        return try {
            if (age.isEmpty()) return false
            if (!age.isDigitsOnly()) return false
            if (age[0].toString() == "0") return false
            val intAge = age.toInt()
            intAge in 1..110
        } catch (e: Exception) {
            false
        }
    }
}
