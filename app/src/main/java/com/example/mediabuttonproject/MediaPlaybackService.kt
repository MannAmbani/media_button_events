package com.example.mediabuttonproject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.media.session.MediaButtonReceiver


class MediaPlaybackService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val mediaSessionCompatCallBack: MediaSessionCompat.Callback =
        object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()
                Toast.makeText(application, "Play Button is pressed!", Toast.LENGTH_SHORT).show()
            }

            override fun onPause() {
                super.onPause()
                Toast.makeText(application, "Pause Button is pressed!", Toast.LENGTH_SHORT).show()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                Toast.makeText(application, "Next Button is pressed!", Toast.LENGTH_SHORT).show()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                Toast.makeText(application, "Previous Button is pressed!", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onStop() {
                super.onStop()
                Toast.makeText(application, "Stop Button is pressed!", Toast.LENGTH_SHORT).show()
            }

            override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
                val intentAction = mediaButtonEvent.action
                if (Intent.ACTION_MEDIA_BUTTON == intentAction) {
                    val event: KeyEvent =
                        mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)!!
                    if (event != null) {
                        val action: Int = event.getAction()
                        if (action == KeyEvent.ACTION_DOWN) {
                            when (event.getKeyCode()) {
                                KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {                                 // code for fast forward
                                    Log.d("TAG", "fast forward")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_NEXT -> {                              // code for next
                                    Log.d("TAG", "media next")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PLAY -> {
                                    Log.d("TAG", "d play")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                                    Log.d("TAG", "d Pause")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                                    // code for play/pause
                                    Toast.makeText(
                                        application,
                                        "d Play Button is pressed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("TAG", "play pause")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {                             // code for previous
                                    Log.d("TAG", "Previous")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_REWIND -> {// code for rewind
                                    Log.d("TAG", "rewind")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_STOP -> {
                                    // code for stop
                                    Toast.makeText(
                                        application,
                                        "Stop Button is pressed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("TAG", "stop")
                                    return true
                                }
                            }
                            return false
                        }
                        if (action == KeyEvent.ACTION_UP) {
                            when (event.getKeyCode()) {
                                KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {                                 // code for fast forward
                                    Log.d("TAG", "fast forward")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_NEXT -> {                              // code for next
                                    Log.d("TAG", "media next")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PLAY -> {
                                    Log.d("TAG", "up play")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                                    Log.d("TAG", "u Pause")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                                    // code for play/pause
                                    Toast.makeText(
                                        application,
                                        " up Play Button is pressed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("TAG", "play pause")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {                             // code for previous
                                    Log.d("TAG", "Previous")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_REWIND -> {// code for rewind
                                    Log.d("TAG", "rewind")
                                    return true
                                }

                                KeyEvent.KEYCODE_MEDIA_STOP -> {
                                    // code for stop
                                    Toast.makeText(
                                        application,
                                        "Stop Button is pressed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("TAG", "stop")
                                    return true
                                }
                            }
                            return false
                        }
                    }
                }
                return super.onMediaButtonEvent(mediaButtonEvent)
            }
        }

    private var mediaSessionCompat: MediaSessionCompat? = null


    override fun onCreate() {
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show()
        Log.e("SERVICE", "onCreate")
        mediaSessionCompat = MediaSessionCompat(this, "MEDIA")
        mediaSessionCompat!!.setCallback(mediaSessionCompatCallBack)
        mediaSessionCompat!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        val mStateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
            )
        mediaSessionCompat!!.setPlaybackState(mStateBuilder.build())
        mediaSessionCompat!!.isActive = true
    }

    override fun onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show()
        Log.e("SERVICE", "onDestroy")
        mediaSessionCompat!!.release()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show()
        Log.e("SERVICE_STARTUP", "onStart")
        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent)
        return super.onStartCommand(intent, flags, startId)
    }
}