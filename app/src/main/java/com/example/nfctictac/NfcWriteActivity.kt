package com.example.nfctictac

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.easynfc.EzNfc

class NfcWriteActivity : AppCompatActivity() {
    private var intentFilterArray: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null


    private lateinit var textView : TextView
    private lateinit var nfcText: EditText

    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var nfcLib = EzNfc( this, intentFilterArray = intentFilterArray)

    private var operation = "text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   supportActionBar?.hide()
        setContentView(R.layout.nfc_write_activity)
        textView = findViewById(R.id.textNfc)
        nfcText = findViewById(R.id.editText)

        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        val radioText = findViewById<RadioButton>(R.id.radio_text)
        val radioUrl = findViewById<RadioButton>(R.id.radio_url)



        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{
                group, id ->
                if(radioText.isChecked){
                    operation = "text"
                }else if(radioUrl.isChecked){
                    operation = "url"
                } else{
                    operation = "text"
                }
            })

        nfcLib.nfcAdapter = nfcAdapter

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        intentFilterArray = nfcLib.onCreateFilter()
    }

    override fun onResume() {
        super.onResume()
       nfcLib.onResume(pendingIntent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if(nfcText.text.isNullOrEmpty()){
            Toast.makeText(this, "message cannot be empty", Toast.LENGTH_SHORT).show()
        }else {
            if (operation == "text") {
                nfcLib.writeText(intent, nfcText.text.toString())
            } else if (operation == "url") {
                nfcLib.writeUrl(intent, nfcText.text.toString())
            } else {
                Toast.makeText(this, "select operation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
     nfcLib.onPause()
        super.onPause()
    }

}