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
class ActionSMSTest {

    @RelaxedMockK
    private lateinit var context: Context
    private lateinit var actionSMS: ActionSMS

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        actionSMS = ActionSMS()
    }

    @Test
    fun testSendSMS() = runBlocking {
        val phone = "1234567890"
        val contactName = "Jorge"
        val expectedIntent = Intent(Intent.ACTION_VIEW, Uri.parse("smsto:$phone"))

        actionSMS.send(context, phone, contactName)

        verify {
            context.startActivity(match {
                it.data == expectedIntent.data && it.action == expectedIntent.action
            })
        }
    }

}
