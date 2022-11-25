package com.example.nfctictac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var buttonStart: Button
    private lateinit var buttonNfc: Button
    private lateinit var buttonSettings: Button
    private lateinit var buttonQuit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        buttonStart = findViewById(R.id.start_button)
        buttonNfc = findViewById(R.id.nfc_button)
        buttonSettings = findViewById(R.id.settings_button)
        buttonQuit = findViewById(R.id.quit_button)

        buttonStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        buttonNfc.setOnClickListener {
            val intent = Intent(this, NfcReadActivity::class.java)
            startActivity(intent)
        }
    }
}