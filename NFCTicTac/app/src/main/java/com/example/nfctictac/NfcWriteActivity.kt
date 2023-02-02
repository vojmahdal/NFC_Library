package com.example.nfctictac

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.easynfc.EzNfc

class NfcWriteActivity : AppCompatActivity() {
    private var intentFilterArray: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null


    private lateinit var textView : TextView
 //   val nfcTs = EzNfc(intent, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   supportActionBar?.hide()
        setContentView(R.layout.nfc_write_activity)
        textView = findViewById(R.id.textNfc)

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

       /* intentFilterArray = EzNfc(intent,
            this,
            NfcAdapter.getDefaultAdapter(this),
            intentFilterArray)
            .onCreateFilter()*/
    }

    override fun onResume() {
        super.onResume()
        //EzNfc(intent, this, NfcAdapter.getDefaultAdapter(this), intentFilterArray).onResumeRead(pendingIntent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //EzNfc(intent, this, textMessage = "nfc_Jedna").writeText()

    }

    override fun onPause() {
     //EzNfc(intent, this).onPause()
        super.onPause()
    }
}