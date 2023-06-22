package com.example.mediabuttonproject

import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.media.browse.MediaBrowser
import android.media.session.MediaController
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MainActivity : AppCompatActivity() {
    private var mIntentFilter: IntentFilter? = null

    private var mMediaBrowserCompat: MediaBrowser? = null
    private var mMediaControllerCompat: MediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, MediaPlaybackService::class.java))
        initHeadset()
    }

    private fun initHeadset() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mMediaBrowserCompatConnectionCallback: MediaBrowser.ConnectionCallback =
                object : MediaBrowser.ConnectionCallback() {
                    override fun onConnected() {
                        super.onConnected()
                        mMediaControllerCompat = MediaController(
                            applicationContext,
                            mMediaBrowserCompat!!.sessionToken
                        )
                        mediaController = mMediaControllerCompat
                        Constants.isInitHeadset = true
                        mediaController.transportControls.pause()
                    }
                }
            mMediaBrowserCompat = MediaBrowser(
                this, ComponentName(this, HeadsetButtonOreoUpService::class.java),
                mMediaBrowserCompatConnectionCallback, intent.extras
            )
            mMediaBrowserCompat!!.connect()
        } else {
            val intent = Intent(this, HeadsetButtonServiceUptoNought::class.java)
            startService(intent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            finishAffinity()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.keyCode == KeyEvent.KEYCODE_HOME) {
            finishAffinity()
            true
        } else {
            super.dispatchKeyEvent(event)
        }
    }


}