package com.softyorch.mvvmjetpackcompose.ui.models.errorValidator

interface IUserValidator {
    fun isNameCorrect(name: String, size: Int): Boolean
    fun isPhoneNumberCorrect(phoneNumber: String): Boolean
    fun isEmailCorrect(email: String): Boolean
    fun isAgeCorrect(age: String): Boolean
}
