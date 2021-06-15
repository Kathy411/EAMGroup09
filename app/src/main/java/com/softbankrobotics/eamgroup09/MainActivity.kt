package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
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
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import kotlin.concurrent.thread

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {
    // Declaration of variables / late initialization variables
    val TAG = "FragmentActivity"
    lateinit var btn_practice : Button
    lateinit var btn_chat : Button
    lateinit var btn_quiz : Button
    lateinit var btn_newspaper : Button
    lateinit var btn_sms : Button
    lateinit var btn_country : Button
    lateinit var locale: Locale


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.OVERLAY)
        setContentView(R.layout.activity_main)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        // Set button variables to button element in layout
        btn_practice = findViewById<Button>(R.id.btn_practice)
        btn_chat = findViewById<Button>(R.id.btn_chat)
        btn_quiz = findViewById<Button>(R.id.btn_quiz)
        btn_newspaper = findViewById<Button>(R.id.btn_newspaper)
        btn_sms = findViewById<Button>(R.id.btn_sms)
        btn_country = findViewById<Button>(R.id.btn_country)

        // Configure language to German
        locale = Locale(Language.GERMAN, Region.GERMANY)

        // BUILD Welcome phrase with String Resource
        // BUILD Say Action and RUN it asynchronously
        val welcome: Phrase =
                Phrase(getString(R.string.Welcome))
        val say: Say = SayBuilder.with(qiContext).withPhrase(welcome).withLocale(locale).build()
        say.async().run()

        // Set buttons in listening mode / start Activity on Click
        btn_quiz.setOnClickListener() {
            val changeToQuiz = Intent(this, QuizActivity::class.java)
            startActivity(changeToQuiz)
        }

        btn_newspaper.setOnClickListener() {
            val changeToNewspaper = Intent(this, NewspaperActivity::class.java)
            startActivity(changeToNewspaper)
        }

        btn_practice.setOnClickListener() {
            val changeToPractice = Intent(this, PracticeActivity::class.java)
            startActivity(changeToPractice)
        }

        btn_chat.setOnClickListener() {
            val changeToSmallTalk = Intent(this, SmallTalkActivity::class.java)
            startActivity(changeToSmallTalk)
        }

        btn_sms.setOnClickListener() {
            val changeToSms = Intent(this, SmsActivity::class.java)
            startActivity(changeToSms)
        }

        btn_country.setOnClickListener() {
            val changeToCountry = Intent(this, CountryActivity::class.java)
            startActivity(changeToCountry)
        }

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