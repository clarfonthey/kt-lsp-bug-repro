plugins {
    val kotlinVersion: String by System.getProperties()
    id("java")
    id("fabric-loom")
    kotlin("jvm").version(kotlinVersion)
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}

val modVersion: String by project
val mavenGroup: String by project
version = modVersion
group = mavenGroup
minecraft {}
repositories {
    maven("https://maven.fabricmc.net") {
        name = "Fabric"
    }
    maven("https://masa.dy.fi/maven") {
        name = "Masa"
    }
    mavenCentral()
    gradlePluginPortal()
}

val carpetVersion: String by project
val fabricKotlinVersion: String by project
val fabricVersion: String by project
val javaVersion: String by project
val loaderVersion: String by project
val minecraftVersion: String by project
dependencies {
    val yarnMappings: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    modImplementation("carpet:fabric-carpet:$minecraftVersion-$carpetVersion")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        options.release.set(javaVersion.toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    jar {
        from("LICENSE.md") {
            rename { "${it}_${base.archivesName}" }
        }
    }
    processResources {
        inputs.property("fabricKotlinVersion", fabricKotlinVersion)
        inputs.property("fabricVersion", fabricVersion)
        inputs.property("javaVersion", javaVersion)
        inputs.property("loaderVersion", loaderVersion)
        inputs.property("minecraftVersion", minecraftVersion)
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "fabricKotlinVersion" to fabricKotlinVersion,
                    "fabricVersion" to fabricVersion,
                    "javaVersion" to javaVersion,
                    "loaderVersion" to loaderVersion,
                    "minecraftVersion" to minecraftVersion,
                    "version" to project.version,
                )
            )
        }
        filesMatching("test.mixins.json") {
            expand(
                mapOf(
                    "javaVersion" to javaVersion,
                )
            )
        }
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion))
        }
        withSourcesJar()
    }
}
