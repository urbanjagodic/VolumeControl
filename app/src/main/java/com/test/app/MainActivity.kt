package com.test.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main activity class, which most the user
 * interaction takes place.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Creates button, editText views, sets
     * onClickListeners, eventListener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val volumeButton = findViewById<Button>(R.id.volumeButton)
        val linesButton = findViewById<Button>(R.id.linesButton)
        val volumeEdit = findViewById<EditText>(R.id.volumeEdit)
        val linesEdit = findViewById<EditText>(R.id.linesEdit)
        val colorButton = findViewById<Button>(R.id.colorButton)
        val colorEdit = findViewById<EditText>(R.id.colorEdit)

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

    /**
     * Function that is called as an eventListener callback function,
     * when refreshData method inside [VolumeController] is called.
     *
     * Refreshes [volumeEdit] and [linesEdit] editText views.
     */
    fun updateValues(volumeEdit : EditText, linesEdit : EditText) {
        volumeEdit.setText(volumeController.getVolume())
        linesEdit.setText(volumeController.getLines().toString())
    }

    /**
     * Shows [Toast] based on passed [message] string.
     */
    fun showToast(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
