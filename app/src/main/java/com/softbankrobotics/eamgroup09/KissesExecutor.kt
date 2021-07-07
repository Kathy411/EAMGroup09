package com.softbankrobotics.eamgroup09

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor

// CREATE Executor class, IMPLEMENT functions runWith and stop
// CALL Animation function -> see file AnimationFunctions.kt

class KissesExecutor(qiContext: QiContext?) :
        BaseQiChatExecutor(qiContext) {
    override fun runWith(params: List<String>) {
        kisses(qiContext)
    }
    override fun stop() {}
}
