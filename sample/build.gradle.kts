plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ktlint.gradle)
}

android {
    compileSdk = 34
    namespace = "com.trendyol.android.devtools"

    defaultConfig {
        applicationId = "com.trendyol.android.devtools"
        minSdk = 21
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }


    buildFeatures {
        viewBinding = true
    }
    lint {
        checkDependencies = true
        htmlOutput = file("$rootDir/build/reports/android-lint.html")
        htmlReport = true
        xmlReport = false
    }
}

dependencies {
    debugImplementation(project(":libraries:autofill-service"))
    implementation(project(":libraries:environment-manager"))
    implementation(project(":libraries:debug-menu"))
    debugImplementation(project(":libraries:analytics-logger"))
    releaseImplementation(project(":libraries:analytics-logger-no-op"))
    debugImplementation(project(":libraries:http-inspector"))
    releaseImplementation(project(":libraries:http-inspector-no-op"))
    implementation(project(":libraries:view-inspector"))
    implementation(project(":libraries:debug-toast"))
    implementation(project(":libraries:deeplink-launcher"))
    implementation(project(":libraries:sharedpref-manager"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)
    implementation(libs.bundles.lifecycle)

    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.moshi.kotlin)

    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
