package com.softyorch.mvvmjetpackcompose.core.intents.actions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActionEmailTest {

    @RelaxedMockK
    private lateinit var context: Context
    private lateinit var actionEmail: ActionEmail

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        actionEmail = ActionEmail()
    }

    @Test
    fun testSendEmail() = runBlocking {
        val mail = "test@testing.com"
        val contactName = "Jorge"
        val expectedIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$mail"))

        actionEmail.send(context, mail, contactName)

        verify {
            context.startActivity(match {
                it.data == expectedIntent.data && it.action == expectedIntent.action
            })
        }
    }
}
