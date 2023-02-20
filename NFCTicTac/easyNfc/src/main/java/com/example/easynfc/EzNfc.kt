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
 * @property activity actual Activity
 * @property intentFilterArray create new intentFilterArray in activity
 * @constructor Create empty Ez nfc
 * @sample private var nfcLib = EzNfc(this, intentFilterArray)
 */
class EzNfc(
    private var activity: Activity,
    private var intentFilterArray: Array<IntentFilter>? = null
){
    private lateinit var intent: Intent
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))

     private var textMessage: String = ""
     private var outputMessage: String = ""

    var nfcAdapter: NfcAdapter? = null

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
                val builder = AlertDialog.Builder(activity.applicationContext, R.style.Dialog)
                builder.setMessage("Does not support NFC")
                builder.setPositiveButton("cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
            } else if (!nfcAdapter!!.isEnabled) {
                val builder = AlertDialog.Builder(activity.applicationContext, R.style.Dialog)
                builder.setTitle("NFC disabled")
                builder.setMessage("enable NFC")
                builder.setPositiveButton("settings") { _, _ ->
                    // startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                }
                builder.setNegativeButton("cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
            }

        }catch (e: Exception){
            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * function is optional,  use in fun OnNewIntent(intent: Intent)
     *
     * @param intnt parse here new intent
     * @return string message of NFC tag
     *
     * @sample val read = NFCLib.read(pendingIntent)
     */
    fun read(intnt: Intent) : String{
        intent = intnt
        outputMessage = ""
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action){
            readPrivate()
        }
        else{
           // Log.e("read_error", "Cannot read from NFC tag")
          //  Toast.makeText(activity.applicationContext, "cannot read from NFC", Toast.LENGTH_SHORT).show()
           // toast( "cannot read from NFC")
        }
        return outputMessage
    }
    private fun toast(text: String){
        Toast.makeText(activity.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * function is optional,  use in fun OnNewIntent(intent: Intent)
     *
     * @param intnt parse here new intent
     *@param txt parse here message of type String
     * @sample  NFCLib.writeText(pendingIntent, "test")
     */
    fun writeText(intnt: Intent, txt: String){
        intent = intnt
        textMessage = txt
        try {
            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){

                writeTextPrivate()
                readPrivate()
                if(checkWriteText()){
                    Toast.makeText(activity.applicationContext, "Successfully written", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity.applicationContext, "NFC does not support write", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (ex: Exception){
            Toast.makeText(activity.applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun writeTextPrivate(){
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        val ndef = Ndef.get(tag) ?: return

        if(ndef.isWritable) {
            var message = NdefMessage(
                arrayOf(
                    NdefRecord.createTextRecord("en", textMessage)
                )
            )
            ndef.connect()
            ndef.writeNdefMessage(message)
            ndef.close()
        }
    }

    /**
     * function is optional, use in fun OnNewIntent(intent: Intent)
     *
     * @param intnt parse here new intent
     * @param txt parse here message of type String, must be valid Url
     * @sample  NFCLib.writeUrl(pendingIntent, "http://www.test.com")
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
     * function to check if url is valid
     */
    private fun checkUrl(): Boolean{
        return URLUtil.isValidUrl(textMessage)
    }

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
     * function to check write
     */
    private fun checkWriteText(): Boolean{
        return outputMessage == textMessage
    }

    /**
     * function is mandatory, use in fun onPause
     * @sample onPause()
     */
    fun onPause(){
        if(activity.isFinishing){
            nfcAdapter?.disableForegroundDispatch(activity)
        }
    }

    /**
     * On create filter
     *
     * @return intentFilterArray, for used Activity
     * @sample  intentFilterArray = nfcLib.onCreateFilter()
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
     * On resume read
     *
     * @param pendingIntent
     */
    fun onResumeRead(pendingIntent: PendingIntent?){
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilterArray, techListArray)
    }

    /**
     * On resume write
     *
     * @param pendingIntent
     */
    fun onResumeWrite(pendingIntent: PendingIntent?){
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilterArray, techListArray)
    }

    /**
     *  function is mandatory, use in fun onResume
     *
     * @param pendingIntent
     */
    fun onResume(pendingIntent: PendingIntent?){
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilterArray, techListArray)
    }



}