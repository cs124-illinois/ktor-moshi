plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
    signing
    id("org.jmailen.kotlinter")
}
dependencies {
    val ktorVersion = "3.0.0"
    kaptTest("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okio:okio:3.9.1")

    testImplementation(kotlin("test"))
    testImplementation("com.google.truth:truth:1.4.4")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
}
tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    artifacts {
        add("archives", sourcesJar)
    }
}
java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
publishing {

}
kapt {
    useBuildCache = true
    includeCompileClasspath = false
}
kotlin {
    kotlinDaemonJvmArgs = listOf("-Dfile.encoding=UTF-8")
}
publishing {
    publications {
        create<MavenPublication>("ktor-moshi") {
            artifactId = "ktor-moshi"
            from(components["java"])
            pom {
                name = "ktor-moshi"
                description = "Moshi ktor plugin for use by CS 124 servers."
                url = "https://cs124.org"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit/"
                    }
                }
                developers {
                    developer {
                        id = "gchallen"
                        name = "Geoffrey Challen"
                        email = "challen@illinois.edu"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/cs124-illinois/ktor-moshi.git"
                    developerConnection = "scm:git:https://github.com/cs124-illinois/ktor-moshi.git"
                    url = "https://github.com/cs124-illinois/ktor-moshi"
                }
            }
        }
    }
}
signing {
    sign(publishing.publications["ktor-moshi"])
}


