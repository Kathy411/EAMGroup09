package com.softbankrobotics.eamgroup09

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.Phrase
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import kotlin.concurrent.thread


@Suppress("DEPRECATION")
class SmsActivity : RobotActivity(), RobotLifecycleCallbacks {
    lateinit var btn_record : Button
    lateinit var btn_send: Button
    lateinit var btn_back_sms3 : Button
    lateinit var editTextNumber: EditText
    lateinit var editTextMessage: EditText
    lateinit var locale : Locale
    private val permissionRequest = 101

    companion object {
        private const val REQUEST_CODE_STT = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        QiSDK.register(this, this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)
        editTextNumber = findViewById(R.id.et_number)
        editTextNumber.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSoftKeyboard(editTextNumber)
            }
        }
        editTextMessage = findViewById(R.id.et_message)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val backButton8: Button = findViewById(R.id.btn_back8)
        backButton8.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }


        locale = Locale(Language.GERMAN, Region.GERMANY)
        val smsPhrase: Phrase = Phrase("Von hier aus kannst Du SMS versenden. Derzeit musst Du Text und Nummer noch eingeben, " +
                "aber meine Programmierer Innen arbeiten an der Spracheingabe. Wenn Du fertig bist, drücke einfach auf Senden")
        val smsSay = SayBuilder.with(qiContext).withPhrase(smsPhrase).withLocale(locale).build()
        smsSay.async().run()

         btn_send = findViewById(R.id.btn_send)
         btn_send.setOnClickListener {
            sendMessage()
        }

        /* btn_record = findViewById(R.id.btn_talk)
        btn_record.setOnClickListener {
            // Get the Intent action
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            // Language model defines the purpose, there are special models for other use cases, like search.
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            // Adding an extra language, you can use any language from the Locale class.
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.GERMAN)
            // Text that shows up on the Speech input prompt.
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Du kannst Deine Nachricht jetzt aufsprechen")
            try {
                // Start the intent for a result, and pass in our request code.
                startActivityForResult(sttIntent, REQUEST_CODE_STT)
            } catch (e: ActivityNotFoundException) {
                // Handling error when the service is not available.
                e.printStackTrace()
                Toast.makeText(this, "Dein Gerät unterstützt leider keine Texteingabe", Toast.LENGTH_LONG).show()
            }
        }

         */
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun sendMessage() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            myMessage()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),
                    permissionRequest)
        }
    }

    private fun myMessage() {
        val toNumber: String = editTextNumber.text.toString().trim()
        val txtMessage: String = editTextMessage.text.toString().trim()
        if (toNumber == "" || txtMessage == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(toNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(toNumber, null, txtMessage, null, null)
                setContentView(R.layout.activity_sms_sent)
               //  Toast.makeText(this, "Nachricht versendet", Toast.LENGTH_LONG).show()
               //  editTextMessage.text.clear()
               //  editTextNumber.text.clear()
            } else {
                Toast.makeText(this, "Ins Nummernfeld bitte nur Zahlen eingeben!", Toast.LENGTH_LONG).show()
            }
        }

        val btn_back_sms3: Button = findViewById(R.id.btn_back_sms3)
        btn_back_sms3.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults:
    IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myMessage()
            } else {
                Toast.makeText(this, "Nachricht konnte leider nicht versendet werden!",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    // STT
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        editTextMessage = findViewById(R.id.et_message)
        when (requestCode) {
            // Handle the result for our request code.
            REQUEST_CODE_STT -> {
                // Safety checks to ensure data is available.
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // Retrieve the result array.
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // Ensure result array is not null or empty to avoid errors.
                    if (!result.isNullOrEmpty()) {
                        // Recognized text is in the first position.
                        val recognizedText = result[0]
                        // Do what you want with the recognized text.
                        editTextMessage.setText(recognizedText)
                    }
                }
            }
        }
    }
    
    */

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}
