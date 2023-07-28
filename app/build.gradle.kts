import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.kotlin.dsl.*

plugins {
	id("java")
	id("jacoco")
	id("org.sonarqube") version "4.0.0.2929"
	id("org.springframework.boot") version "2.7.10-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.spring") version "1.9.0"
	kotlin("plugin.jpa") version "1.9.0"
}

group = "br.com.kumabe"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")	
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")	
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sonarqube {
	properties {
		property("project.settings", "sonar-project.properties")
		property("sonar.exclusions", "**/configs/*,**/converters/*,**/dtos/*,**/enums/*,**/exceptions/*,**/models/*,**/repositories/*,**/utils/*,**/SpringApp.kt")
		property("sonar.coverage.exclusions", "**/configs/*,**/converters/*,**/dtos/*,**/enums/*,**/exceptions/*,**/models/*,**/repositories/*,**/utils/*,**/SpringApp.kt")
	}
}

jacoco {
	toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		csv.required.set(true)
		xml.outputLocation.set(File("$buildDir/reports/jacoco/jacoco.xml"))
	}
}

tasks.test {
	useJUnitPlatform()
	finalizedBy("jacocoTestReport")
}