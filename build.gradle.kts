import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0" apply false
    id("org.jmailen.kotlinter") version "4.4.0" apply false
    id("com.github.ben-manes.versions") version "0.51.0"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}
allprojects {
    group = "org.cs124"
    version = "2024.6.0"
}
subprojects {
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
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
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
