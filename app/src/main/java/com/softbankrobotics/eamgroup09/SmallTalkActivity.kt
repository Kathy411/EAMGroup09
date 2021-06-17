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
import kotlin.concurrent.thread

class SmallTalkActivity: RobotActivity(), RobotLifecycleCallbacks {
    // Declare regular and late initializing variables for this class
    val TAG = "FragmentActivity"
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)
    lateinit var topSmallTalk : Topic
    lateinit var smallTalkChatbot: QiChatbot
    lateinit var chat : Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this )
        setContentView(R.layout.activity_small_talk)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        // Declare Back Button / OnClick -> revert to MainActivity
        val backButton6: Button = findViewById(R.id.btn_back6)
        backButton6.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
        // ** Chat Action starts here **
        // BUILD topic(QiContext, Resource) and chatbot(QiContext, language config, topic)
        topSmallTalk = TopicBuilder.with(qiContext).withResource(R.raw.top_small_talk).build()
        smallTalkChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topSmallTalk).build()

        val smallTalk = findViewById<TextView>(R.id.tv_smalltalk)
        smallTalkChatbot.variable("talk").addOnValueChangedListener {
            runOnUiThread {
                smallTalk.text = it
            }
        }


        // Animations used in this activity - mutable Map of QiChat-Variable and BaseQiChatExecutor
        val executors = hashMapOf(
                    "hello" to HelloExecutor(qiContext),
                    "nice" to NiceExecutor(qiContext),
                    "clapping" to ClappingExecutor(qiContext),
                    "dance" to DanceExecutor(qiContext)
            )
        // Set Executors to qiChatbot
        smallTalkChatbot.executors = executors as Map<String, QiChatExecutor>?

        // BUILD Chat (QiContext, qiChatbot, language config)
        chat = ChatBuilder.with(qiContext).withChatbot(smallTalkChatbot).withLocale(locale).build()
        chat.addOnStartedListener { goToBookmark("START") }
        // RUN chat asynchronously
        val fchat: Future<Void> = chat.async().run()

        // STOP the chat when the qichatbot reaches an ^endDiscuss-Tag
        smallTalkChatbot.addOnEndedListener { endReason ->
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
    // Once chat is run, this function is called to have the chatbot start at a certain bookmark)
    private fun goToBookmark(bookmarkName : String) {
        smallTalkChatbot.goToBookmark (
                topSmallTalk.bookmarks[bookmarkName],
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE)
    }
}