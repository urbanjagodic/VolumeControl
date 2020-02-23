package com.test.app

import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class VolumeController @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var volumeText : TextView
    private var barsLayout : LinearLayout
    private var lines = 0
    private var volume = ""
    private var color = ""
    private var colorFlag = true
    private var volumeString = "Volume set at: %s %%"
    private var eventListener: VolumeEventListener? = null
    private var touchHandler = TouchHandler(this)

    private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager


    init {

        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var myView = inflater.inflate(R.layout.volume_controller_layout, this, true)

        volumeText = myView.findViewById(R.id.volume_label)
        barsLayout = myView.findViewById(R.id.bars)

        if(attrs != null) {
            initValues(attrs)
        }
        initBarLenghts()

        barsLayout.setOnTouchListener { view, motionEvent -> touchHandler.handleTouch(context, motionEvent)}
    }


    fun setEventListener(listener: VolumeEventListener?) {
        this.eventListener = listener
    }

    fun refreshData() {
        setEnabledLines()
        volumeText.setText(volumeString.format(volume))
        eventListener?.onEventOccured()
    }

    fun initValues(attrs: AttributeSet) {
        attrs.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.attributes,0, 0)
            volume = attributes.getString(R.styleable.attributes_volume).toString()
            lines = attributes.getInt(R.styleable.attributes_lines, 1)
            attributes.recycle()
        }
        setVolume(volume)
        refreshData()
    }

    fun initBarLenghts() {
        for (index in 0 until barsLayout.childCount) {
            val childBar: LinearLayout = barsLayout.getChildAt(index) as LinearLayout
            var maxWidth = childBar.layoutParams.width
            var calculatedWidth = (((10 - index) * 10) * maxWidth) / 100
            childBar.layoutParams.width = calculatedWidth
        }
    }

    fun setEnabledLines() {
        for (index in 0 until barsLayout.childCount) {
            val childBar: LinearLayout = barsLayout.getChildAt(index) as LinearLayout
            if(isLineEnabled(index)) {
                if(colorFlag) {
                    childBar.setBackgroundColor(ContextCompat.getColor(context, R.color.barColorEnabled))
                }
                else {
                    try {
                        childBar.setBackgroundColor(Color.parseColor(color))
                    } catch (ex: IllegalArgumentException) {
                        eventListener?.onErrorEventOccured()
                    }
                }
            }
            else {
                childBar.setBackgroundColor(ContextCompat.getColor(context, R.color.barColorDisabled))
            }
        }
    }

    fun isLineEnabled(currentIndex : Int) : Boolean {
        return ((10 - currentIndex) <= lines)
    }

    fun setLines(num : Int) {
        playSound(num)
        lines = num
        volume = if (num == 0) "0" else "${num}0"
        refreshData()
    }

    fun playSound(num : Int) {
        var linesDiff = num - lines;
        if(linesDiff < 0) {
            linesDiff *= -1
            for (index in 0 until linesDiff) {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
            }
        }
        else {
            for (index in 0 until linesDiff) {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
            }
        }
    }

    fun setVolume(num : String) {
        val currentLine = num.substring(0,num.length - 1).toInt()
        playSound(currentLine)
        volume = num
        lines = currentLine
        refreshData()
    }

    fun setColor(value : String) {
        color = value
        colorFlag = false
        setEnabledLines()
    }

    fun getLines() : Int {
        return lines
    }

    fun getVolume() : String {
        return volume
    }
}


