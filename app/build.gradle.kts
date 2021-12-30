plugins {
    id("com.android.application")
    kotlin("android")
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
    implementation(Dependencies.material)

    androidTestImplementation(Dependencies.junitExt)
    androidTestImplementation(Dependencies.espresso)

    testImplementation(Dependencies.junit)
}