package com.example.easynfc

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
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







class EzNfc constructor(
    private var intent: Intent,
    private var activity: Activity,
    private var nfcAdapter: NfcAdapter? = null,
    private var intentFilterArray: Array<IntentFilter>? = null,
    var textMessage: String = ""

){
    /**
     * function that verified, if device support NFC reader.
     * this function verified, if NFC reader is enabled in settings as well
     * function need two attributes, Context and NfcAdapter?
     * Use this function in Activity, that works with NFC
     * Use it in fun OnCreate after initialing IntentFilterArray in try statement
     */
    var outputMessage: String = ""


    //private var intentFilterArray: Array<IntentFilter>? = null
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))

    fun support(){
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

    /**function to read Record from NFC
     * Use this function in Activity, that read records
     * Use it in fun OnNewIntent()
     * function need only attributes Intent and Context
     * this function return value type String
     */
    fun builderRead() : String{
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

    /**function to write Record from NFC
     * Use this function in Activity, that write records
     * Use it in fun OnNewIntent()
     * function need attributes intent and context
     * third attribute is string value, which is written on NFC tag
     * If writing on NFC is successful, toast shows text "Successfully written".
     */
    fun write(){
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
     * write NFC url
     */
    fun writeUrl(){
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
    fun checkWriteText(): Boolean{
        return outputMessage == textMessage
    }

    /**function onPause**/
    fun onPause(){
        if(activity.isFinishing){
            nfcAdapter?.disableForegroundDispatch(activity)
        }
    }
  /*  fun init(){
        private val nfcAdapter: NfcAdapter? by lazy {
            NfcAdapter.getDefaultAdapter(this)
        }
        private var intentFilterArray: Array<IntentFilter>? = null
        private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))
    }*/
    fun onCreate(intentFilterArray: Array<IntentFilter>?){

      val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
      try {
          try {
              ndef.addDataType("text/plain")
          } catch (e: IntentFilter.MalformedMimeTypeException) {
              throw RuntimeException("fail", e)
          }
       //   intentFilterArray = arrayOf(ndef)

          //EzNfc().support(this, nfcAdapter)
          /*  EzNfc.Builder()
                .setContext(this)
                .setNfcAdapter(nfcAdapter)
                .support()
        */
      }catch (e: Exception){
          Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
      }
    }
    fun onCreateFilter(): Array<IntentFilter>?{

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            try {
                ndef.addDataType("text/plain")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
            intentFilterArray = arrayOf(ndef)

            //EzNfc().support(this, nfcAdapter)
            /*  EzNfc.Builder()
                  .setContext(this)
                  .setNfcAdapter(nfcAdapter)
                  .support()
          */
            support()
        }catch (e: Exception){
            Toast.makeText(activity.applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
        return intentFilterArray
    }


    fun onResume(pendingIntent: PendingIntent?){
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilterArray, techListArray)
    }
}