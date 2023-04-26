plugins {
    id("kotlinx.team.infra") version "0.3.0-dev-64"
    kotlin("multiplatform") apply false
}

infra {
    teamcity {
        libraryStagingRepoDescription = project.name
    }
    publishing {
        include(":kotlinx-datetime")
        libraryRepoUrl = "https://github.com/Kotlin/kotlinx-datetime"
        sonatype { }
    }
}

val mainJavaToolchainVersion by ext(project.property("java.mainToolchainVersion"))
val modularJavaToolchainVersion by ext(project.property("java.modularToolchainVersion"))

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

with(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin.apply(rootProject)) {
    //canary nodejs that supports wasm GC M6
    nodeVersion = "20.0.0-v8-canary2023041819be670741"
    nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
}


// Node with canary V8 version is not parsed correctly by NPM, producing errors like
//
//          error typescript@4.7.4: The engine "node" is incompatible with this module.
//                                  Expected version ">=4.2.0". Got "20.0.0-v8-canary***"
//
allprojects.forEach {
    it.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask>().configureEach {
        args.add("--ignore-engines")
    }
}
