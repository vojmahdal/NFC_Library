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
      //  val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
     //  NfcRead().create(this, nfcAdapter, ndef)
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
try {


    try {
        ndef.addDataType("text/plain")
    } catch (e: IntentFilter.MalformedMimeTypeException) {
        throw RuntimeException("fail", e)
    }
    intentFilterArray = arrayOf(ndef)

    EzNfc().support(this, nfcAdapter)
/*
    if (nfcAdapter == null) {
        val builder = AlertDialog.Builder(this@NfcReadActivity, R.style.Theme_NFCTicTac)
        builder.setMessage("Does not support NFC")
        builder.setPositiveButton("cancel", null)
        val myDialog = builder.create()
        myDialog.setCanceledOnTouchOutside(false)
        myDialog.show()
    } else if (!nfcAdapter!!.isEnabled) {
        val builder = AlertDialog.Builder(this@NfcReadActivity, R.style.Theme_NFCTicTac)
        builder.setTitle("NFC disabled")
        builder.setMessage("enable NFC")
        builder.setPositiveButton("settings") { _, _ ->
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }
        builder.setNegativeButton("cancel", null)
        val myDialog = builder.create()
        myDialog.setCanceledOnTouchOutside(false)
        myDialog.show()
    }*/
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

        EzNfc().read(intent, applicationContext, textView)

        /*
        val action = intent.action
      if(NfcAdapter.ACTION_NDEF_DISCOVERED == action){
          val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
          with(parcelables){
              try {
                  val inNdefMessage = this?.get(0) as NdefMessage
                  val inNdefRecord = inNdefMessage.records

                  var ndefRecord_0 = inNdefRecord[0]
                  var inMessage = String(ndefRecord_0.payload)
                  machineId = inMessage.drop(3)

                  textView.text = "Machine ID ${machineId}"
                  if(!textView.text.toString().equals("")){
                      if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action
                          || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action){
                          val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                          val ndef = Ndef.get(tag) ?: return

                          if(ndef.isWritable) {
                              var message = NdefMessage(
                                  arrayOf(
                                      NdefRecord.createTextRecord("en", machineId)
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

                          }else{
                              try {
                                  ndefRecord_0 = inNdefRecord[2]
                                  inMessage = String(ndefRecord_0.payload)

                              }catch (e: Exception){
                                  Toast.makeText(applicationContext, "user id not written", Toast.LENGTH_SHORT).show()
                              }
                          }
                      }
                  }
              }catch (ex: Exception){
                  Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
              }
          }
      }*/

    }

    override fun onPause() {
        if(this.isFinishing){
            nfcAdapter?.disableForegroundDispatch(this)
        }
        super.onPause()

    }

   /* private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("e")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }*/
}