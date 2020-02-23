package com.test.app

import android.view.MotionEvent
import kotlin.math.roundToInt

/**
 * TouchHandler class takes cares of handling touch
 * events on [VolumeController.barsLayout] and triggering
 * proper [VolumeController] methods.
 */
class TouchHandler(var volumeController: VolumeController) {

    private var startY = 0.0

    /**
     * Handles touch events based on passed
     * [m] MotionEvent instance.
     */
    fun handleTouch(m: MotionEvent) : Boolean {
        val pointerCount = m.pointerCount
        for (i in 0 until pointerCount) {
            val y = m.getY(i)
            when(m.action) {
                MotionEvent.ACTION_UP -> {
                    val diff = (y - startY)
                    startY = 0.0
                    calculateTouchDiff(diff)
                }
                MotionEvent.ACTION_MOVE -> {
                    if(startY ==  0.0) {
                        startY = y.toDouble()
                    }
                }
            }
        }
        return true
    }

    /**
     * Calculates and sets [VolumeController.lines] based on
     * passed [touchDiff] from the handleTouch method.
     */
    private fun calculateTouchDiff(touchDiff : Double) {
        var currentDiff = touchDiff
        val direction = if (touchDiff < 0) "increase" else "decrease"
        if(touchDiff < 0) {
            currentDiff *= -1
        }
        val addedLineCount = (currentDiff / 100).roundToInt()
        if(direction.equals("increase")) {
            volumeController.setLines(if ((volumeController.getLines() + addedLineCount) < 10)
                volumeController.getLines() + addedLineCount else 10)
        }
        else {
            volumeController.setLines(if ((volumeController.getLines() - addedLineCount) > 0)
                volumeController.getLines() -  addedLineCount else 0)
        }
    }
}