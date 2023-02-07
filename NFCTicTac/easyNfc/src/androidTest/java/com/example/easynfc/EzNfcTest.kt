package com.example.easynfc

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EzNfcTest {

    @get:Rule
     val mMainActivityRule: ActivityScenarioRule<TestActivity> = ActivityScenarioRule(TestActivity::class.java)

    @Test
    fun getNfcAdapter() {
        mMainActivityRule.scenario.onActivity { activity ->
            val result = EzNfc(activity).nfcAdapter
            assertThat(result).isNotNull()
        }

    }

    @Test
    fun setNfcAdapter() {
    }

    @Test
    fun check() {

    }

    @Test
    fun read() {
        mMainActivityRule.scenario.onActivity { activity ->
            val test = "test"
            val result = EzNfc(activity).read(activity.intent)
            //assert(true)
            assertThat(result).isNotNull()
          //  assertThat(result).
        }
    }

    @Test
    fun writeText() {
        mMainActivityRule.scenario.onActivity { activity ->
            val ezLib = EzNfc(activity)
            val test = "test"
            val write = EzNfc(activity).writeText(activity.intent, test)
           // assertThat(result).
            assertThat(write).isEqualTo(ezLib.outputMessage)
        }
    }

  /*  @Test
    fun `empty text`() {
        mMainActivityRule.scenario.onActivity { activity ->
            val test = "test"
            val result = EzNfc(activity).writeText(activity.intent, test)
            assertThat(result).isFalse()
        }
    }*/

    @Test
    fun writeUrl() {
        mMainActivityRule.scenario.onActivity { activity ->
            val test = "https://www.google.cz/?client=safari"
            assertThat(test).doesNotContainMatch("[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
            val write = EzNfc(activity).writeText(activity.intent, test)
            // assertThat(result).
        }
    }



    @Test
    fun writeEqualsRead() {
        mMainActivityRule.scenario.onActivity { activity ->
            val test = "testEquals"
            val read = EzNfc(activity).read(activity.intent)
            assertThat(read).isEqualTo(test)
            // assertThat(result).
        }
    }


    @Test
    fun onPause() {
    }

    @Test
    fun onCreateFilter() {
        mMainActivityRule.scenario.onActivity { activity ->
           val result = EzNfc(activity, intentFilterArray = null)
        }
    }

    @Test
    fun onResumeRead() {
    }

    @Test
    fun onResumeWrite() {
    }
}