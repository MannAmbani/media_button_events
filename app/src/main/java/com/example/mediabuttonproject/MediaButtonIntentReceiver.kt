package com.example.mediabuttonproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException

import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.UnsupportedEncodingException


class MediaButtonIntentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("ON RECEIVE")
        val intentAction = intent!!.action
        if (Intent.ACTION_MEDIA_BUTTON == intentAction) {
            if (intent.extras!!.getBoolean("isFromOreo") && intent.extras!!.getBoolean("isOn")) {
                Log.d("TAG", "isFromOreo and isOn")
//                publish()
            } else if (intent.extras!!.getBoolean("isFromOreo") && !intent.extras!!.getBoolean("isOn")) {
                Log.d("TAG", "isFromOreo and not isOn")
            } else {
                val event =
                    intent.getParcelableExtra<Parcelable>(Intent.EXTRA_KEY_EVENT) as KeyEvent?
                        ?: return
                val keycode = event.keyCode
                if (event.action == KeyEvent.ACTION_UP) {
                    when (keycode) {
                        KeyEvent.KEYCODE_MEDIA_PAUSE -> Log.d("TAG", "pause")
                        KeyEvent.KEYCODE_MEDIA_PLAY -> Log.d("TAG", "play")
                        KeyEvent.KEYCODE_HEADSETHOOK -> Log.d("TAG", "hook")
                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> Log.d("TAG", "previous")
                        KeyEvent.KEYCODE_MEDIA_NEXT -> Log.d("TAG", "next")
                    }
                }
            }
        }
    }

    fun publish() {
        val clientId = MqttClient.generateClientId()
        val client = MqttAndroidClient(
            MainActivity().applicationContext,
            "tcp://broker.hivemq.com:1883",
            clientId
        )
        val topic = "foo/bar"
        val payload = "the payload"
        var encodedPayload = ByteArray(0)
        try {
            encodedPayload = payload.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            client.publish(topic, message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }


//    override fun onReceive(context: Context?, intent: Intent) {
//         val intentAction = intent.action
//         if (Intent.ACTION_MEDIA_BUTTON != intentAction) {
//             return
//         }
//         val event = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_KEY_EVENT) as KeyEvent?
//             ?: return
//         val action = event.action
//         if (action == KeyEvent.ACTION_DOWN) {
//             // do something
//             Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show()
//         }
//         abortBroadcast()
//     }
//    companion object{
//        var delegate: Delegate? = null
//        private var mAudioManager: AudioManager? = null
//        private var mRemoteControlResponder: ComponentName? = null
//        private val doublePressSpeed:Long = 300 // double keypressed in ms
//
//        private var doublePressTimer: Timer? = null
//        private var counter = 0
//
//        fun register(context: Context) {
//            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//            mRemoteControlResponder = ComponentName(context, MediaButtonIntentReceiver::class.java)
//            mAudioManager!!.registerMediaButtonEventReceiver(mRemoteControlResponder)
//        }
//
//        fun unregister(context: Context?) {
//            mAudioManager!!.unregisterMediaButtonEventReceiver(mRemoteControlResponder)
//            if (doublePressTimer != null) {
//                doublePressTimer!!.cancel()
//                doublePressTimer = null
//            }
//        }
//    }
//
//
////    private var mAudioManager: AudioManager? = null
////    private var mRemoteControlResponder: ComponentName? = null
//
////    private val doublePressSpeed:Long = 300 // double keypressed in ms
////
////    private var doublePressTimer: Timer? = null
////    private var counter = 0
//
//    interface Delegate {
//
//
//        fun onMediaButtonSingleClick()
//        fun onMediaButtonDoubleClick()
//    }
//
//    override fun onReceive(context: Context?, intent: Intent?) {
//        if (intent == null || delegate == null || Intent.ACTION_MEDIA_BUTTON != intent.action) return
//        val keyEvent = intent.extras!![Intent.EXTRA_KEY_EVENT] as KeyEvent?
//        if (keyEvent == null || keyEvent.action != KeyEvent.ACTION_DOWN) return
//        counter++
//        if (doublePressTimer != null) {
//            doublePressTimer!!.cancel()
//        }
//        doublePressTimer = Timer()
//        doublePressTimer!!.schedule(object : TimerTask() {
//            override fun run() {
//                if (counter == 1) {
//                    delegate!!.onMediaButtonSingleClick()
//                } else {
//                    delegate!!.onMediaButtonDoubleClick()
//                }
//                counter = 0
//            }
//        }, doublePressSpeed)
//    }


    ////    override fun onReceive(context: Context?, intent: Intent) {
//    sda
//        val intentAction = intent.action
//        if (Intent.ACTION_MEDIA_BUTTON != intentAction) {
//            Log.d("TAG","Button Pressed")
//            return
//        }
//        Log.d("TAG","Button Pressed")
//        val event: KeyEvent =
//            intent.getParcelableExtra<Parcelable>(Intent.EXTRA_KEY_EVENT) as KeyEvent?
//                ?: return
//        val action: Int = event.getAction()
//        if (action == KeyEvent.ACTION_DOWN) {
//            // do something
//            Log.d("TAG","button pressed")
//            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show()
//        }
//        if (action == KeyEvent.KEYCODE_MEDIA_PLAY){
//            Log.d("TAG","play button pressed")
//            Toast.makeText(context, "play BUTTON PRESSED!", Toast.LENGTH_SHORT).show()
//        }
//        if (action == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
//            Log.d("TAG","pause button pressed")
//            Toast.makeText(context, "play pause BUTTON PRESSED!", Toast.LENGTH_SHORT).show()
//        }
//
//    }

}
