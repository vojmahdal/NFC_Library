package com.example.nfctictac

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.easynfc.EzNfc

class NfcReadActivity : AppCompatActivity() {

    private var intentFilterArray: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null

    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.nfc_read_activity)
        textView = findViewById(R.id.txtviewmachineid)

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
       intentFilterArray = EzNfc(intent, this, NfcAdapter.getDefaultAdapter(this), intentFilterArray).onCreateFilter()

    }
        override fun onResume(){
            super.onResume()
            EzNfc(intent, this, NfcAdapter.getDefaultAdapter(this), intentFilterArray).onResume(pendingIntent)
        }

    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        textView.text = EzNfc(intent, this).builderRead()
    }

    override fun onPause() {
       EzNfc(intent, this).onPause()
        super.onPause()

    }

}