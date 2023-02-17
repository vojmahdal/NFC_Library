package com.example.easynfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock

import java.net.URL

class EzNfcTest {
    private lateinit var activity: Activity
    private lateinit var ndef: IntentFilter

    private lateinit var applicationContext: Context
    private lateinit var intentFilterArray: Array<IntentFilter>

    private lateinit var libEzNfc: EzNfc

    private lateinit var nfcTestAdapter: NfcAdapter

    private lateinit var toast: Toast
    @Before
    fun setUp() {
        activity = Mockito.mock(Activity::class.java)
        ndef = Mockito.mock(IntentFilter::class.java)
        applicationContext = Mockito.mock(Context::class.java)
        intentFilterArray = arrayOf(ndef)
        nfcTestAdapter = Mockito.mock(NfcAdapter::class.java)
        toast = Mockito.mock(Toast::class.java)



        //ezNFC
        libEzNfc = EzNfc(activity, intentFilterArray)
    }


    @Test
    fun getNfcAdapter() {
        val result = libEzNfc.nfcAdapter
        assertEquals(result, libEzNfc.nfcAdapter)
    }

    @Test
    fun setNfcAdapter() {
        libEzNfc.nfcAdapter = nfcTestAdapter
        assertEquals(libEzNfc.nfcAdapter, nfcTestAdapter)
    }

    @Test
    fun read() {
        val intent = mock(Intent::class.java)
      //  val e = mock(Log::class.java)
        val result = libEzNfc.read(intent)
        val tag = Mockito.mock(Tag::class.java)
        val data = byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04)
        `when`(intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)).thenReturn(tag)
        `when`(tag.ndefMessage).thenReturn(NdefMessage(data))

    }

    @Test
    fun writeText() {
        val intent = mock(Intent::class.java)
        val tag = Mockito.mock(Tag::class.java)
        val result = libEzNfc.writeText(intent, "test")
        val read = libEzNfc.read(intent)
        assertEquals(read, "test")

    }
    @Test
    fun testReadNFC(){
        val mockTag = Mockito.mock(Tag::class.java)
        Mockito.`when`(mockTag.read(Mockito.anyString())).thenReturn(true)
        val testData = "testData"
        assertTrue(mockTag.read(testData))
    }

    @Test
    fun writeUrl() {
        val intent = mock(Intent::class.java)
        val url = mock(URLUtil::class.java)
        libEzNfc.writeUrl(intent, "testUrl")
    }

    @Test
    fun onPause() {

        libEzNfc.onPause()
    }

    @Test
    fun onCreateFilter() {
       // val makeText = mock(Toast::makeText())
       val createdFilter = libEzNfc.onCreateFilter()
        //assertEquals(createdFilter, null)
    }

    @Test
    fun onResumeRead() = run{
        val pendingIntent = mock(PendingIntent::class.java)
        libEzNfc.onResumeRead(pendingIntent)
    }

    @Test
    fun onResumeWrite() {
        val pendingIntent = mock(PendingIntent::class.java)
        libEzNfc.onResumeWrite(pendingIntent)
    }
}