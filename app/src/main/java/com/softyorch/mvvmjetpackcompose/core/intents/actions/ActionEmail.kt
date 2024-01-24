package com.softyorch.mvvmjetpackcompose.core.intents.actions

import android.content.Context
import android.content.Intent
import android.net.Uri

class ActionEmail {
    fun send(context: Context, phone: String, name: String) {
        val intent = generateIntent(phone, name)
        context.startActivity(intent)
    }

    private fun generateIntent(phone: String, name: String): Intent {
        val action = Intent.ACTION_SENDTO
        val prefix = "smsto:"
        val uri = Uri.parse("$prefix$phone")

        return Intent(action, uri).apply {
            putExtra("sms_body", "Hola $name")
        }
    }
}
