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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var actualBtn: Button
    var player = 0
    var count = 0

    private var nfcText: String = ""
    private val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var intentFilterArray: Array<IntentFilter>? = null
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))
    private var pendingIntent: PendingIntent? = null

    //field
    private lateinit var f1: Button
    private lateinit var f2: Button
    private lateinit var f3: Button

    private lateinit var f4: Button
    private lateinit var f5: Button
    private lateinit var f6: Button

    private lateinit var f7: Button
    private lateinit var f8: Button
    private lateinit var f9: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            try {
                ndef.addDataType("text/plain")
            }catch (e: IntentFilter.MalformedMimeTypeException){
                throw RuntimeException("fail", e)
            }
            intentFilterArray = arrayOf(ndef)
        if (nfcAdapter == null) {
            val builder = AlertDialog.Builder(this@MainActivity, R.style.Theme_NFCTicTac)
            builder.setMessage("Does not support NFC")
            builder.setPositiveButton("cancel", null)
            val myDialog = builder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
        } else if (!nfcAdapter!!.isEnabled) {
            val builder = AlertDialog.Builder(this@MainActivity, R.style.Theme_NFCTicTac)
            builder.setTitle("NFC disabled")
            builder.setMessage("enable NFC")
            builder.setPositiveButton("settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            builder.setNegativeButton("cancel", null)
            val myDialog = builder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
        }}catch (ex: Exception){
            Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }


        f1 = findViewById(R.id.field1)
        f2 = findViewById(R.id.field2)
        f3 = findViewById(R.id.field3)

        f4 = findViewById(R.id.field4)
        f5 = findViewById(R.id.field5)
        f6 = findViewById(R.id.field6)

        f7 = findViewById(R.id.field7)
        f8 = findViewById(R.id.field8)
        f9 = findViewById(R.id.field9)

        play(f1)
        play(f2)
        play(f3)

        play(f4)
        play(f5)
        play(f6)

        play(f7)
        play(f8)
        play(f9)


    }

     private fun play(actualBtn: Button){
        actualBtn.setOnClickListener {
        if(actualBtn.text == ""){
            if (player == 0  && nfcText =="nfc_tag_blue"){
                actualBtn.text = "X"
                player = 1
                count++
            }else if(player == 1 && nfcText == "nfc_tag_black"){
                actualBtn.text = "O"
                player = 0
                count++
            }else{
                Toast.makeText(applicationContext, "Stejný uživatel nesmí hrát dvakrát", Toast.LENGTH_SHORT).show()
            }
            check()
            endGame()
        }
        }
    }
    fun endGame(){
        if (count == 9){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }


    fun check(){
      /*  if(actualBtn.text == "" && nfcText =="nfc_tag_blue"){
            if (player == 0){
                actualBtn.text = "X"
                player = 1
            }else{
                actualBtn.text = "O"
                player = 0
            }
        }*/
        //1st row
        if(f1.text == "O" && f2.text == "0" && f3.text == "0"){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        }
        if(f1.text == "X" && f2.text == "X" && f3.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        //2nd row
        if(f4.text == "O" && f5.text == "0" && f6.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f4.text == "X" && f5.text == "X" && f6.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        //3rd row
        if(f7.text == "O" && f8.text == "0" && f9.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f7.text == "X" && f8.text == "X" && f9.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //1st col
        if(f1.text == "O" && f4.text == "0" && f7.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f1.text == "X" && f4.text == "X" && f7.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        //2nd col
        if(f2.text == "O" && f5.text == "0" && f8.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f2.text == "X" && f5.text == "X" && f8.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        //3rd col
        if(f3.text == "O" && f6.text == "0" && f9.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f3.text == "X" && f6.text == "X" && f9.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        //diagonals
        if(f1.text == "O" && f5.text == "0" && f9.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f1.text == "X" && f5.text == "X" && f9.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        if(f3.text == "O" && f5.text == "0" && f7.text == "0"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        if(f3.text == "X" && f5.text == "X" && f7.text == "X"){
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilterArray, techListArray)

    }

    var nfcTagText = ""
    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)

        if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action){
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                try {
                    val inNdefMessage = this?.get(0) as NdefMessage
                    val inNdefRecord = inNdefMessage.records

                    var ndefRecord_0 = inNdefRecord[0]
                    var inMessage = String(ndefRecord_0.payload)
                    nfcTagText = inMessage.drop(3)

                     nfcText = "${nfcTagText}"
                    if (!nfcText.equals("")) {
                        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                            || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action
                        ) {
                            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                            val ndef = Ndef.get(tag) ?: return

                            if (ndef.isWritable) {
                                var message = NdefMessage(
                                    arrayOf(
                                        NdefRecord.createTextRecord("en", nfcTagText)
                                    )
                                )

                                ndef.connect()
                                ndef.writeNdefMessage(message)
                                ndef.close()
                                Toast.makeText(
                                    applicationContext,
                                    "success",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                try {
                                    ndefRecord_0 = inNdefRecord[2]
                                    inMessage = String(ndefRecord_0.payload)

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        applicationContext,
                                        "nfc not written",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } catch (ex: Exception) {
                    Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        if(this.isFinishing){
            nfcAdapter?.disableForegroundDispatch(this)
        }
        super.onPause()
    }

}