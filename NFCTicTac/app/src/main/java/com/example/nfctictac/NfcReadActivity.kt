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

    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private var intentFilterArray: Array<IntentFilter>? = null
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))
    private var pendingIntent: PendingIntent? = null
    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.nfc_read_activity)

        textView = findViewById(R.id.txtviewmachineid)

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
try {


    try {
        ndef.addDataType("text/plain")
    } catch (e: IntentFilter.MalformedMimeTypeException) {
        throw RuntimeException("fail", e)
    }
    intentFilterArray = arrayOf(ndef)

    EzNfc().support(this, nfcAdapter)


}catch (e: Exception){
    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
}
    }
        override fun onResume(){
            super.onResume()
          nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilterArray, techListArray)
        }

        var iswrite = "0"

        var machineId = ""




    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)

      //  EzNfc().read(intent, applicationContext, textView)
        textView.text = EzNfc().readVar(intent, applicationContext)


    }

    override fun onPause() {
        if(this.isFinishing){
            nfcAdapter?.disableForegroundDispatch(this)
        }
        super.onPause()

    }

}