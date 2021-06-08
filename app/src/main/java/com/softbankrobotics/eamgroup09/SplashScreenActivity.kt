package com.softbankrobotics.eamgroup09

import android.content.Intent
import android.os.Bundle
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy
import java.util.*

class SplashScreenActivity : RobotActivity() {

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.OVERLAY)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                goToMain()
            }
        }, 5000)  // 3 seconds time delay to show Splash
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }


    // goToMain enables the system, to move on to the next screen
    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
