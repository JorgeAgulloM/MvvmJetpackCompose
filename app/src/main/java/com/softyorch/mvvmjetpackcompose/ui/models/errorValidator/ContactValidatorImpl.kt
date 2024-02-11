package com.softyorch.mvvmjetpackcompose.ui.models.errorValidator

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.softyorch.mvvmjetpackcompose.ui.models.ContactErrorModel
import com.softyorch.mvvmjetpackcompose.ui.models.ContactUi
import com.softyorch.mvvmjetpackcompose.utils.EMPTY_STRING
import com.softyorch.mvvmjetpackcompose.utils.MIN_NAME_LENGTH
import javax.inject.Inject

class ContactValidatorImpl @Inject constructor() : IContactValidator {

    private val contactError: ContactErrorModel = ContactErrorModel()

    override fun searchError(contact: ContactUi): ContactErrorModel {

        val errorName = !isNameCorrect(contact.name, MIN_NAME_LENGTH)
        val errorSurName = contact.surName?.let {
            if (it.isEmpty()) false else !isNameCorrect(it, MIN_NAME_LENGTH)
        } ?: false
        val errorPhoneNumber = !isPhoneNumberCorrect(contact.phoneNumber)
        val errorEmail =
            contact.email?.let { if (it.isEmpty()) false else !isEmailCorrect(it) } ?: false
        val errorAge =
            contact.age?.let { if (it.isEmpty()) false else !isAgeCorrect(it) } ?: false

        return contactError.copy(
            name = errorName,
            surName = errorSurName,
            phoneNumber = errorPhoneNumber,
            email = errorEmail,
            age = errorAge
        )
    }

    override fun searchFieldError(contact: ContactUi, oldDataContact: ContactUi): ContactErrorModel {

        if (contact.name != oldDataContact.name)
            return contactError.copy(name = !isNameCorrect(contact.name, MIN_NAME_LENGTH))

        if (contact.surName != oldDataContact.surName)
            return contactError.copy(surName = contact.surName?.let {
                if (it.isEmpty()) false else !isNameCorrect(it, MIN_NAME_LENGTH)
            } ?: false)

        if (contact.phoneNumber != oldDataContact.phoneNumber)
            return contactError.copy(phoneNumber = !isPhoneNumberCorrect(contact.phoneNumber))

        if (contact.email != oldDataContact.email)
            return contactError.copy(email = contact.email?.let {
                if (it.isEmpty()) false else !isEmailCorrect(it)
            } ?: false)

        if (contact.age != oldDataContact.age)
            return contactError.copy(age = contact.age?.let {
                if (it.isEmpty()) false else !isAgeCorrect(it)
            } ?: false)

        return contactError
    }

    private fun isNameCorrect(name: String, size: Int): Boolean {
        return (name != EMPTY_STRING && name.length >= size && !name.isDigitsOnly())
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
