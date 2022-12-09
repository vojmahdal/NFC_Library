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


class EzNfc {
    fun s(c: Context?, message: String?) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }

    fun read(intent: Intent, context: Context, textView: TextView){
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
                    textView.text = nfcMessage
                    if (!textView.text.toString().equals("")){
                        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                            || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action){
                            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                            val ndef = Ndef.get(tag) ?: return
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
    }

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
    fun write(intent: Intent, context: Context, writeMessage: String){
        try {
            var nfcText = writeMessage

            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
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
                    Toast.makeText(context, "čip neumožňuje zapisování", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (ex: Exception){
            Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
        }
    }
}