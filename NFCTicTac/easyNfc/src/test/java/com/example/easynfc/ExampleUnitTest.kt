package com.example.easynfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.widget.Toast
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.MockedConstruction
import org.mockito.Mockito
import org.mockito.Mockito.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var activity: Activity
    private lateinit var ndef: IntentFilter

    private lateinit var applicationContext: Context
    private lateinit var intentFilterArray: Array<IntentFilter>

    private lateinit var libEzNfc: EzNfc

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Before
    fun setup(){
        activity = Mockito.mock(Activity::class.java)
        ndef = mock(IntentFilter::class.java)
        applicationContext = mock(Context::class.java)
        intentFilterArray = arrayOf(ndef)
        val toast = mock(Toast::class.java)

        //ezNFC
        libEzNfc = EzNfc(activity, intentFilterArray)
    }

    @Test
    fun onCreateFilterTest() {
        // mock objects
       // val activity = mock(Activity::class.java)


        // mock behavior
        `when`(activity.applicationContext).thenReturn(activity.applicationContext)
        `when`(activity.applicationContext).thenReturn(applicationContext)

        // test
        val onCreateFilter = libEzNfc.onCreateFilter()
        assertEquals(intentFilterArray, onCreateFilter)
    }

    @Test
    fun writeUrlTest() {
        // mock objects
        val tag = mock(Tag::class.java)
        val ndef = mock(Ndef::class.java)
        val message = mock(NdefMessage::class.java)
        val applicationContext = mock(Context::class.java)
        val toast = mock(Toast::class.java)

       // val ezTest = EzNfc(activity)
        // mock behavior
        `when`(activity.intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)).thenReturn(tag)
        `when`(Ndef.get(tag)).thenReturn(ndef)
        `when`(ndef.writeNdefMessage(message)).thenReturn(Unit)
        `when`(applicationContext.applicationContext).thenReturn(applicationContext)
        `when`(activity.applicationContext).thenReturn(applicationContext)

        // test
        libEzNfc.writeUrl(activity.intent, "www.example.com")
    }





}