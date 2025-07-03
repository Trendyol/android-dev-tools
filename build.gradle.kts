// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ktlint.gradle) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.gradle.nexus.publish.plugin)
}

apply(from = "${rootDir}/scripts/publish-root.gradle")

