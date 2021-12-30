import org.gradle.api.JavaVersion

object AppConfig {
    const val compileSdk = 31
    const val targetSdk = 31
    const val minSdk = 28
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val buildToolsVersion = "31.0.0"
    val java = JavaVersion.VERSION_11

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}