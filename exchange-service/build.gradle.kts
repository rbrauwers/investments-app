import configs.builder.getExchangeRatesApiKey
import configs.builder.testingDebug

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

        buildConfigField(
            "String",
            "EXCHANGE_RATES_API_KEY",
            getExchangeRatesApiKey(rootProject.file(projectDir.path + "/configs.properties")))
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

    testingDebug(projectDir.path + "/configs.properties")
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltCompiler)

    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLogging)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitMoshi)

    androidTestImplementation(Dependencies.junitExt)
    androidTestImplementation(Dependencies.espresso)

    testImplementation(Dependencies.junit)
}