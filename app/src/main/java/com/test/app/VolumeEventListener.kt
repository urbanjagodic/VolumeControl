package com.test.app

/**
 * VolumeEventListener interface is
 * used for event callback interaction
 * between customView and MainActivity
 */
interface VolumeEventListener {
    /**
     * Triggered when custom event occurs.
     */
    fun onEventOccured()

    /**
     * Triggered when and exception error is caught.
     */
    fun onErrorEventOccured()
}