plugins {
    kotlin("jvm")
    kotlin("kapt")
    application
    id("org.jmailen.kotlinter")
}
dependencies {
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    implementation(project(":library"))
    implementation("io.ktor:ktor-server-netty:2.3.5")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.5")
    implementation("io.ktor:ktor-server-call-logging:2.3.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.squareup.moshi:moshi-adapters:1.15.0")
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.5")
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
