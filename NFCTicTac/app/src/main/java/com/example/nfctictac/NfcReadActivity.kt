package com.example.nfctictac

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.easynfc.EzNfc

class NfcReadActivity : AppCompatActivity() {

    private var intentFilterArray: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null

    private lateinit var textView : TextView
   // private lateinit var nfcAdapter: NfcAdapter

   // var nfcLib = EzNfc(intent, this)
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfc_read_activity)
        textView = findViewById(R.id.txtviewmachineid)

     //   nfcAdapter =  NfcAdapter.getDefaultAdapter(this)
      //  nfcLib.nfcAdapter = nfcAdapter

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

           intentFilterArray = EzNfc(intent, this,  NfcAdapter.getDefaultAdapter(this), intentFilterArray).onCreateFilter()

    }
        override fun onResume(){
            super.onResume()


            EzNfc(intent, this, NfcAdapter.getDefaultAdapter(this), intentFilterArray).onResumeRead(pendingIntent)
            //nfcLib.on
        }

    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
       // nfcLib.intent = intent
        textView.text =    EzNfc(intent, this).builderRead()
            //nfcLib.builderRead()

    }

    override fun onPause() {
       EzNfc(intent, this).onPause()
     //   nfcLib.onPause()
        super.onPause()

    }

}