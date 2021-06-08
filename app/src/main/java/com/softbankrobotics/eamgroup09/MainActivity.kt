package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.Phrase
import com.aldebaran.qi.sdk.`object`.conversation.Say
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "FragmentActivity"
    //Set language to German
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.OVERLAY)
        setContentView(R.layout.activity_main)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        // declared buttons on main
        val btn_practice = findViewById<Button>(R.id.btn_practice)
        val btn_chat = findViewById<Button>(R.id.btn_chat)
        val btn_quiz = findViewById<Button>(R.id.btn_quiz)
        val btn_newspaper = findViewById<Button>(R.id.btn_newspaper)
        val btn_sms = findViewById<Button>(R.id.btn_sms)
        val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)

        // welcome phrase
        val welcome: Phrase =
                Phrase("Schön Dich zu sehen! Was möchtest Du heute machen? Wir können uns unterhalten oder wir üben! Bitte drücke den entsprechenden Button um anzufangen!")
        val say : Say =  SayBuilder.with(qiContext).withPhrase(welcome).withLocale(locale).build()
        say.async().run()


        // Set buttons in listening mode / start Action on Click
        btn_quiz.setOnClickListener(){
            val changeToQuiz = Intent(this, QuizActivity::class.java)
            startActivity(changeToQuiz)
        }

        btn_newspaper.setOnClickListener(){
            val changeToNewspaper = Intent(this, NewspaperActivity::class.java)
            startActivity(changeToNewspaper)
        }

        btn_practice.setOnClickListener(){
            val changeToPractice = Intent(this, PracticeActivity::class.java)
            startActivity(changeToPractice)
        }

        btn_chat.setOnClickListener(){
            val changeToSmallTalk = Intent(this, SmallTalkActivity::class.java)
            startActivity(changeToSmallTalk)
        }

        btn_sms.setOnClickListener(){
            val changeToSms = Intent(this, SmsActivity::class.java)
            startActivity(changeToSms)
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