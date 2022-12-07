package com.example.nfctictac

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
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NfcWriteActivity : AppCompatActivity() {
    private var intentFilterArray: Array<IntentFilter>? = null
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))
    private val nfcAdapter : NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var pendingIntent: PendingIntent? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.nfc_write_activity)
      //  this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("text/plain")
        }catch (e: IntentFilter.MalformedMimeTypeException){
            throw RuntimeException("fail", e)
        }
        intentFilterArray = arrayOf(ndef)
        if(nfcAdapter == null){
            val builder = AlertDialog.Builder(this@NfcWriteActivity, R.style.Theme_NFCTicTac)
            builder.setMessage("Does not support NFC")
            builder.setPositiveButton("cancel", null)
            val myDialog = builder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
        }else if (!nfcAdapter!!.isEnabled){
            val builder= AlertDialog.Builder(this@NfcWriteActivity, R.style.Theme_NFCTicTac)
            builder.setTitle("NFC disabled")
            builder.setMessage("enable NFC")
            builder.setPositiveButton("settings"){
                _, _ -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            builder.setNegativeButton("cancell", null)
            val myDialog = builder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilterArray, techListArray)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
      //  Toast.makeText(applicationContext, "successfully connected", Toast.LENGTH_SHORT).show()
        try {
            val nfctext = "nfc_tag_black"

            if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                val ndef = Ndef.get(tag) ?: return

                if(ndef.isWritable){
                    var message = NdefMessage(
                        arrayOf(
                            NdefRecord.createTextRecord("en", nfctext)
                        )
                    )
                    ndef.connect()

                    ndef.writeNdefMessage(message)
                    ndef.close()

                    Toast.makeText(applicationContext, "successfully written", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "is not writable", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (ex: Exception){
            Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        if(this.isFinishing){
            nfcAdapter?.disableForegroundDispatch(this)
        }
        super.onPause()
    }
}