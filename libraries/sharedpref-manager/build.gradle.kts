import com.trendyol.android.devtools.plugins.publish.defaultConfiguration

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.convention.publish)
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toString().toInt())

android {
    namespace = "com.trendyol.android.devtools.sharedprefmanager"
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

group = "com.trendyol.android.devtools"
version = "0.3.1"

publishConfig {
    defaultConfiguration(
        artifactId = "sharedpref-manager",
        description = "Android SharedPreferences Manager"
    )
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.ktx)
}
