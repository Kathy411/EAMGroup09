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