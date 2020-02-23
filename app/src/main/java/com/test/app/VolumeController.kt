package com.test.app

import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Custom view VolumeController class,
 * that extends [LinearLayout] widget view.
 */
class VolumeController @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    /**
     * Inits start variables.
     */
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

    /**
     * Inits start values and views.
     */
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val myView = inflater.inflate(R.layout.volume_controller_layout, this, true)

        volumeText = myView.findViewById(R.id.volume_label)
        barsLayout = myView.findViewById(R.id.bars)

        if(attrs != null) {
            initValues(attrs)
        }
        initBarLenghts()

        // Sets on touch listener to barsLayout and calls touchHandler method
        barsLayout.setOnTouchListener { view, motionEvent -> touchHandler.handleTouch(motionEvent)}
    }

    /**
     * Sets an event [listener] of
     * [VolumeEventListener] type
     */
    fun setEventListener(listener: VolumeEventListener?) {
        this.eventListener = listener
    }

    /**
     * Refreshes current data, enables lines, sets [volumeText],
     * triggers [eventListener] event
     */
    private fun refreshData() {
        setEnabledLines()
        volumeText.setText(volumeString.format(volume))
        eventListener?.onEventOccured()
    }

    /**
     * Inits [attrs] attribute values (if present) set in the xml layout file
     */
    private fun initValues(attrs: AttributeSet) {
        attrs.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.attributes,0, 0)
            volume = attributes.getString(R.styleable.attributes_volume).toString()
            lines = attributes.getInt(R.styleable.attributes_lines, 1)
            attributes.recycle()
        }
        setVolume(volume)
        refreshData()
    }

    /**
     * Sets volume bar lengths based on current bar index.
     */
    private fun initBarLenghts() {
        for (index in 0 until barsLayout.childCount) {
            val childBar: LinearLayout = barsLayout.getChildAt(index) as LinearLayout
            val maxWidth = childBar.layoutParams.width
            val calculatedWidth = (((10 - index) * 10) * maxWidth) / 100
            childBar.layoutParams.width = calculatedWidth
        }
    }

    /**
     * Colors selected bars into enabled color or disabled color.
     * Default pre-set enabled color is [R.color.barColorEnabled],
     * if user sets a color of his choice, that color is set instead.
     */
    private fun setEnabledLines() {
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

    /**
     * @returns true if bar of [currentIndex] is enabled,
     * @returns false otherwise
     */
    private fun isLineEnabled(currentIndex : Int) : Boolean {
        return ((10 - currentIndex) <= lines)
    }

    /**
     * Sets [lines], [volume] based on [num] input,
     * and plays sound effect, and refreshes data.
     */
    fun setLines(num : Int) {
        playSound(num)
        lines = num
        volume = if (num == 0) "0" else "${num}0"
        refreshData()
    }

    /**
     * Plays sound effect with a volume adjustment,
     * based on passed [num], which calculates linesDiff.
     */
    private fun playSound(num : Int) {
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

    /**
     * Sets [volume] and [lines] based on passed
     * [num] value. Plays sound and refreshes data.
     */
    fun setVolume(num : String) {
        val currentLine = num.substring(0,num.length - 1).toInt()
        playSound(currentLine)
        volume = num
        lines = currentLine
        refreshData()
    }

    /**
     * Sets color based on passed [value] color,
     * and re-draws enabled lines.
     */
    fun setColor(value : String) {
        color = value
        colorFlag = false
        setEnabledLines()
    }

    /**
     * @returns [lines] int
     */
    fun getLines() : Int {
        return lines
    }

    /**
     * @returns [volume] string
     */
    fun getVolume() : String {
        return volume
    }
}


