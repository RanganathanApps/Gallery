plugins {

    id("com.android.application")
    id("io.fabric")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    kotlin("kapt")
}

android {
    compileSdkVersion(AppConfig.targetSdkVersion)
    flavorDimensions("default")
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdkVersion(AppConfig.minSdkVersion)
        targetSdkVersion(AppConfig.targetSdkVersion)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    dataBinding {
        isEnabled = true
    }
    productFlavors {
        create("demo") {

            applicationId = AppConfig.applicationId
        }
        create("production") {

            applicationId = AppConfig.applicationId
        }

    }
    signingConfigs {

        create("release") {
            storeFile = rootProject.file("gallery_and_photos.jks")
            storePassword = System.getenv("Ranga@9900")
            keyAlias = System.getenv("key0")
            keyPassword = System.getenv("Ranga@9900")
        }
    }
    buildTypes {

        getByName("debug") {
            resValue("string", "app_name", AppConfig.applicaionName)
            isDebuggable = true
        }


        create("qa") {
            isShrinkResources = true
            isMinifyEnabled = true
            isUseProguard = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            resValue("string", "app_type", "Debug")
            resValue("string", "app_name", AppConfig.applicaionName)
        }

        getByName("release") {

            resValue("string", "app_name", AppConfig.applicaionName)
            resValue("string", "app_type", "release")
            isUseProguard = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }


    }
    lintOptions {
        isCheckReleaseBuilds = false
        // Or, if you prefer, you can continue to check for errors in release builds,
        // but continue the build even when errors are found:
        isAbortOnError =  false
    }


    /*compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }*/
}


dependencies {
    /*kotlin*/
    implementation(Libs.Kotlin.kotlin_std)
    /*androidx*/
    implementation(Libs.Support.appcompat)
    implementation(Libs.Support.recyclerview)
    implementation(Libs.Support.constraint)
    implementation(Libs.material)
    implementation(Libs.Support.design)

    /*databinding, viewmodel and coroutines*/
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0")
    kapt(Libs.databinding)


    implementation(Libs.viewModelExt)
    implementation(Libs.coroutines)
    /*retrofit*/
    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.converter_moshi)
    implementation(Libs.Retrofit.retrofit2_kotlin_coroutines)
    implementation(Libs.Retrofit.retrofit2_converter_gson)
    implementation(Libs.Retrofit.logging_interceptor)

    /*dagger*/
    implementation(Libs.Dagger.core)
    kapt(Libs.Dagger.compiler)
    /*Picasso*/
    implementation(Libs.picasso)
    /*circleImageview*/
    implementation(Libs.circleimageview)
    /*palette*/
    implementation(Libs.palette)
    /*caligraphy*/
    //implementation(Libs.caligraphy)
    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:1.0.0")
    /*materialEditext*/
    implementation(Libs.materialEditext)
    /*sdp*/
    implementation(Libs.sdp)

    implementation("com.github.mukeshsolanki:android-otpview-pinview:2.0.3")
    implementation(Libs.lottie)
    //implementation(Libs.baseAppConfig)
    implementation("com.github.RanganathanApps:BaseConfigApp:0.0.33")

    implementation("com.felipecsl.asymmetricgridview:library:2.0.1")
    implementation("com.jsibbold:zoomage:1.2.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    /*sectioned grid recycler*/
    implementation ("com.github.zhukic:sectioned-recyclerview:1.2.3")
    /*firebase*/
    implementation(Libs.Firebase.remote_config)
    implementation(Libs.Firebase.firebase_core)
    implementation(Libs.Firebase.crashlytics)
    implementation(Libs.Firebase.messaging)
    implementation(Libs.Firebase.firestore)

    /*expandable recyclerview*/
    implementation("com.thoughtbot:expandablerecyclerview:1.3")
    implementation("com.thoughtbot:expandablecheckrecyclerview:1.4")
    /*multidex*/
    implementation("com.android.support:multidex:1.0.3")
    /*background task execution*/
    implementation("org.jetbrains.anko:anko-common:0.9")

    implementation("com.squareup.okio:okio:1.15.0")

}
apply(mapOf("plugin" to "com.google.gms.google-services"))

repositories {
    mavenCentral()
}
