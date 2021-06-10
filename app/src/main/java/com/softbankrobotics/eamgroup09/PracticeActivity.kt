package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.BOOKMARK
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
    // Declare regular and late initializing variables for this class
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
        // Declare Back Button / OnClick -> revert to MainActivity
        val backButton3: Button = findViewById(R.id.btn_back3)
        backButton3.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
        // Put chat action into worker thread manually
        // RUN beginChat Function, hosting the chat action
        thread { beginChat(qiContext) }
    }

    // Function that runs complete chat action
    fun beginChat(qiContext: QiContext?) {
        val showText = findViewById<TextView>(R.id.tv_tabletLabelPract)
        val showHint = findViewById<TextView>(R.id.tv_hintLable)
        val showCorrect = findViewById<TextView>(R.id.tv_correct)

        // BUILD topics(QiContext, Resource) - within the first topic user decides Word / Sentences / Letters / Clozes
        topStart = TopicBuilder.with(qiContext).withResource(R.raw.top_practice_start).build()
        topWords = TopicBuilder.with(qiContext).withResource(R.raw.top_p_words).build()
        topSentences= TopicBuilder.with(qiContext).withResource(R.raw.top_p_sentences).build()
        topLetters = TopicBuilder.with(qiContext).withResource(R.raw.top_p_letters).build()
        topComp = TopicBuilder.with(qiContext).withResource(R.raw.top_p_comp_sentence) .build()

        // BUILD Chatbot(QiContext, language config, all topics)
        practiceChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topStart, topWords, topSentences, topLetters, topComp).build()

        // GET QiChat-Variable $hint, SET its text to upper TextView in activity_practice
        // Action on UI process -> runOnUIThread
        practiceChatbot.variable("hint").addOnValueChangedListener {
            runOnUiThread {
                showHint.text = it
            }
        }

        // GET QiChat-Variable $show, SET its text to lower TextView in activity_practice
        // Action on UI process -> runOnUIThread
        practiceChatbot.variable("show").addOnValueChangedListener {
            runOnUiThread {
                showText.text = it
            }
        }

        // GET QiChat-Variable $correct, SET its text to second upper TextView in activity_practice
        // Action on UI process -> runOnUIThread
        practiceChatbot.variable("correct").addOnValueChangedListener {
            runOnUiThread {
                showCorrect.text = it
            }
        }

        // BUILD Chat (QiContext, qiChatbot, language config)
        chat = ChatBuilder.with(qiContext).withChatbot(practiceChatbot).withLocale(locale).build()

        // Once chat is run, START at a certain bookmark -> goToBookmark()
        chat.addOnStartedListener { goToBookmark("START") }

        // RUN chat asynchronously
        val fchat: Future<Void> = chat.async().run()

        // STOP the chat when the qichatbot reaches an ^endDiscuss-Tag
        practiceChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
    }

    // Once chat is run, this function is called to have the chatbot start at a certain bookmark)
    private fun goToBookmark(bookmarkName : String) {
        practiceChatbot.goToBookmark (
            topStart.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    override fun onRobotFocusLost() {
        Log.i(BOOKMARK, "Focus lost")
    }

    override fun onRobotFocusRefused(reason: String?) {
        Log.i(BOOKMARK, "Focus refused because $reason")
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this, this)
    }
}
