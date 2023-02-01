package com.example.easynfc

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.widget.TextView
import android.widget.Toast

/*
class EzNfc {
    //nfc read to var
    /**function to read Record from NFC
     * Use this function in Activity, that read records
     * Use it in fun OnNewIntent()
     * function need only attributes Intent and Context
     * this function return value type String
     */
    fun read(intent: Intent, context: Context): String{
        var textMessage = ""
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action){
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables){

                try {
                    val inNdefMessage = this?.get(0) as NdefMessage
                    val inNdefRecord = inNdefMessage.records
                    var ndefRecord_0 = inNdefRecord[0]
                    var inMessage = String(ndefRecord_0.payload)

                    var nfcMessage = inMessage.drop(3)

                    textMessage = nfcMessage

                    if (!textMessage.equals("")){
                        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                            || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action){
                            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                            val ndef = Ndef.get(tag)
                            if(ndef.isWritable){
                                var message = NdefMessage(
                                    arrayOf(
                                        NdefRecord.createTextRecord("en", nfcMessage)
                                    )
                                )
                                ndef.connect()
                                ndef.writeNdefMessage(message)
                                ndef.close()
                            }else{
                                try {
                                    ndefRecord_0 = inNdefRecord[2]
                                    inMessage = String(ndefRecord_0.payload)

                                }catch (e: Exception){
                                    Toast.makeText(context, "user id not written", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                } catch (ex: Exception){
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return textMessage
    }

    /**
     * function that verified, if device support NFC reader.
     * this function verified, if NFC reader is enabled in settings as well
     * function need two attributes, Context and NfcAdapter?
     * Use this function in Activity, that works with NFC
     * Use it in fun OnCreate after initialing IntentFilterArray in try statement
     */
    fun support(context: Context, nfcAdapter : NfcAdapter?){
        try {
            if (nfcAdapter == null) {
                val builder = AlertDialog.Builder(context, R.style.Dialog)
                builder.setMessage("Does not support NFC")
                builder.setPositiveButton("cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
            } else if (!nfcAdapter!!.isEnabled) {
                val builder = AlertDialog.Builder(context, R.style.Dialog)
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
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**function to write Record from NFC
     * Use this function in Activity, that write records
     * Use it in fun OnNewIntent()
     * function need attributes intent and context
     * third attribute is string value, which is written on NFC tag
     * If writing on NFC is successful, toast shows text "Successfully written".
     */
    fun write(intent: Intent, context: Context, writeMessage: String){
        try {
            var nfcText = writeMessage

            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                val ndef = Ndef.get(tag) ?: return

                if(ndef.isWritable){
                    var message = NdefMessage(
                        arrayOf(
                            NdefRecord.createTextRecord("en", nfcText)
                        )
                    )
                    ndef.connect()
                    ndef.writeNdefMessage(message)
                    ndef.close()
                        Toast.makeText(context, "Successfully written", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "NFC does not support write", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (ex: Exception){
            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
        }
    }


}
*/
//////////////*/////////
class EzNfcBuilder private constructor(){
    class Builder{
        private lateinit var intent: Intent
        private lateinit var context: Context
        private var nfcAdapter: NfcAdapter? = null
        private var textMessage = ""

        fun setIntent(intent: Intent) = apply {
            this.intent = intent
        }
        fun setContext(context: Context) = apply {
            this.context = context
        }
        fun setNfcAdapter(nfcAdapter: NfcAdapter?) = apply {
            this.nfcAdapter = nfcAdapter
        }

        fun setTextMessage(textMessage: String) = apply {
            this.textMessage = textMessage
        }

        fun support(){
            try {
                if (nfcAdapter == null) {
                    val builder = AlertDialog.Builder(context, R.style.Dialog)
                    builder.setMessage("Does not support NFC")
                    builder.setPositiveButton("cancel", null)
                    val myDialog = builder.create()
                    myDialog.setCanceledOnTouchOutside(false)
                    myDialog.show()
                } else if (!nfcAdapter!!.isEnabled) {
                    val builder = AlertDialog.Builder(context, R.style.Dialog)
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
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        fun builderRead() : String{
            var outputMessage = ""
            val action = intent.action
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == action){
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
                        Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{Toast.makeText(context, "cannot read from NFC", Toast.LENGTH_SHORT).show()}
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
                var nfcText = textMessage

                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){
                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val ndef = Ndef.get(tag) ?: return

                    if(ndef.isWritable){
                        var message = NdefMessage(
                            arrayOf(
                                NdefRecord.createTextRecord("en", nfcText)
                            )
                        )
                        ndef.connect()
                        ndef.writeNdefMessage(message)
                        ndef.close()
                        Toast.makeText(context, "Successfully written", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "NFC does not support write", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (ex: Exception){
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * write NFC url
         */
        fun writeUrl(){
            try {
                var nfcText = textMessage

                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                    || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){
                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val ndef = Ndef.get(tag) ?: return

                    if(ndef.isWritable){
                        var message = NdefMessage(
                            arrayOf(
                                NdefRecord.createUri(nfcText)
                            )
                        )
                        ndef.connect()
                        ndef.writeNdefMessage(message)
                        ndef.close()
                        Toast.makeText(context, "Successfully written", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "NFC does not support write", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (ex: Exception){
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
            }
        }

        /**
         * function to check write
         */
        fun checkWrite(){

        }
    }
}







class EzNfc(){
    private lateinit var intent: Intent
    private lateinit var context: Context
    private var nfcAdapter: NfcAdapter? = null
    private var textMessage = ""

    fun setIntent(intent: Intent) = apply {
        this.intent = intent
    }
    fun setContext(context: Context) = apply {
        this.context = context
    }
    fun setNfcAdapter(nfcAdapter: NfcAdapter?) = apply {
        this.nfcAdapter = nfcAdapter
    }

    //nastaveni pro zpravu
    fun setTextMessage(textMessage: String) = apply {
        this.textMessage = textMessage
    }

    fun builderRead(intent: Intent, context: Context) : String{
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action){
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables){
                try {
                    val inNdefMessage = this?.get(0) as NdefMessage
                    val inNdefRecord = inNdefMessage.records
                    var ndefRecord_0 = inNdefRecord[0]
                    var inMessage = String(ndefRecord_0.payload)

                    var nfcMessage = inMessage.drop(3)

                    textMessage = nfcMessage

                    if (!textMessage.equals("")){
                        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                            || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action){
                            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                            val ndef = Ndef.get(tag)
                            if(ndef.isWritable){
                                var message = NdefMessage(
                                    arrayOf(
                                        NdefRecord.createTextRecord("en", nfcMessage)
                                    )
                                )
                                ndef.connect()
                                ndef.writeNdefMessage(message)
                                ndef.close()
                            }else{
                                try {
                                    ndefRecord_0 = inNdefRecord[2]
                                    inMessage = String(ndefRecord_0.payload)

                                }catch (e: Exception){
                                    Toast.makeText(context, "user id not written", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                } catch (ex: Exception){
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return textMessage
    }

    /*class Builder(val intent: Intent, val context: Context){
        var read: String? = null
        private set

    }*/

    //nfc read to var
    /**function to read Record from NFC
     * Use this function in Activity, that read records
     * Use it in fun OnNewIntent()
     * function need only attributes Intent and Context
     * this function return value type String
     */
    fun read(intent: Intent, context: Context): String{
        var textMessage = ""
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action){
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables){

                try {
                    val inNdefMessage = this?.get(0) as NdefMessage
                    val inNdefRecord = inNdefMessage.records
                    var ndefRecord_0 = inNdefRecord[0]
                    var inMessage = String(ndefRecord_0.payload)

                    var nfcMessage = inMessage.drop(3)

                    textMessage = nfcMessage

                    if (!textMessage.equals("")){
                        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                            || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action){
                            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                            val ndef = Ndef.get(tag)
                            if(ndef.isWritable){
                                var message = NdefMessage(
                                    arrayOf(
                                        NdefRecord.createTextRecord("en", nfcMessage)
                                    )
                                )
                                ndef.connect()
                                ndef.writeNdefMessage(message)
                                ndef.close()
                            }else{
                                try {
                                    ndefRecord_0 = inNdefRecord[2]
                                    inMessage = String(ndefRecord_0.payload)

                                }catch (e: Exception){
                                    Toast.makeText(context, "user id not written", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                } catch (ex: Exception){
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return textMessage
    }
    /**
     * function that verified, if device support NFC reader.
     * this function verified, if NFC reader is enabled in settings as well
     * function need two attributes, Context and NfcAdapter?
     * Use this function in Activity, that works with NFC
     * Use it in fun OnCreate after initialing IntentFilterArray in try statement
     */
    fun support(context: Context, nfcAdapter : NfcAdapter?){
        try {
            if (nfcAdapter == null) {
                val builder = AlertDialog.Builder(context, R.style.Dialog)
                builder.setMessage("Does not support NFC")
                builder.setPositiveButton("cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
            } else if (!nfcAdapter!!.isEnabled) {
                val builder = AlertDialog.Builder(context, R.style.Dialog)
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
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**function to write Record from NFC
     * Use this function in Activity, that write records
     * Use it in fun OnNewIntent()
     * function need attributes intent and context
     * third attribute is string value, which is written on NFC tag
     * If writing on NFC is successful, toast shows text "Successfully written".
     */
    fun write(intent: Intent, context: Context, writeMessage: String){
        try {
            var nfcText = writeMessage

            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                val ndef = Ndef.get(tag) ?: return

                if(ndef.isWritable){
                    var message = NdefMessage(
                        arrayOf(
                            NdefRecord.createTextRecord("en", nfcText)
                        )
                    )
                    ndef.connect()
                    ndef.writeNdefMessage(message)
                    ndef.close()
                    Toast.makeText(context, "Successfully written", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "NFC does not support write", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (ex: Exception){
            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
        }
    }


}