package com.example.nfctictac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var actualBtn: Button
    var player = 0
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val f1 = findViewById<Button>(R.id.field1)
        val f2 = findViewById<Button>(R.id.field2)
        val f3 = findViewById<Button>(R.id.field3)

        val f4 = findViewById<Button>(R.id.field4)
        val f5 = findViewById<Button>(R.id.field5)
        val f6 = findViewById<Button>(R.id.field6)

        val f7 = findViewById<Button>(R.id.field7)
        val f8 = findViewById<Button>(R.id.field8)
        val f9 = findViewById<Button>(R.id.field9)

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

    fun play(actualBtn: Button){
        actualBtn.setOnClickListener {
        if(actualBtn.text == ""){
            if (player == 0){
                actualBtn.text = "X"
                player = 1
            }else{
                actualBtn.text = "O"
                player = 0
            }
            count++
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


    fun check(view: View){
        val actualBtn = view as Button
        if(actualBtn.text == ""){
            if (player == 0){
                actualBtn.text = "X"
                player = 1
            }else{
                actualBtn.text = "O"
                player = 0
            }
        }

    }

}