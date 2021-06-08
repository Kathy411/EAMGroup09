package com.softbankrobotics.eamgroup09

import android.os.Bundle
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.holder.AutonomousAbilitiesType
import com.aldebaran.qi.sdk.`object`.holder.Holder
import com.aldebaran.qi.sdk.builder.HolderBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class AutonomousAbilitiesActivity : RobotActivity(), RobotLifecycleCallbacks {

    // The holder for the abilities.
    private var holder: Holder? = null

    // The QiContext provided by the QiSDK.
    private var qiContext: QiContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register the RobotLifecycleCallbacks
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this)
        super.onDestroy()
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        // Store the provided QiContext.
        this.qiContext = qiContext
        holdAbilities(qiContext)
    }

    override fun onRobotFocusLost() {
        // Remove the QiContext.
        this.qiContext = null
    }

    override fun onRobotFocusRefused(reason: String) {
        // Nothing here.
    }

    private fun holdAbilities(qiContext: QiContext) {
        // Build and store the holder for the abilities.
        holder = HolderBuilder.with(qiContext)
            .withAutonomousAbilities(
                AutonomousAbilitiesType.BACKGROUND_MOVEMENT,
                AutonomousAbilitiesType.BASIC_AWARENESS,
                AutonomousAbilitiesType.AUTONOMOUS_BLINKING
            )
            .build()
    }
}