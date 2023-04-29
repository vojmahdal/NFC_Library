package com.example.easynfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcF
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast


/**
 * Library class for NFC
 *
 * use it in your Activity class
 * @property activity actual Activity
 * @property intentFilterArray create new intentFilterArray in activity
 * @constructor Create instance of Ez nfc
 */
class EzNfc(
    private var activity: Activity,
    private var intentFilterArray: Array<IntentFilter>? = null
) {
    private lateinit var intent: Intent
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))

    private var textMessage: String = ""
    private var outputMessage: String = ""

    private var languageCode: String = "en"

    var nfcAdapter: NfcAdapter? = null


    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for reading text from NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @return string message of NFC tag
     */
    fun read(intnt: Intent): String {
        intent = intnt
        outputMessage = ""
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action
        ) {
            readPrivate()
        } else {
            Log.e("read_error", "Cannot read from NFC tag")
            Toast.makeText(activity.applicationContext, "cannot read from NFC", Toast.LENGTH_SHORT)
                .show()
        }
        return outputMessage
    }


    /**
     * function is optional,  use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing plain text into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     *@param txt parse here message of type String
     */
    fun writeText(intnt: Intent, txt: String) {
        intent = intnt
        textMessage = txt
        outputMessage = ""
        try {
            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action
            ) {

                writeTextPrivate()
                if (checkWriteText()) {
                    Toast.makeText(
                        activity.applicationContext,
                        "Successfully written",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity.applicationContext,
                        "Writing text on tag failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(activity.applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing data of url into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @param txt parse here message of type String, must be valid Url
     */
    fun writeUrl(intnt: Intent, txt: String) {
        intent = intnt
        textMessage = txt
        if (checkUrl()) {
            writeUri(textMessage)
        } else Toast.makeText(
            activity.applicationContext,
            "url is not valid", Toast.LENGTH_SHORT
        ).show()
    }


    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing email address into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @param email parse here message of type String, must be valid email address
     */
    fun writeEmailAddress(intnt: Intent, email: String) {
        intent = intnt
        textMessage = email
        val uriMessage = "mailto:${email}"
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        if (email.matches(emailRegex.toRegex())) {
            writeUri(uriMessage)
        } else {
            Toast.makeText(
                activity.applicationContext,
                "format of e-mail is not valid",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing Email message into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @param email parse here email of reciver of type String, must be valid email address
     * @param subject parse here subject for receiver of type String
     * @param mailMessage parse here message for receiver of type String
     */
    fun writeEmailMessage(intnt: Intent, email: String, subject: String, mailMessage: String) {
        intent = intnt
        textMessage = email

        val uriMessage = "mailto:${email}" +
                "?subject=${subject}&body=${mailMessage}"

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        if (email.matches(emailRegex.toRegex())) {
            writeUri(uriMessage)
        } else {
            Toast.makeText(
                activity.applicationContext,
                "format of e-mail is not valid",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing telephone number into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @param email parse here message of type String, must be valid email address
     */
    fun writeTelNumber(intnt: Intent, telNumber: String) {
        intent = intnt
        val uriMessage = "tel:${telNumber}"

        val telRegex = "^[0-9\\-\\+]{9,15}\$"
        if (telNumber.matches(telRegex.toRegex())) {
            writeUri(uriMessage)
        } else {
            Toast.makeText(
                activity.applicationContext,
                "format of telephone is not valid", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing telephone number with sms message into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @param telNumber parse here telephone number of type String, must be valid telephone number
     * @param message parse here body of sms message
     */
    fun writeSms(intnt: Intent, telNumber: String, message: String) {
        intent = intnt
        textMessage = telNumber
        val uriMessage = "sms:${telNumber}?body=${message}"
        val telRegex = "^[0-9\\-\\+]{9,15}\$"
        if (telNumber.matches(telRegex.toRegex())) {
            writeUri(uriMessage)
        } else {
            Toast.makeText(
                activity.applicationContext,
                "format of telephone is not valid", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing data of geolocation into NFC tag
     * It is needed to add two parameters, latitude and longitude
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @param lat parse here latitude
     * @param long parse here longitude
     */
    fun writeLocation(intnt: Intent, lat: String, long: String) {
        intent = intnt

        val uriMessage = "geo:${lat},${long}"
        val latRegex =
            "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))\$"
        val longRegex =
            "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))\$"

        if (lat.matches(latRegex.toRegex()) and long.matches(longRegex.toRegex())) {
            writeUri(uriMessage)
        } else {
            Toast.makeText(
                activity.applicationContext,
                "format of latitude, or longitude is not valid", Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun writeUri(uri: String) {
        try {
            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action
            ) {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                val ndef = Ndef.get(tag) ?: return

                if (ndef.isWritable) {
                    val message = NdefMessage(
                        arrayOf(
                            NdefRecord.createUri(uri)
                        )
                    )
                    ndef.connect()
                    ndef.writeNdefMessage(message)
                    ndef.close()

                    Toast.makeText(
                        activity.applicationContext,
                        "Successfully written",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity.applicationContext,
                        "NFC does not support write",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(activity.applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * function is mandatory, use in fun onPause
     *
     */
    fun onPause() {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    /**
     * method onCreateFilter is used
     * to assign the created variable of class Activity
     *
     * Use in Activity for reading text from NFC tag
     *
     * @return intentFilterArray, for used Activity
     */
    fun onCreateFilterRead(): Array<IntentFilter>? {

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            try {
                ndef.addDataType("text/plain")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
            intentFilterArray = arrayOf(ndef)
            support()
        } catch (e: Exception) {
            Log.e("intentfilter", "intent filter is null")
        }
        return intentFilterArray
    }


    /**
     * method onCreateFilter is used
     * to assign the created variable of class Activity
     *
     * Use in Activity for writing data into NFC tag
     *
     * @return intentFilterArray, for used Activity
     */
    fun onCreateFilterWrite(): Array<IntentFilter>? {
        try {
            intentFilterArray = arrayOf(
                IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
            )
            support()
        } catch (e: Exception) {
            Log.e("intentfilter", "intent filter is null")
        }
        return intentFilterArray
    }

    /**
     *  function is mandatory, use in fun onResume
     *
     * @param pendingIntent must be created in fun onCreate, FLAG of pendingIntent must be FLAG_ACTIVITY_SINGLE_TOP
     */
    fun onResume(pendingIntent: PendingIntent?) {
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            intentFilterArray,
            techListArray
        )
    }


    /**
     * private function that verified, if device support NFC reader.
     * this function verified, if NFC reader is enabled in settings as well
     * function need two attributes, Context and NfcAdapter?
     * Use this function in Activity, that works with NFC
     * Use it in fun OnCreate after initialing IntentFilterArray in try statement
     */
    private fun support() {
        try {
            if (nfcAdapter == null) {
                Toast.makeText(
                    activity.applicationContext,
                    "Does not support NFC", Toast.LENGTH_SHORT
                ).show()
            } else if (!nfcAdapter!!.isEnabled) {
                Toast.makeText(
                    activity.applicationContext,
                    "NFC disabled, please enable NFC reading", Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * private fun used in public fun WriteText
     */
    private fun writeTextPrivate() {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        val ndef = Ndef.get(tag) ?: return

        if (ndef.isWritable) {
            var message = NdefMessage(
                arrayOf(
                    NdefRecord.createTextRecord(languageCode, textMessage)
                )
            )
            ndef.connect()
            ndef.writeNdefMessage(message)
            outputMessage = textMessage
            ndef.close()
        }
    }

    /**
     * function to check if url is valid
     */
    private fun checkUrl(): Boolean {
        return URLUtil.isValidUrl(textMessage)
    }

    /**
     * private fun used in public fun read
     */
    private fun readPrivate() {
        val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        with(parcelables) {
            try {
                val inNdefMessage = this?.get(0) as NdefMessage
                val inNdefRecord = inNdefMessage.records
                val ndefRecord_0 = inNdefRecord[0]
                val inMessage = String(ndefRecord_0.payload)

                val nfcMessage = inMessage.drop(3)

                outputMessage = nfcMessage

            } catch (ex: Exception) {
                Toast.makeText(activity.applicationContext, "no data found", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * function to check if data are equal to input data
     */
    private fun checkWriteText(): Boolean {
        return outputMessage == textMessage
    }

    /**
     * function to set language code for function to write text
     *
     * default code is "en", but it can be changed
     *
     * @param code insert here string code of language you want to use
     */
    fun setLanguageCode(code: String) {
        languageCode = code
    }

}