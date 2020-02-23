package com.test.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var volumeButton = findViewById<Button>(R.id.volumeButton)
        var linesButton = findViewById<Button>(R.id.linesButton)
        var volumeEdit = findViewById<EditText>(R.id.volumeEdit)
        var linesEdit = findViewById<EditText>(R.id.linesEdit)
        var colorButton = findViewById<Button>(R.id.colorButton)
        var colorEdit = findViewById<EditText>(R.id.colorEdit)

        volumeButton.setOnClickListener{
            if(volumeEdit.text.toString().isNotEmpty()) {
                volumeController.setVolume(volumeEdit.text.toString())
            }
            else {
                showToast("Entered volume value is empty")
            }
        }

        linesButton.setOnClickListener{
            if(linesEdit.text.toString().isNotEmpty()) {
                volumeController.setLines(linesEdit.text.toString().toInt())
            }
            else {
                showToast("Entered lines value is empty")
            }
        }

        colorButton.setOnClickListener{
            if(colorEdit.text.toString().isNotEmpty()) {
                volumeController.setColor(colorEdit.text.toString())
            }
            else {
                showToast("Entered color value is empty")
            }
        }


        volumeController.setEventListener(object: VolumeEventListener {
            override fun onEventOccured() {
                updateValues(volumeEdit, linesEdit)
            }

            override fun onErrorEventOccured() {
                showToast("Invalid entered color")
            }
        })
    }

    fun updateValues(volumeEdit : EditText, linesEdit : EditText) {
        volumeEdit.setText(volumeController.getVolume())
        linesEdit.setText(volumeController.getLines().toString())
    }

    fun showToast(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
