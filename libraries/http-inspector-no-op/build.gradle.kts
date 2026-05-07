import com.trendyol.android.devtools.plugins.publish.defaultConfiguration

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.convention.publish)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toString().toInt())

android {
    compileSdk = 35
    namespace = "com.trendyol.android.devtools.httpinspector.noop"

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
        // aarMetadata.minCompileSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

group = "com.trendyol.android.devtools"
version = "0.3.0"

publishConfig {
    defaultConfiguration(
        artifactId = "http-inspector-no-op",
        description = "Android Http Inspector No-Op"
    )
}

dependencies {
    implementation(libs.okhttp)
}
