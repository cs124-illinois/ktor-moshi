import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20" apply false
    id("org.jmailen.kotlinter") version "4.0.0" apply false
    id("com.github.ben-manes.versions") version "0.49.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}
allprojects {
    group = "org.cs124"
    version = "2023.11.0"
}
subprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
    tasks.withType<JavaCompile> {
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
    tasks.withType<Test> {
        enableAssertions = true
        useJUnitPlatform()
        jvmArgs("-ea", "-Xmx1G", "-Xss256k")
    }
}
tasks.dependencyUpdates {
    fun String.isNonStable() = !(
        listOf("RELEASE", "FINAL", "GA", "JRE").any { uppercase().contains(it) }
            || "^[0-9,.v-]+(-r)?$".toRegex().matches(this)
        )
    rejectVersionIf { candidate.version.isNonStable() }
    gradleReleaseChannel = "current"
}
