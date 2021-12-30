plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildToolsVersion = AppConfig.buildToolsVersion
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
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