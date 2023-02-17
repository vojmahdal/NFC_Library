package com.example.easynfc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.Robolectric
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNfcAdapter
import org.robolectric.shadows.ShadowActivity

//@RunWith(AndroidJUnit4::class)
@RunWith(RobolectricTestRunner::class)
class EzNfcTest {

    private lateinit var shadowNfcAdapter: ShadowNfcAdapter
    private lateinit var nfcLib: EzNfc
private lateinit var activity: Activity

private lateinit var mockNdef: Ndef
    @get:Rule
    val mMainActivityRule: ActivityScenarioRule<TestActivity> =
        ActivityScenarioRule(TestActivity::class.java)

    @Before
    fun setup(){
        activity = Robolectric.buildActivity(TestActivity::class.java)
            .create().get()

        val context = ApplicationProvider.getApplicationContext<Context>()
        Robolectric.getForegroundThreadScheduler().pause()
        Robolectric.getBackgroundThreadScheduler().pause()

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        shadowNfcAdapter = Shadows.shadowOf(nfcAdapter)

        shadowNfcAdapter.setEnabled(true)
    }

    @After
    fun tearDown() {
        // zakázání NFC
        shadowNfcAdapter.setEnabled(false)

        // ukončení Robolectricu
        Robolectric.getForegroundThreadScheduler().reset()
        Robolectric.getBackgroundThreadScheduler().reset()
    }



    @Test
    fun test() {
        val textRecord = NdefRecord.createTextRecord("en", "test")
        var message = NdefMessage(textRecord)

        var shadowActivity = Shadows.shadowOf(activity)
        val intent = Intent(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, arrayOf(message))
        }
        //shadowActivity.setLatestIntent(intent)

    }
    @Test
    fun Testv2(){
        val record = NdefRecord.createTextRecord("en", "Hello World!")
        val tag = Mockito.mock(Tag::class.java)
        `when`(tag.id).thenReturn(byteArrayOf(1, 2, 3, 4))
        `when`(tag.techList).thenReturn(arrayOf(Ndef::class.java.name))

        val ndefMessage = NdefMessage(arrayOf(record))
        `when`(mockNdef.isConnected).thenReturn(true)
        `when`(mockNdef.maxSize).thenReturn(100)
        `when`(mockNdef.isWritable).thenReturn(true)
        `when`(mockNdef.canMakeReadOnly()).thenReturn(true)
        `when`(mockNdef.ndefMessage).thenReturn(ndefMessage)

        val intent = Intent(NfcAdapter.ACTION_NDEF_DISCOVERED)
        intent.putExtra(NfcAdapter.EXTRA_TAG, tag)
        val activity = buildActivity(TestActivity::class.java, intent).setup().get()


        assertTrue(activity.nfcAdapter!!.isEnabled)
        assertTrue(activity.nfcAdapter!!.isNdefPushEnabled)
        activity.nfcAdapter!!.setNdefPushMessage(ndefMessage, activity)


    }
    @Test
    fun Test(){

    }
}
