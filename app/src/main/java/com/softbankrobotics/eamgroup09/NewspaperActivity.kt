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
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import kotlin.concurrent.thread

class NewspaperActivity: RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "FragmentActivity"
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)     //Set language to German
    lateinit var topNews : Topic
    lateinit var newsChatbot: QiChatbot
    lateinit var newsChat : Chat
    lateinit var newsLabel : TextView
    lateinit var readtomeLabel : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this )
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.OVERLAY)
        setContentView(R.layout.activity_newspaper)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {

        // SET Back button to change to Main on Click
        val backButton2: Button = findViewById(R.id.btn_back7)
        backButton2.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

        // PUT chat action in worker thread manually
        thread { beginNewsChat(qiContext) }
    }

        // START chat action
    fun beginNewsChat(qiContext: QiContext?) {
        newsLabel = findViewById<TextView>(R.id.tv_newsLable)
        readtomeLabel = findViewById<TextView>(R.id.tv_readtome)

        // BUILD topic with qiContext & Resource
        topNews = TopicBuilder.with(qiContext).withResource(R.raw.top_newspaper).build()
        // BUILD QiChatbot with qiContext, language config & topic
        newsChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topNews).build()


        // Animations used in this activity - mutable Map of QiChat-Variable and BaseQiChatExecutor
        val executors = hashMapOf(
            "hello" to HelloExecutor(qiContext),
            "nice" to NiceExecutor(qiContext),
            "clapping" to ClappingExecutor(qiContext)
        )
        // Set Executors to qiChatbot
        newsChatbot.executors = executors as Map<String, QiChatExecutor>?

        // Set QiChat Variable $headline to TextView
        newsChatbot.variable("headline").addOnValueChangedListener {
            runOnUiThread {
                newsLabel.text = it
            }
        }
        // Set QiChat Variable $readtome to TextView
        newsChatbot.variable("readtome").addOnValueChangedListener {
            runOnUiThread {
               readtomeLabel.text = it
            }
        }
        // BUILD Chat with qiContext, qiChatbot, language config
        newsChat = ChatBuilder.with(qiContext).withChatbot(newsChatbot).withLocale(locale).build()

        // SET defined bookmark to START the conversation
        newsChat.addOnStartedListener { goToBookmark("READTOME") }

        // RUN the chat asynchronously
        val fNchat: Future<Void> = newsChat.async().run()

        // STOP the chat when the chatbot is done
        newsChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fNchat.requestCancellation()
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
    }

    private fun goToBookmark(bookmarkName : String) {
        newsChatbot.goToBookmark (
            topNews.bookmarks[bookmarkName],
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