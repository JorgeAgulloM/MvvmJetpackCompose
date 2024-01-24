package com.softyorch.mvvmjetpackcompose.core.intents

interface IActions {
    fun sendDial(phone: String)
    fun sendSMS(phone: String, name: String)
    fun sendEmail(phone: String, name: String)
}
