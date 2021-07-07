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

    // Declaration of variables for this class
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

        // Show keyboard when edit text "Number" is active
        editTextNumber = findViewById(R.id.et_number)
        editTextNumber.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSoftKeyboard(editTextNumber)
            }
        }
        editTextMessage = findViewById(R.id.et_message)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {

        // Implement back button for this Activity -> On Click, change back to MainActivity
        val backButton8: Button = findViewById(R.id.btn_back8)
        backButton8.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

        // Language configuration
        locale = Locale(Language.GERMAN, Region.GERMANY)

        // BUILD phrase -> verbal Robot output
        val smsPhrase: Phrase = Phrase("Von hier aus kannst Du SMS versenden. Derzeit musst Du Text und Nummer noch eingeben, " +
                "aber meine Programmierer Innen arbeiten an der Spracheingabe. Wenn Du fertig bist, drücke einfach auf Senden")
        val smsSay = SayBuilder.with(qiContext).withPhrase(smsPhrase).withLocale(locale).build()

        // RUN phrase asynchronously
        smsSay.async().run()

        // Implement "SEND" button
         btn_send = findViewById(R.id.btn_send)
         btn_send.setOnClickListener {
            sendMessage()
        }

        // Following code was supposed to implement Android STT - worked in a separate app, not within this activity tho... working on it :)

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
        // SHOW softkeyboard, once EditText got the focus
        if (view.requestFocus()) {
            val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun sendMessage() {
        // CHECK if app got uses permission - if so, call Function myMessage()
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

        // Show toast, if number and/or text message EditTexts are empty
        if (toNumber == "" || txtMessage == "") {
            Toast.makeText(this, "Nummern- und Textfeld darf nicht leer sein", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(toNumber)) {

        // IF both EditTexts are filled, proceed to send the message and show "sent"-screen
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(toNumber, null, txtMessage, null, null)
                setContentView(R.layout.activity_sms_sent)

            } else {
                // IF letters are found in the number EditText, show Toast
                Toast.makeText(this, "Ins Nummernfeld bitte nur Zahlen eingeben!", Toast.LENGTH_LONG).show()
            }
        }

        // IMPLEMENT Back button for the activity
        val btn_back_sms3: Button = findViewById(R.id.btn_back_sms3)
        btn_back_sms3.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults:
    IntArray) {
        // CALL super on Permission Results
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            // IF Permission is granted proceed to myMessage()
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myMessage()
            } else {
            // IF Permission denied, show Toast
                Toast.makeText(this, "Nachricht konnte leider nicht versendet werden!",
                        Toast.LENGTH_SHORT).show()
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
