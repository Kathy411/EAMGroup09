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
        val backButton5: Button = findViewById(R.id.btn_back5)
        backButton5.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

        // Chat Action - Topic top_quiz TODO
        topSms = TopicBuilder.with(qiContext).withResource(R.raw.top_sms).build()
        smsChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topSms).build()

        // In case animations are needed, pls add to the hashMap, also add Executor and Runnable
        val executors = hashMapOf(
            "hello" to HelloExecutor(qiContext),
            "nice" to NiceExecutor(qiContext)
        )
        // Set Executors to qiChatbot -> NOTHING TO DO!
        smsChatbot.executors = executors as Map<String, QiChatExecutor>?

        // Set QiChat Variable to TextView
        val smsLabel = findViewById<TextView>(R.id.tv_smsTabletLabel)
        smsChatbot.variable("sms_text").addOnValueChangedListener {
            runOnUiThread {
                smsLabel.text = it
            }
        }
        // build Chat (QiContextm qiChatbot, Locale) -> NOTHING TO DO!
        chat = ChatBuilder.with(qiContext).withChatbot(smsChatbot).withLocale(locale).build()
        chat.addOnStartedListener { goToBookmark("READTOME") }

        val fchat: Future<Void> = chat.async().run()

        // Stop the chat when the qichatbot is done
        // TODO Use End reason in the topic -> Command ^endDiscuss(chooseEndReason)
        smsChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
        }
    }

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