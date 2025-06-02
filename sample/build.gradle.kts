plugins {
    kotlin("jvm")
    application
    id("org.jmailen.kotlinter")
    id("com.google.devtools.ksp")
}
dependencies {
    val ktorVersion = "3.1.3"
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    implementation(project(":library"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("com.squareup.moshi:moshi-adapters:1.15.2")
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.4.4")
}