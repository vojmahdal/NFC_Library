package com.example.easynfc

import android.app.Activity
import android.app.AlertDialog
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
){
    private lateinit var intent: Intent
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))

     private var textMessage: String = ""
     private var outputMessage: String = ""

    private var languageCode: String = "en"

    var nfcAdapter: NfcAdapter? = null



    /**
     * function is optional,  use in fun OnNewIntent(intent: Intent)
     *
     * function is for reading data from NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     * @return string message of NFC tag
     */
    fun read(intnt: Intent) : String{
        intent = intnt
        outputMessage = ""
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action){
            readPrivate()
        }
        else{
            Log.e("read_error", "Cannot read from NFC tag")
            Toast.makeText(activity.applicationContext, "cannot read from NFC", Toast.LENGTH_SHORT).show()
        }
        return outputMessage
    }
    private fun toast(text: String){
        Toast.makeText(activity.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * function is optional,  use in fun OnNewIntent(intent: Intent)
     *
     * function is for writing data of text into NFC tag
     *
     * @param intnt parse here Intent from parameter of fun onNewIntent
     *@param txt parse here message of type String
     */
    fun writeText(intnt: Intent, txt: String){
        intent = intnt
        textMessage = txt
        outputMessage = ""
        try {
            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){

                writeTextPrivate()
                if(checkWriteText()){
                    Toast.makeText(activity.applicationContext, "Successfully written", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity.applicationContext, "Writing text on tag failed", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (ex: Exception){
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
    fun writeUrl(intnt: Intent, txt: String){
        intent = intnt
        textMessage = txt
        if (checkUrl()){
            try {
                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){
                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val ndef = Ndef.get(tag) ?: return

                    if(ndef.isWritable){
                        var message = NdefMessage(
                            arrayOf(
                                NdefRecord.createUri(textMessage)
                            )
                        )
                        ndef.connect()
                        ndef.writeNdefMessage(message)
                        ndef.close()

                        Toast.makeText(activity.applicationContext, "Successfully written", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity.applicationContext, "NFC does not support write", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (ex: Exception){
                Toast.makeText(activity.applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }else Toast.makeText(activity.applicationContext, "url is not valid", Toast.LENGTH_SHORT).show()
    }



    /**
     * function is mandatory, use in fun onPause
     *
     */
    fun onPause(){
        if(activity.isFinishing){
            nfcAdapter?.disableForegroundDispatch(activity)
        }
    }

    /**
     * method onCreateFilter is used
     * to assign the created variable of class Activity
     *
     * @return intentFilterArray, for used Activity
     */
    fun onCreateFilter(): Array<IntentFilter>?{

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            try {
                ndef.addDataType("text/plain")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
            intentFilterArray = arrayOf(ndef)
            support()
        }catch (e: Exception){
//            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
        return intentFilterArray
    }



    /**
     *  function is mandatory, use in fun onResume
     *
     * @param pendingIntent must be created in fun onCreate, FLAG of pendingIntent must be FLAG_ACTIVITY_SINGLE_TOP
     */
    fun onResume(pendingIntent: PendingIntent?){
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilterArray, techListArray)
    }



    /**
     * private function that verified, if device support NFC reader.
     * this function verified, if NFC reader is enabled in settings as well
     * function need two attributes, Context and NfcAdapter?
     * Use this function in Activity, that works with NFC
     * Use it in fun OnCreate after initialing IntentFilterArray in try statement
     */
    private fun support(){
        try {
            if (nfcAdapter == null) {
                Toast.makeText(activity.applicationContext,
                    "Does not support NFC", Toast.LENGTH_SHORT).show()
            } else if (!nfcAdapter!!.isEnabled) {
                Toast.makeText(activity.applicationContext,
                    "NFC disabled, please enable NFC reading", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * private fun used in public fun WriteText
     */
    private fun writeTextPrivate(){
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        val ndef = Ndef.get(tag) ?: return

        if(ndef.isWritable) {
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
    private fun checkUrl(): Boolean{
        return URLUtil.isValidUrl(textMessage)
    }

    /**
     * private fun used in public fun read
     */
    private fun readPrivate(){
        val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        with(parcelables){
            try {
                val inNdefMessage = this?.get(0) as NdefMessage
                val inNdefRecord = inNdefMessage.records
                val ndefRecord_0 = inNdefRecord[0]
                val inMessage = String(ndefRecord_0.payload)

                val nfcMessage = inMessage.drop(3)

                outputMessage = nfcMessage

            } catch (ex: Exception){
                Toast.makeText(activity.applicationContext, "no data found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * function to check if data are equal to input data
     */
    private fun checkWriteText(): Boolean{
        return outputMessage == textMessage
    }

    /**
     * function to set language code for function to write text
     *
     * default code is "en", but it can be changed
     *
     * @param code insert here string code of language you want to use
     */
    fun setLanguageCode(code: String){
        languageCode = code
    }

}