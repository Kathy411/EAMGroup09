package com.softbankrobotics.eamgroup09

import android.R
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy


class SmsActivity: RobotActivity(), RobotLifecycleCallbacks {
    override fun onRobotFocusGained(qiContext: QiContext?) {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}

/*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this )
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.OVERLAY)
        setContentView(R.layout.activity_)
    }

override fun onRobotFocusGained(qiContext: QiContext?) {
        val senden = findViewById<Button>(R.id.btn_)
        val Aufnahme: Button = findViewById(R.id.button2)
        val anzeige : TextView = findViewById(R.id.textView)

        senden.setOnClickListener(
                View.OnClickListener { anzeige.text = "SMS wurde versendet" }
        )
        Aufnahme.setOnClickListener(
                View.OnClickListener { anzeige.text = "Aufnahme wurde begonnen" }
        )
    }

    fun onClick(v: View?) {
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) //Sprache erkennen
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bitte sage jetzt den Inhalt deiner SMS-Nachricht!")
        try {
            startActivityforResult(i, 100)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(ApplicationProvider.getApplicationContext<Context>(), "Die Sprachausgabe wird nicht unterstützt", Toast.LENGTH_LONG).show()
        }
    }

    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            }
        }



    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}
/*
 */