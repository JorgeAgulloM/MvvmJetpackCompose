package com.softyorch.mvvmjetpackcompose.core.intents

import android.content.Context
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionDial
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionEmail
import com.softyorch.mvvmjetpackcompose.core.intents.actions.ActionSMS

class ActionsImpl (private val context: Context): IActions {

    companion object {
        fun Context.Actions(): IActions = ActionsImpl(this)
    }
    override fun sendDial(phone: String) {
        ActionDial().send(context, phone)
    }

    override fun sendSMS(phone: String, name: String) {
        ActionSMS().send(context, phone, name)
    }

    override fun sendEmail(phone: String, name: String) {
        ActionEmail().send(context, phone, name)
    }
}
