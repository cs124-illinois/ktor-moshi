plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
    id("org.jmailen.kotlinter")
}
dependencies {
    kaptTest("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")

    implementation("io.ktor:ktor-server-core:1.6.7")
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
    implementation("com.squareup.okio:okio:3.0.0")

    testImplementation(kotlin("test"))
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.ktor:ktor-server-test-host:1.6.7")
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
}
publishing {
    publications {
        create<MavenPublication>("mave") {
            artifactId = "ktor-moshi"
            from(components["java"])
        }
    }
}
kapt {
    useBuildCache = true
    includeCompileClasspath = false
    javacOptions {
        option("--illegal-access", "permit")
    }
}
kotlin {
    kotlinDaemonJvmArgs = listOf("-Dfile.encoding=UTF-8", "--illegal-access=permit")
}