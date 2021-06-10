package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class SmsActivity: RobotActivity(), RobotLifecycleCallbacks {
    // Declare regular and late initializing variables for this class
    val TAG = "FragmentActivity"
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)
    lateinit var topSms : Topic
    lateinit var smsChatbot: QiChatbot
    lateinit var chat : Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this )
        setContentView(R.layout.activity_sms)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        // Declare Back Button / OnClick -> revert to MainActivity
        val backButton5: Button = findViewById(R.id.btn_back5)
        backButton5.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

        // ** Chat Action starts here **
        // BUILD topic(QiContext, Resource) and chatbot(QiContext, language config, topic)
        topSms = TopicBuilder.with(qiContext).withResource(R.raw.top_sms).build()
        smsChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topSms).build()

        // Animations used in this activity - mutable Map of QiChat-Variable and BaseQiChatExecutor
        val executors = hashMapOf(
            "hello" to HelloExecutor(qiContext),
            "nice" to NiceExecutor(qiContext)
        )
        // Set Executors to chatbot
        smsChatbot.executors = executors as Map<String, QiChatExecutor>?

        // Get QiChat-Variable, set its text to TextView
        // Action on UI process -> runOnUIThread
        val smsLabel = findViewById<TextView>(R.id.tv_smsTabletLabel)
        smsChatbot.variable("sms_text").addOnValueChangedListener {
            runOnUiThread {
                smsLabel.text = it
            }
        }

        // BUILD Chat (QiContext, qiChatbot, language config)
        chat = ChatBuilder.with(qiContext).withChatbot(smsChatbot).withLocale(locale).build()

        // Once chat is run, START at a certain bookmark -> goToBookmark()
        chat.addOnStartedListener { goToBookmark("READTOME") }

        // RUN chat asynchronously
        val fchat: Future<Void> = chat.async().run()

        // STOP the chat when the qichatbot reaches an ^endDiscuss-Tag
        smsChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
        }
    }

    // Once chat is run, this function is called to have the chatbot start at a certain bookmark)
    private fun goToBookmark(bookmarkName : String) {
        smsChatbot.goToBookmark (
            topSms.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}