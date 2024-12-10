plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.capstone.diabite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.diabite"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        multiDexEnabled = true

        buildConfigField("String", "BASE_URL", "\"https://serpapi.com/\"")
        buildConfigField("String", "BASE_URL2", "\"http://107.175.0.251:5000/\"")
        buildConfigField("String", "BASE_URL3", "\"https://ai-trivia-questions-generator.p.rapidapi.com/\"")
        buildConfigField("String", "NEWS_API_KEY", "\"8366636bee785a14e736e3439b83ce9b2f24de5da7506821fa8fed4ac9abcb58\"")
        buildConfigField("String", "QUIZ_API_KEY", "\"8d290ed7a9mshf5392919b361003p111ccajsnd7e8f2eade26\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"AIzaSyAe7bWtEGEOkeWhGBPeXeE1oe5YSrJktIY\"")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.circleimageview)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)

    implementation (libs.androidx.viewpager2)
    implementation(libs.lottie)
    implementation(libs.glide)

    implementation(libs.androidx.room.runtime)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.datastore.preferences)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.generativeai)
    implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")

    implementation(libs.ucrop)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}