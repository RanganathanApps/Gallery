import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
        // Add repository
        maven(url = "https://maven.fabric.io/public")

    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.40")
        classpath("com.google.gms:google-services:4.3.0")
        classpath("io.fabric.tools:gradle:1.31.0")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }

    allprojects {
        repositories {
            mavenLocal()
            google()
            jcenter()
            maven(url = "https://jitpack.io")
            maven(url = "https://maven.google.com")
           
            //start here
            configurations.all {
                resolutionStrategy.eachDependency {
                    /*requested = details.requested
                    if (requested.group .equals('com.google.android.gms')) {
                        details.useVersion("12.0.1")
                    }
                    if (requested.group .equals('com.google.firebase')) {
                        details.useVersion("12.0.1")
                    }*/

                }
            }
        }
    }


}

