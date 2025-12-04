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
    namespace = "com.trendyol.android.devtools.analyticslogger"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }
}

group = "com.trendyol.android.devtools"
version = "0.9.0"

publishConfig {
    defaultConfiguration(
        artifactId = "analytics-logger",
        description = "Analytics logger for Trendyol Android applications"
    )
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.moshi)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.embeddedKoinCore)
    implementation(libs.embeddedKoinAndroid)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}
