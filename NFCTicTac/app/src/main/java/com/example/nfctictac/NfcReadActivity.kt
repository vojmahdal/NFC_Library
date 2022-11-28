package com.example.nfctictac

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.nfc.tech.NfcF
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NfcReadActivity : AppCompatActivity() {

    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private var intentFilterArray: Array<IntentFilter>? = null
    private val techListArray = arrayOf(arrayOf(NfcF::class.java.name))
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.nfc_read_activity)


        // this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        /*  try{

            //nfcread
            pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
            val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
            try {
                ndef.addDataType("text/plain")
            } catch (e: IntentFilter.MalformedMimeTypeException){
                throw RuntimeException("fail", e)
            }
            intentFilterArray = arrayOf(ndef)
            if(!nfcAdapter!!.isEnabled){
                val builder = AlertDialog.Builder(this@NfcReadActivity, R.style.M)
            }

        } catch (ex:Exception){
            Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
        }
*/
    }
        override fun onResume(){
            super.onResume()
          nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilterArray, techListArray)
        }

        var iswrite = "0"

        var machineId = ""




    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)

        val action = intent.action
      if(NfcAdapter.ACTION_NDEF_DISCOVERED == action){
          val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
          with(parcelables){
              try {
                  val inNdefMessage = this?.get(0) as NdefMessage
                  val inNdefRecord = inNdefMessage.records

                  //var inMessage = String()
              }catch (ex: Exception){
                  Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
              }
          }
      }

    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

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
    }
}