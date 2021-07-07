package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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

class QuizActivity: RobotActivity(), RobotLifecycleCallbacks {
    // Declare lateinit variables for the activity
    val TAG = "FragmentActivity"
    val locale: Locale = Locale(Language.GERMAN, Region.GERMANY)   //Set language to German
    lateinit var topQuiz: Topic
    lateinit var topLanderquiz: Topic
    lateinit var topSehenwurdigkeiten: Topic
    lateinit var quizChatbot: QiChatbot
    lateinit var chat: Chat
    lateinit var hinweistext: TextView
    lateinit var fragetext: TextView
    lateinit var imageSehenswurdigkeit: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setContentView(R.layout.activity_quiz)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        // SET Back button for the Activity
        val backButton1: Button = findViewById(R.id.btn_back1)
        backButton1.setOnClickListener {
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
        // Put chat action into worker thread manually
        thread {beginQuiz(qiContext)}
    }

    private fun beginQuiz(qiContext: QiContext?) {

        // LINK TextViews to Code
        hinweistext = findViewById(R.id.hinweistext)
        fragetext = findViewById(R.id.fragetext)

        // BUILD the topics with qiContext & Resource
        topQuiz = TopicBuilder.with(qiContext).withResource(R.raw.top_quiz).build()
        topLanderquiz = TopicBuilder.with(qiContext).withResource(R.raw.top_laender).build()
        topSehenwurdigkeiten = TopicBuilder.with(qiContext).withResource(R.raw.top_sehenswuerdigkeiten).build()

        // BUILD the chatbot with qiContext, language configuration, topics
        quizChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale)
                .withTopic(topQuiz, topLanderquiz, topSehenwurdigkeiten).build()

        // BUILD Chat with qiContext, chatbot, language configurations
        chat = ChatBuilder.with(qiContext).withChatbot(quizChatbot).withLocale(locale).build()
        // SET begin of Conversation to bookmark "STARTQUIZ"
        chat.addOnStartedListener { goToBookmark("STARTQUIZ") }

        // WHEN Function of reached QiChat Bookmarks to ImageFunctions
        quizChatbot.addOnBookmarkReachedListener {
            when (it.name) {
                //ab hier: Sehenswürdigkeitenquiz
                "NORWEGEN_VISIBLE"  ->  setImageNorwegen(R.id.image_sehenswurdigkeiten)
                "VENEDIG_VISIBLE"   ->  setImageVenedig(R.id.image_sehenswurdigkeiten)
                "BRASILIEN_VISIBLE" ->  setImageBrasilien(R.id.image_sehenswurdigkeiten)
                "CHEOPS_VISIBLE"    ->  setImageCheops(R.id.image_sehenswurdigkeiten)
                "INDIEN_VISIBLE"    ->  setImageIndien(R.id.image_sehenswurdigkeiten)
                "MEKKA_VISIBLE"     ->  setImageMekka(R.id.image_sehenswurdigkeiten)
                "DUBAI_VISIBLE"     ->  setImageDubai(R.id.image_sehenswurdigkeiten)
                "ISRAEL_VISIBLE"    -> setImageIsrael(R.id.image_sehenswurdigkeiten)
                "ULM_VISIBLE"       -> setImageUlm(R.id.image_sehenswurdigkeiten)
                "PARIS_VISIBLE"     -> setImageParis(R.id.image_sehenswurdigkeiten)
                "LONDON_VISIBLE"    -> setImageLondon(R.id.image_sehenswurdigkeiten)
                "FREIHEITSSTATUE_VISIBLE"    -> setImageFreiheitsstatue(R.id.image_sehenswurdigkeiten)
                //ab hier: Länderquiz
                "LAND_ARGENTINIEN_VISIBLE"  -> setImageLandArgentinen(R.id.image_sehenswurdigkeiten)
                "LAND_ALBANIEN_VISIBLE"     -> setImageLandAlbanien(R.id.image_sehenswurdigkeiten)
                "LAND_AUSTRALIEN_VISIBLE"   -> setImageLandAustralien(R.id.image_sehenswurdigkeiten)
                "LAND_BAHAMAS_VISIBLE"      -> setImageLandBahamas(R.id.image_sehenswurdigkeiten)
                "LAND_BELGIEN_VISIBLE"      -> setImageLandBelgien(R.id.image_sehenswurdigkeiten)
                "LAND_BRASILIEN_VISIBLE"    -> setImageLandBrasilien(R.id.image_sehenswurdigkeiten)
                "LAND_ELFENBEINKUESTE_VISIBLE"  -> setImageLandElfenbeinkueste(R.id.image_sehenswurdigkeiten)
                "LAND_GHANA_VISIBLE"        -> setImageLandGhana(R.id.image_sehenswurdigkeiten)
                "LAND_JAPAN_VISIBLE"        -> setImageLandJapan(R.id.image_sehenswurdigkeiten)
                "LAND_KANADA_VISIBLE"       -> setImageLandKanada(R.id.image_sehenswurdigkeiten)
                "LAND_KENIA_VISIBLE"        -> setImageLandKenia(R.id.image_sehenswurdigkeiten)
                "LAND_NORWEGEN_VISIBLE"     -> setImageLandNorwegen(R.id.image_sehenswurdigkeiten)
                "LAND_OESTERREICH_VISIBLE"  -> setImageLandOesterreich(R.id.image_sehenswurdigkeiten)
                "LAND_PORTUGAL_VISIBLE"     -> setImageLandPortugal(R.id.image_sehenswurdigkeiten)
                "LAND_SCHOTTLAND_VISIBLE"   -> setImageLandSchottland(R.id.image_sehenswurdigkeiten)
                "LAND_SCHWEDEN_VISIBLE"     -> setImageLandSchweden(R.id.image_sehenswurdigkeiten)
                "LAND_SCHWEIZ_VISIBLE"      -> setImageLandSchweiz(R.id.image_sehenswurdigkeiten)
                "LAND_SUEDAFRIKA_VISIBLE"   -> setImageLandSuedafrika(R.id.image_sehenswurdigkeiten)
                "LAND_TUERKEI_VISIBLE"      -> setImageLandTuerkei(R.id.image_sehenswurdigkeiten)
                "LAND_WALES_VISIBLE"        -> setImageLandWales(R.id.image_sehenswurdigkeiten)
            }
        }

        // SHOW QiChat Text Variable $hinweis in TextView
        quizChatbot.variable("hinweis").addOnValueChangedListener {
            runOnUiThread {
                hinweistext.text = it
            }
        }
        // SHOW QiChat Text Variable $frage in TextView
        quizChatbot.variable("frage").addOnValueChangedListener {
            runOnUiThread {
                fragetext.text = it
            }
        }

        // Animations used in this activity - mutable Map of QiChat-Variable and BaseQiChatExecutor
        val executors = hashMapOf(
                "hello" to HelloExecutor(qiContext),
                "nice" to NiceExecutor(qiContext),
                "clap" to ClappingExecutor(qiContext)
        )
        // Set Executors to qiChatbot
        quizChatbot.executors = executors as Map<String, QiChatExecutor>?

        // RUN Chat asynchronously
        val fchat: Future<Void> = chat.async().run()

        // Stop the chat when the qichatbot is done, change to MainActivity
        quizChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
            val changeToMain = Intent(this, MainActivity::class.java)
            startActivity(changeToMain)
        }
    }



    private fun goToBookmark(bookmarkName: String) {
        quizChatbot.goToBookmark(
                topQuiz.bookmarks[bookmarkName],
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE
        )
    }

    ////// SET Image sources  /////
    //to follow: Sehenswürdigkeitenquiz
    private fun setImageNorwegen(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_norwegen)
        }
    }
    private fun setImageVenedig(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_venedig)
        }
    }
    private fun setImageBrasilien(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_brasilien)
        }
    }
    private fun setImageCheops(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_cheops)
        }
    }
    private fun setImageIndien(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_indien)
        }
    }
    private fun setImageMekka(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_mekka)
        }
    }
    private fun setImageDubai(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_dubai)
        }
    }
    private fun setImageIsrael(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_israel)
        }
    }
    private fun setImageUlm(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_ulm)
        }
    }
    private fun setImageParis(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_paris)
        }
    }
    private fun setImageLondon(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_london)
        }
    }
    private fun setImageFreiheitsstatue(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.sehenswurdigkeit_freiheitsstatue)
        }
    }

    //to follow: Länderquiz
    private fun setImageLandArgentinen(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_argentinien)
        }
    }
    private fun setImageLandAlbanien(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_albanien)
        }
    }
    private fun setImageLandAustralien(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_australien)
        }
    }
    private fun setImageLandBahamas(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_bahamas)
        }
    }
    private fun setImageLandBelgien(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_belgien)
        }
    }
    private fun setImageLandBrasilien(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_brasilien)
        }
    }
    private fun setImageLandElfenbeinkueste(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_elfenbeinkueste)
        }
    }
    private fun setImageLandGhana(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_ghana)
        }
    }
    private fun setImageLandJapan(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_japan)
        }
    }
    private fun setImageLandKanada(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_kanada)
        }
    }
    private fun setImageLandKenia(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_kenia)
        }
    }
    private fun setImageLandNorwegen(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_norwegen)
        }
    }
    private fun setImageLandOesterreich(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_oesterreich)
        }
    }
    private fun setImageLandPortugal(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_portugal)
        }
    }
    private fun setImageLandSchottland(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_schottland)
        }
    }
    private fun setImageLandSchweden(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_schweden)
        }
    }
    private fun setImageLandSchweiz(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_schweiz)
        }
    }
    private fun setImageLandSuedafrika(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_suedafrika)
        }
    }
    private fun setImageLandTuerkei(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_tuerkei)
        }
    }
    private fun setImageLandWales(resource : Int) {
        runOnUiThread {
            imageSehenswurdigkeit = findViewById(R.id.image_sehenswurdigkeiten)
            imageSehenswurdigkeit.setImageResource(R.drawable.laender_wales)
        }
    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this,this )
    }

}

