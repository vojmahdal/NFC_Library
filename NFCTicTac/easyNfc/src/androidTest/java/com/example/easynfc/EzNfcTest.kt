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
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.common.truth.Truth.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
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

@RunWith(AndroidJUnit4::class)
class EzNfcTest {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var nfcLib: EzNfc
//private lateinit var activityScenario: ActivityScenario<TestActivity>
 /*   @get:Rule
    val mMainActivityRule: ActivityScenarioRule<TestActivity> =
        ActivityScenarioRule(TestActivity::class.java)
*/
    @get: Rule
    val activityTestRule = ActivityTestRule(TestActivity::class.java)

  /*  @Before
    fun setup(){
        val context = InstrumentationRegistry
            .getInstrumentation().targetContext
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        activityScenario = launch(TestActivity::class.java)

        nfcLib = EzNfc(TestActivity(), intentFilterArray = null)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }
*/

    @Test
    fun testNfcReader() {
        // Ensure that NFC is enabled
        val nfcAdapter = NfcAdapter.getDefaultAdapter(activityTestRule.activity)
        assertNotNull(nfcAdapter)
        assertTrue(nfcAdapter.isEnabled)

        // Create a mock NDEF message to simulate the data on the tag
        val message = NdefMessage(
            arrayOf(
                NdefRecord.createTextRecord(
                   "en",
                    "Hello, NFC World!"
                )
            )
        )

        // Create an NDEF intent with the mock message
        val intent = Intent(activityTestRule.activity, activityTestRule.activity::class.java)
        intent.action = NfcAdapter.ACTION_NDEF_DISCOVERED
        intent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, arrayOf(message))

        // Send the intent to the activity
        activityTestRule.activity.intent = intent

    }

   /* @Test
    fun Test(){
        if(!nfcAdapter.isEnabled){
            return
        }
        val intent = Intent().apply {
            action = NfcAdapter.ACTION_TAG_DISCOVERED
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
       val tag = getNfcTag() ?: return
        val testData = "test"

        nfcLib.writeText(intent, testData)
       val read = nfcLib.read(intent)
        assertEquals(read, testData)
    }

    private fun getNfcTag(): Tag? {
        val intent = Intent().apply {
            action = NfcAdapter.ACTION_TAG_DISCOVERED
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
    }*/
}
