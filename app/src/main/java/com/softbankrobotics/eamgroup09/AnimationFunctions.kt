package com.softbankrobotics.eamgroup09

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder

fun kisses(qiContext: QiContext) {
    val kissesAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_kisses)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val kissesAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(kissesAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    kissesAnimate.run()
}

fun nice(qiContext: QiContext) {
    val niceAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_nice)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val niceAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(niceAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    niceAnimate.run()
}

fun affirm(qiContext: QiContext) {
    val affirmAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_affirm)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val affirmAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(affirmAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    affirmAnimate.run()
}

fun enum(qiContext: QiContext) {
    val enumAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_enum)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val enumAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(enumAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    enumAnimate.run()
}

fun disappointed(qiContext: QiContext) {
    val disappointedAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_disappointed)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val disappointedAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(disappointedAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    disappointedAnimate.run()
}

fun showTablet(qiContext: QiContext) {
    val showTabletAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_show_tablet)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val showTabletAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(showTabletAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    showTabletAnimate.run()
}

fun question(qiContext: QiContext) {
    val questionAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_question)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val questionAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(questionAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    questionAnimate.run()
}

fun thatsIt(qiContext: QiContext) {
    val thatsItAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_thats_it)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val thatsItAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(thatsItAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    thatsItAnimate.run()
}

fun yeah(qiContext: QiContext) {
    val yeahAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_yeah)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val yeahAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(yeahAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    yeahAnimate.run()
}


fun basketball(qiContext: QiContext) {
    val basketballAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_basketball)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val basketballAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(basketballAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    basketballAnimate.run()
}

fun hello(qiContext: QiContext) {
    val helloAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_hello)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val helloAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(helloAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    helloAnimate.run()
}

fun clapping(qiContext: QiContext) {
    val clappingAnim: Animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
            .withResources(R.raw.anim_clap)                    // Set the animation resource.
            .build()                                            // Build the animation.

    // Create an animate action.
    val clappingAnimate: Animate = AnimateBuilder.with(qiContext)  // Create the builder with the context.
            .withAnimation(clappingAnim)                           // Set the animation.
            .build()                                            // Build the animate action.
    clappingAnimate.run()
}