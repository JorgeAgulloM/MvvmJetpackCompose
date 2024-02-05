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
class ActionDialTest {

    @RelaxedMockK
    private lateinit var context: Context
    private lateinit var actionDial: ActionDial

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        actionDial = ActionDial()
    }

    @Test
    fun testSendNumberPhone() = runBlocking {
        val phone = "1234567890"
        val expectedIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+$phone"))

        actionDial.send(context, phone)

        verify {
            context.startActivity(match {
                it.data == expectedIntent.data && it.action == expectedIntent.action
            })
        }
    }
}
