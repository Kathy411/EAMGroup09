package com.softbankrobotics.eamgroup09

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class SmsActivity : RobotActivity(), RobotLifecycleCallbacks {
    lateinit var sendButton: Button
    lateinit var editTextNumber: EditText
    lateinit var editTextMessage: EditText
    lateinit var locale : Locale
    private val permissionRequest = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        QiSDK.register(this, this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)
        editTextNumber = findViewById(R.id.et_number)
        showSoftKeyboard(editTextNumber)
        editTextMessage = findViewById(R.id.et_message)
        showSoftKeyboard(editTextMessage)
        sendButton = findViewById(R.id.btn_send)

    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val backButton8: Button = findViewById(R.id.btn_back8)
        backButton8.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

        locale = Locale(Language.GERMAN, Region.GERMANY)
        val smsPhrase : Phrase = Phrase("Von hier aus kannst Du SMS versenden. Derzeit musst Du Text und Nummer noch eingeben, " +
                "aber meine Programmierer Innen arbeiten an der Spracheingabe. Wenn Du fertig bist, drücke einfach auf Senden")
        val smsSay = SayBuilder.with(qiContext).withPhrase(smsPhrase).withLocale(locale).build()

        smsSay.async().run()

        sendButton.setOnClickListener {
            sendMessage()
            //   }
        }
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
                Toast.makeText(this, "Nachricht versendet", Toast.LENGTH_LONG).show()
                editTextMessage.text.clear()
                editTextNumber.text.clear()
            } else {
                Toast.makeText(this, "Ins Nummernfeld bitte nur Zahlen eingeben!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults:
    IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myMessage();
            } else {
                Toast.makeText(this, "Nachricht konnte leider nicht versendet werden!",
                        Toast.LENGTH_SHORT).show();
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
