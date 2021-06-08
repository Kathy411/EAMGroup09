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
import kotlin.concurrent.thread

class SmallTalkActivity: RobotActivity(), RobotLifecycleCallbacks {
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
        val backButton6: Button = findViewById(R.id.btn_back6)
        backButton6.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }

            topSmallTalk = TopicBuilder.with(qiContext).withResource(R.raw.top_small_talk).build()
            smallTalkChatbot =
                    QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topSmallTalk).build()

            // TODO If animations are needed, pls add them to hashMap, create Executor & runnable function
            val executors = hashMapOf(
                    "hello" to HelloExecutor(qiContext),
                    "nice" to NiceExecutor(qiContext)
            )
            // Set Executors to qiChatbot
            smallTalkChatbot.executors = executors as Map<String, QiChatExecutor>?

            chat = ChatBuilder.with(qiContext).withChatbot(smallTalkChatbot).withLocale(locale).build()
            val fchat: Future<Void> = chat.async().run()

            // Stop the chat when the qichatbot is done
            smallTalkChatbot.addOnEndedListener { endReason ->
                Log.i(TAG, "qichatbot end reason = $endReason")
                fchat.requestCancellation()
            }
        }
    /* private fun goToBookmark(bookmarkName : String) {
        smsChatbot.goToBookmark (
            topSms.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }
     */

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}