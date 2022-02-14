plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("kapt")
}

android {
    buildToolsVersion = AppConfig.buildToolsVersion
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.rbrauwers.investments"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
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

    compileOptions {
        sourceCompatibility = AppConfig.java
        targetCompatibility = AppConfig.java
    }

    kotlinOptions {
        jvmTarget = "${AppConfig.java}"
    }
}

dependencies {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.hilt)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.material)
    implementation(Dependencies.navFragment)
    implementation(Dependencies.recyclerView)
    implementation(project(":csv-reader"))
    implementation(project(":exchange-service"))

    kapt(Dependencies.hiltCompiler)

    androidTestImplementation(Dependencies.junitExt)
    androidTestImplementation(Dependencies.espresso)

    testImplementation(Dependencies.junit)
}

kapt {
    correctErrorTypes = true
}