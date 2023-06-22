package com.example.mediabuttonproject

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.IBinder

class HeadsetButtonServiceUptoNought : Service() {
    //private String TAG = "myHeadsetKey";
    private var mAudioManager: AudioManager? = null
    private var rec: ComponentName? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Register the only designated broadcast for monitoring button events
        mAudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        rec = ComponentName(packageName, MediaButtonIntentReceiver::class.java.name)
        //mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,20,0);
        mAudioManager!!.registerMediaButtonEventReceiver(rec)
        // Sign headphone plug broadcasting
        registerHeadsetPlugReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerHeadsetPlugReceiver()
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Dynamic registration broadcast, and set the highest priority
     */
    private fun registerHeadsetPlugReceiver() {
        val mediaButtonIntentReceiver = MediaButtonIntentReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON)
        intentFilter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY + 1
        registerReceiver(mediaButtonIntentReceiver, intentFilter)
    }
}