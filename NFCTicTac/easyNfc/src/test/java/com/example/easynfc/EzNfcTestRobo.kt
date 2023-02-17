package com.example.easynfc

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.nfc.NdefMessage
import org.junit.Assert.*
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner


import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import java.util.*


@RunWith(RobolectricTestRunner::class)
class EzNfcTestRobo {

    private lateinit var libEzNfc: EzNfc
    private lateinit var activity: Activity
    private lateinit var ndef: IntentFilter

    private lateinit var applicationContext: Context
    private lateinit var intentFilterArray: Array<IntentFilter>

    private lateinit var context: Context
    @Before
    fun setup(){
        activity = Mockito.mock(Activity::class.java)

        context = InstrumentationRegistry.getInstrumentation().targetContext

        ndef = Mockito.mock(IntentFilter::class.java)
        applicationContext = Mockito.mock(Context::class.java)
        intentFilterArray = arrayOf(ndef)
        libEzNfc = EzNfc(activity, intentFilterArray)
    }

    @Test
    fun getNfcAdapter() {
    }

    @Test
    fun setNfcAdapter() {
    }

    fun createTextRecord(text: String): NdefMessage{
        val languageCode = "en"
        val payload = text.toByteArray(Charsets.UTF_8)
        val record = NdefRecord.createTextRecord(languageCode, text)
        return NdefMessage(arrayOf(record))
    }

    @Test
    fun read() {
        val ndefRecord = NdefRecord.createTextRecord("en", "Hello NFC world!")
        val ndefMessage = NdefMessage(arrayOf(ndefRecord))

        val tag = Mockito.mock(Tag::class.java)

        val ndef = Ndef.get(tag)
        ndef.connect()
        ndef.writeNdefMessage(ndefMessage)
        ndef.close()
        val tagResult = ndef.tag
        assertTrue(Arrays.equals(tagResult.techList, tag.techList))


    }

    @Test
    fun writeText() {
    }

    @Test
    fun writeUrl() {
    }

    @Test
    fun onPause() {
    }

    @Test
    fun onCreateFilter() {
    }

    @Test
    fun onResumeRead() {
    }

    @Test
    fun onResumeWrite() {
    }
}