import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("org.jetbrains.dokka") version "1.9.20"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "com.tecknobit.ametistaengine"
version = "1.0.0"

kotlin {
    jvm {
        compilations.all {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            this@jvm.compilerOptions {
                jvmTarget.set(JvmTarget.JVM_18)
            }
        }
    }
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
    }
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.apiManager)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.io.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.napier)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.oshi.core)
                implementation(libs.ktor.client.apache5)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.kdeviceinfo)
                implementation(libs.ktor.client.darwin)
            }
        }
        val wasmJsMain by getting {
            dependencies {
            }
        }
    }
    jvmToolchain(18)
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        )
    )
    coordinates(
        groupId = "io.github.n7ghtm4r3",
        artifactId = "Ametista-Engine",
        version = "1.0.0"
    )
    pom {
        name.set("Ametista-Engine")
        // TODO: TO SET
        //description.set("Utilities for clients with an architecture based on SpringBoot and Jetpack Compose frameworks. Is a support library to implement some utilities for the clients and some default composable such OutlinedTextField, AlertDialogs and different others")
        inceptionYear.set("2024")
        url.set("https://github.com/N7ghtm4r3/Ametista-Engine")

        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://opensource.org/license/apache-2-0")
            }
        }
        developers {
            developer {
                id.set("N7ghtm4r3")
                name.set("Manuel Maurizio")
                email.set("maurizio.manuel2003@gmail.com")
                url.set("https://github.com/N7ghtm4r3")
            }
        }
        scm {
            //url.set("https://github.com/N7ghtm4r3/Ametista-Engine")
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

android {
    namespace = "com.tecknobit.ametistaengine"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
