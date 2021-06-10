package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatExecutor
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class CountryActivity: RobotActivity(), RobotLifecycleCallbacks {

    // Declare regular and late initializing variables for this class
    val TAG = "FragmentActivity"
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)
    lateinit var topCountry : Topic
    lateinit var countryChatbot: QiChatbot
    lateinit var chat : Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this )
        setContentView(R.layout.activity_country)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        // Declare Back Button / OnClick -> revert to MainActivity
        val backButton7: Button = findViewById(R.id.btn_back7)
        backButton7.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
        // ** Chat Action starts here **
        // BUILD topic(QiContext, Resource) and chatbot(QiContext, language config, topic)
        topCountry = TopicBuilder.with(qiContext).withResource(R.raw.top_country).build()
        countryChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topCountry).build()


        // Animations used in this activity - mutable Map of QiChat-Variable and BaseQiChatExecutor
        val executors = hashMapOf(
                "hello" to HelloExecutor(qiContext),
                "nice" to NiceExecutor(qiContext)
        )
        // Set Executors to qiChatbot
        countryChatbot.executors = executors as Map<String, QiChatExecutor>?

        // BUILD Chat (QiContext, qiChatbot, language config)
        chat = ChatBuilder.with(qiContext).withChatbot(countryChatbot).withLocale(locale).build()
        // RUN chat asynchronously
        val fchat: Future<Void> = chat.async().run()

        // STOP the chat when the qichatbot reaches an ^endDiscuss-Tag
        countryChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
        }
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}