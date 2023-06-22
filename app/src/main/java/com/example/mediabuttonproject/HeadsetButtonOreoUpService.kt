package com.example.mediabuttonproject

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver


class HeadsetButtonOreoUpService : MediaBrowserServiceCompat() {
    private var mMediaPlayer: MediaPlayer? = null
    private var mMediaSessionCompat: MediaSessionCompat? = null
    private var mediaButtonIntentReceiver: MediaButtonIntentReceiver? = null

    private val mMediaSessionCallback: MediaSessionCompat.Callback =
        object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
                val intentAction = mediaButtonEvent.action
                if (Intent.ACTION_MEDIA_BUTTON == intentAction) {
                    val event: KeyEvent =
                        mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)!!
                    if (event != null) {
                        val action: Int = event.action
                        if (action == KeyEvent.ACTION_DOWN) {
                            when (event.keyCode) {
                                KeyEvent.KEYCODE_MEDIA_PLAY, KeyEvent.KEYCODE_MEDIA_PAUSE, KeyEvent.KEYCODE_MEDIA_PREVIOUS, KeyEvent.KEYCODE_MEDIA_NEXT -> {
                                    Log.d("TAG", "d play")

                                    return true

                                }

                                KeyEvent.KEYCODE_HEADSETHOOK -> Log.d("TAG", "hook")
                            }
                            return false

                        }
                    }
                }

                return super.onMediaButtonEvent(mediaButtonEvent)


            }

            override fun onPlay() {
                super.onPlay()
                Log.d("TAG", "Play().. ")
                setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                sendBroadcast(true)
            }

            override fun onPause() {
                super.onPause()
                Log.d("TAG", "Pause().. ")
                setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
                if (Constants.isInitHeadset) {
                    Constants.isInitHeadset = false
                } else {
                    sendBroadcast(false)
                }
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                setMediaPlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT)
                sendBroadcast(true)
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()

                setMediaPlaybackState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS)
                sendBroadcast(true)
            }
        }

    private fun sendBroadcast(isOn: Boolean) {
        val intent = Intent(Intent.ACTION_MEDIA_BUTTON)
        intent.putExtra("isFromOreo", true)
        if (isOn) {
            intent.putExtra("isOn", true)
        } else {
            intent.putExtra("isOn", false)
        }
        sendBroadcast(intent)
    }

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
        initMediaSession()
        registerHeadsetPlugReceiver()
    }

    private fun registerHeadsetPlugReceiver() {
        mediaButtonIntentReceiver = MediaButtonIntentReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON)
        intentFilter.priority = 2147483647
        registerReceiver(mediaButtonIntentReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mMediaSessionCompat!!.release()
            unregisterReceiver(mediaButtonIntentReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initMediaPlayer() {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer!!.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        //mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer!!.start()
    }

    private fun initMediaSession() {
        val mediaButtonReceiver = ComponentName(
            applicationContext,
            MediaButtonReceiver::class.java
        )
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(this, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            mediaButtonIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        mMediaSessionCompat =
            MediaSessionCompat(applicationContext, "Tag", mediaButtonReceiver, pendingIntent)
        mMediaSessionCompat!!.setCallback(mMediaSessionCallback)
        mMediaSessionCompat!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)


        //mMediaSessionCompat.setMediaButtonReceiver(pendingIntent);
        mMediaSessionCompat!!.isActive = true
        sessionToken = mMediaSessionCompat!!.sessionToken
    }

    private fun setMediaPlaybackState(state: Int) {
        val playbackstateBuilder = PlaybackStateCompat.Builder()
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE)
        } else {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY)
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        mMediaSessionCompat!!.setPlaybackState(playbackstateBuilder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mMediaSessionCompat, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    //Not important for general audio service, required for class
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return if (TextUtils.equals(clientPackageName, packageName)) {
            BrowserRoot(getString(R.string.app_name), null)
        } else null
    }

    //Not important for general audio service, required for class
    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem?>?>
    ) {
        result.sendResult(null)
    }
}