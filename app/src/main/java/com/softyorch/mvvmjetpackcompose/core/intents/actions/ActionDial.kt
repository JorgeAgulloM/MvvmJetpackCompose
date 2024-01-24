package com.softyorch.mvvmjetpackcompose.core.intents.actions

import android.content.Context
import android.content.Intent
import android.net.Uri

class ActionDial {
    fun send(context: Context, phone: String) {
        val intent = generateIntent(phone)
        context.startActivity(intent)
    }

    private fun generateIntent(phone: String): Intent {
        val action = Intent.ACTION_DIAL
        val prefix = "tel:+"
        val uri = Uri.parse("$prefix$phone")

        return Intent(action, uri)
    }
}
