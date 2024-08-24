@file:Suppress("DEPRECATION")

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "8.1.4" apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("androidx.room") version "2.6.0" apply false

}
buildscript {
    extra.apply {
        set("room_version", "2.6.0")
    }
    val agp_version by extra("8.4.2")
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}