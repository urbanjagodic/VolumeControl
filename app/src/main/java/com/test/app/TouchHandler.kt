package com.test.app

import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import kotlin.math.roundToInt


class TouchHandler(var volumeController: VolumeController) {

    var startY = 0.0

    fun handleTouch(context: Context, m: MotionEvent) : Boolean {
        val pointerCount = m.pointerCount
        for (i in 0 until pointerCount) {
            val y = m.getY(i)
            when(m.action) {
                MotionEvent.ACTION_UP -> {
                    var diff = (y - startY)
                    startY = 0.0
                    calculateTouchDiff(diff)
                    //Toast.makeText(context, "ACTION UP, Y difference Y: $diff", Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_MOVE -> {
                    if(startY ==  0.0) {
                        startY = y.toDouble()
                        //Toast.makeText(context, "ACTION MOVE, current Y: $y", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return true
    }

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