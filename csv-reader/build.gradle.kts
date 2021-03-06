plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
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

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltCompiler)

    androidTestImplementation(Dependencies.junitExt)
    androidTestImplementation(Dependencies.espresso)

    testImplementation(Dependencies.junit)
}