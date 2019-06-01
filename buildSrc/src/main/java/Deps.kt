object AppConfig {
    val buildToolsVersion = "28.0.0"
    val minSdkVersion = 16
    val targetSdkVersion = 28
    val compileSdkVersion = 28
    val applicationId = "apps.ranganathan.gallery"
    val versionCode = 1
    val versionName = "1.0"
    val applicaionName = "Photos & Gallery"
    val BaseAppTitle = "Names"
    val applicationIdTwo = "development.app.sample"
}

object Versions {

    const val kotlin_version = "1.3.21"
    const val gradle_version = "3.2.0"
    const val android_plugin_version = "3.1.0"
    const val support_lib = "27.1.0"
    const val appcompat = "1.0.2"
    const val retrofit = "2.4.0"
    const val rxjava = "2.1.9"
    const val constraint_layout = "1.1.3"
    const val junit = "4.12"
    const val junit_runner = "1.0.1"
    const val espresso_core = "3.0.1"

    val recyclerview_version = "1.0.0"
    val lifecycle_extensions_version = "2.0.0-beta01"
    val databinding_version = "3.2.0"
    val palette_version = "1.0.0"
    val dagger2_version = "2.16"
    val caligraphy_version = "2.3.0"
    val material_edittext_version = "2.1.4"
    val sdp_version = "1.0.6"
    val rtp_version = "1.1.0"
    val lottie_version = "3.0.0"

    val material_version = "1.0.0"
    val constraint_version = "1.1.3"

    val coroutines_version = "1.0.0"
    val picasso_version = "2.71828"
    val circle_imageview_version = "3.0.0"

    val retrofit_version = "2.5.0"
    val retrofit_gson_version = "2.3.0"
    val retrofit_adapter_version = "0.9.2"
    val moshi_version = "2.5.0"

    val retrofit_interceptor_version = "3.9.1"

    val test_junit_version = "4.12"
    val runner_version = "1.1.0-alpha4"
    val espresso_core_version = "3.1.0-alpha4"
    val firebase_auth = "16.2.1"
    val firebase_messaging = "17.6.0"
    val firebase_database = "16.1.0"
    val firebase_storage = "16.1.0"
    val firebase_remote_config = "16.5.0"
    val firebase_core = "16.0.8"
    val firebase_firestore = "18.2.0"
    val crashlytics_version = "2.9.9"

    val firebase_ml_vision = "19.0.3"
    val firebase_image_label = "17.0.2"


    val base_app_config = "0.0.11"
}

object Libs {

    object Dagger {
        val core = "com.google.dagger:dagger:${Versions.dagger2_version}"
        val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger2_version}"
        val android = "com.google.dagger:dagger-android:${Versions.dagger2_version}"
        val androidSupport = "com.google.dagger:dagger-DataBindingUtil-support:${Versions.dagger2_version}"
        val androidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger2_version}"
    }

    object Support {
        val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint_layout}"
        val design = "com.android.support:design:${Versions.support_lib}"
        val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview_version}"
        val cardview = "com.android.support:cardview-v7:${Versions.support_lib}"

    }

    object Retrofit {
        val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        val converter_moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit_version}"
        val retrofit2_kotlin_coroutines =
            "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofit_adapter_version}"
        val retrofit2_converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit_gson_version}"
        val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.retrofit_interceptor_version}"
    }

    object Firebase {
        val auth = "com.google.firebase:firebase-auth:${Versions.firebase_auth}"
        val messaging = "com.google.firebase:firebase-messaging:${Versions.firebase_messaging}"
        val database = "com.google.firebase:firebase-database:${Versions.firebase_database}"
        val storage = "com.google.firebase:firebase-storage:${Versions.firebase_storage}"
        val remote_config = "com.google.firebase:firebase-config:${Versions.firebase_remote_config}"
        val firebase_core = "com.google.firebase:firebase-core:${Versions.firebase_core}"
        val firestore = "com.google.firebase:firebase-firestore:${Versions.firebase_firestore}"
        val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics_version}"


    }

    object MLKit {
        val ml_vision = "com.google.firebase:firebase-ml-vision:${Versions.firebase_ml_vision}"
        val image_label = "com.google.firebase:firebase-ml-vision-image-label-model:${Versions.firebase_image_label}"
    }


    object Kotlin {
        val kotlin_std = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
    }

    object Test {
        val runner = "androidx.test:runner:${Versions.runner_version}"
        val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core_version}"
    }


    val baseAppConfig = "com.github.ranganathansamraj92:appconfig:${Versions.base_app_config}"

    val material = "com.google.android.material:material:${Versions.material_version}"


    val viewModelExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle_extensions_version}"

    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_version}"

    val databinding = "androidx.databinding:databinding-compiler:${Versions.databinding_version}"

    val picasso = "com.squareup.picasso:picasso:${Versions.picasso_version}"

    val circleimageview = "de.hdodenhof:circleimageview:${Versions.circle_imageview_version}"

    val palette = "androidx.palette:palette:${Versions.palette_version}"

    val caligraphy = "uk.co.chrisjenx:calligraphy:${Versions.caligraphy_version}"

    val materialEditext = "com.rengwuxian.materialedittext:library:${Versions.material_edittext_version}"

    val sdp = "com.intuit.sdp:sdp-android:${Versions.sdp_version}"

    val loading_button = "br.com.simplepass:loading-button-android:2.1.0"

    val runtimePermissions = "com.github.florent37:runtime-permission-kotlin:${Versions.rtp_version}"

    val lottie = "com.airbnb.android:lottie:${Versions.lottie_version}"

}