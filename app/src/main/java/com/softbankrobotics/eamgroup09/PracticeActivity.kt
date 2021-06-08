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


class PracticeActivity: RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "FragmentActivity"
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)
    lateinit var topWords : Topic
    lateinit var topSentences : Topic
    lateinit var topLetters : Topic
    lateinit var topComp: Topic
    lateinit var topStart : Topic
    lateinit var practiceChatbot: QiChatbot
    lateinit var chat : Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this )
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.OVERLAY)
        setContentView(R.layout.activity_practice)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {

        // Set back button in listening state - on click change back to main
        val backButton3: Button = findViewById(R.id.btn_back3)
        backButton3.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
        // Put chat action into worker thread manually
        thread { beginChat(qiContext) }
    }

    // Function that runs complete chat action
    fun beginChat(qiContext: QiContext?) {
        val showText = findViewById<TextView>(R.id.tv_tabletLabelPract)
        val showHint = findViewById<TextView>(R.id.tv_hintLable)
        val showCorrect = findViewById<TextView>(R.id.tv_correct)

        // Build the topics with qiContext, resource and language config
        topStart = TopicBuilder.with(qiContext).withResource(R.raw.top_practice_start).build()
        topWords = TopicBuilder.with(qiContext).withResource(R.raw.top_p_words).build()
        topSentences= TopicBuilder.with(qiContext).withResource(R.raw.top_p_sentences).build()
        topLetters = TopicBuilder.with(qiContext).withResource(R.raw.top_p_letters).build()
        topComp = TopicBuilder.with(qiContext).withResource(R.raw.top_p_comp_sentence) .build()

        // Build chatbot with qiContext, topics and language config
        practiceChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topStart, topWords, topSentences, topLetters, topComp).build()


        practiceChatbot.variable("hint").addOnValueChangedListener {
            runOnUiThread {
                showHint.text = it
            }
        }
        practiceChatbot.variable("show").addOnValueChangedListener {
            runOnUiThread {
                showText.text = it
            }
        }
        practiceChatbot.variable("correct").addOnValueChangedListener {
            runOnUiThread {
                showCorrect.text = it
            }
        }

        chat = ChatBuilder.with(qiContext).withChatbot(practiceChatbot).withLocale(locale).build()
        chat.addOnStartedListener { goToBookmark("START") }
        val fchat: Future<Void> = chat.async().run()

        // Stop the chat when the qichatbot is done
        practiceChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
    }

    private fun goToBookmark(bookmarkName : String) {
        practiceChatbot.goToBookmark (
            topStart.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }


}
