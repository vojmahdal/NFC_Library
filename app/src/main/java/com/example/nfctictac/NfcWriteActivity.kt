package com.example.nfctictac

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.easynfc.EzNfc

class NfcWriteActivity : AppCompatActivity() {
    private var intentFilterArray: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null


    private lateinit var textView: TextView

    private lateinit var nfcText: EditText
    private lateinit var secText: EditText
    private lateinit var thirdText: EditText

    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var nfcLib = EzNfc(this, intentFilterArray = intentFilterArray)

    private var operation = "text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   supportActionBar?.hide()
        setContentView(R.layout.nfc_write_activity)
        textView = findViewById(R.id.textNfc)
        nfcText = findViewById(R.id.editText)

        secText = findViewById(R.id.editSubject)
        thirdText = findViewById(R.id.editMessage)

        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        val radioText = findViewById<RadioButton>(R.id.radio_text)
        val radioUrl = findViewById<RadioButton>(R.id.radio_url)
        val radioMail = findViewById<RadioButton>(R.id.radio_mail)
        val radioMailAddress = findViewById<RadioButton>(R.id.radio_mail_address)
        val radioGeo = findViewById<RadioButton>(R.id.radio_geo)
        val radioSms = findViewById<RadioButton>(R.id.radio_sms)




        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, id ->
                if (radioText.isChecked) {
                    secText.visibility = View.GONE
                    thirdText.visibility = View.GONE
                    operation = "text"
                    nfcText.hint = "text"
                } else if (radioUrl.isChecked) {
                    secText.visibility = View.GONE
                    thirdText.visibility = View.GONE
                    operation = "url"
                    nfcText.hint = "valid url"
                } else if (radioMailAddress.isChecked) {
                    secText.visibility = View.GONE
                    thirdText.visibility = View.GONE
                    operation = "mailAddress"
                    nfcText.hint = "mail Address"
                } else if (radioMail.isChecked) {
                    secText.visibility = View.VISIBLE
                    thirdText.visibility = View.VISIBLE
                    nfcText.hint = "Receiver"
                    secText.hint = "Subject"
                    thirdText.hint = "Message"
                    operation = "mail"
                } else if (radioGeo.isChecked) {
                    secText.visibility = View.VISIBLE
                    thirdText.visibility = View.GONE
                    nfcText.hint = "latitude"
                    secText.hint = "longitude"
                    operation = "geo"
                } else if (radioSms.isChecked) {
                    secText.visibility = View.VISIBLE
                    thirdText.visibility = View.GONE
                    nfcText.hint = "telephone number"
                    secText.hint = "message"
                    operation = "sms"
                } else {
                    operation = "text"
                }
            })

        nfcLib.nfcAdapter = nfcAdapter
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        if (Build.VERSION.SDK_INT < 30) {
            pendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                this, 0,
                intent, PendingIntent.FLAG_MUTABLE
            )
        }
        intentFilterArray = nfcLib.onCreateFilterWrite()
    }

    override fun onResume() {
        super.onResume()
        nfcLib.onResume(pendingIntent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (nfcText.text.isNullOrEmpty()) {
            Toast.makeText(this, "message cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (operation == "text") {
                nfcLib.writeText(intent, nfcText.text.toString())
            } else if (operation == "url") {
                nfcLib.writeUrl(intent, nfcText.text.toString())
            } else if (operation == "mailAddress") {
                nfcLib.writeEmailAddress(intent, nfcText.text.toString())
            } else if (operation == "mail") {
                nfcLib.writeEmailMessage(
                    intent, nfcText.text.toString(), secText.text.toString(),
                    thirdText.text.toString()
                )
            } else if (operation == "sms") {
                nfcLib.writeSms(intent, nfcText.text.toString(), secText.text.toString())
            } else if (operation == "geo") {
                nfcLib.writeLocation(intent, nfcText.text.toString(), secText.text.toString())
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