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
    namespace = "com.trendyol.android.devtools.debugmenu"

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

    buildFeatures {
        viewBinding = true
    }
}

group = "com.trendyol.android.devtools"
version = "0.6.0"

publishConfig {
    defaultConfiguration(
        artifactId = "debug-menu",
        description = "Android Debug Menu"
    )
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.android.material)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.embeddedKoinCore)
    implementation(libs.embeddedKoinAndroid)
}
