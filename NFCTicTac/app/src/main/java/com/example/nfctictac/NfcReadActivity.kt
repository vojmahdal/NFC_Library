package com.example.nfctictac

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class NfcReadActivity : AppCompatActivity() {

    //private var nfcAdapter: NfcAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.nfc_read_activity)
       // this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

    }



    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)

        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
            }
        }
    }
}