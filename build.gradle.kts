import org.jetbrains.gradle.ext.*
import org.jetbrains.gradle.ext.Application

plugins {
    id("org.jetbrains.gradle.plugin.idea-ext").version("1.1.7")
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

rootProject.mkdir("test/plugins")

allprojects {
    apply(plugin = "com.github.johnrengelman.shadow")

    group = "pl.north93"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

gradle.projectsEvaluated {
    rootProject.idea {
        project {
            settings {
                ideArtifacts {
                    create("data-source-csv.jar") {
                        archive("data-source-csv.jar") {
                            jarContentFromRuntimeClasspath(":plugins:data-source-csv")
                        }
                    }

                    create("data-source-bigquery.jar") {
                        archive("data-source-bigquery.jar") {
                            jarContentFromRuntimeClasspath(":plugins:data-source-bigquery")
                        }
                    }

                    create("data-source-random.jar") {
                        archive("data-source-random.jar") {
                            jarContentFromRuntimeClasspath(":plugins:data-source-random")
                        }
                    }

                    create("body-factory-inline.jar") {
                        archive("body-factory-inline.jar") {
                            jarContentFromRuntimeClasspath(":plugins:body-factory-inline")
                        }
                    }

                    create("postprocessor-constant.jar") {
                        archive("postprocessor-constant.jar") {
                            jarContentFromRuntimeClasspath(":plugins:postprocessor-constant")
                        }
                    }
                }

                runConfigurations {
                    application("Start") {
                        it.moduleName = moduleName(":application")
                        it.mainClass = "pl.north93.deadsimplerequestsender.Main"
                        it.workingDirectory = "${rootProject.projectDir}/test"
                        it.jvmArgs = "--add-opens=java.base/java.nio=ALL-UNNAMED"
                        it.buildArtifactBeforeRun("data-source-csv.jar")
                        it.buildArtifactBeforeRun("data-source-bigquery.jar")
                        it.buildArtifactBeforeRun("data-source-random.jar")
                        it.buildArtifactBeforeRun("body-factory-inline.jar")
                        it.buildArtifactBeforeRun("postprocessor-constant.jar")
                    }
                }
            }
        }
    }
}


fun RecursiveArtifact.jarContentFromRuntimeClasspath(projectName: String) {
    val project = project(projectName)

    directoryContent("${project.projectDir}/src/main/resources")
    moduleOutput(moduleName(projectName))

    val runtimeClasspath by project.configurations
    jarContentsFromConfiguration(runtimeClasspath)
}

fun RecursiveArtifact.jarContentsFromConfiguration(configuration: Configuration) {
    val resolvedArtifacts = configuration
            .resolvedConfiguration
            .resolvedArtifacts

    resolvedArtifacts
            .filter { it.id.componentIdentifier is ModuleComponentIdentifier }
            .map { it.file }
            .forEach { extractedDirectory(it) }

    resolvedArtifacts
            .map { it.id.componentIdentifier }
            .filterIsInstance<ProjectComponentIdentifier>()
            .forEach {
                moduleOutput(moduleName(it.projectPath))
            }
}

fun RunConfigurationContainer.application(name: String, configuration: (Application) -> Unit) {
    add(Application(name, rootProject).also(configuration))
}

fun JavaRunConfiguration.buildArtifactBeforeRun(artifactName: String) {
    beforeRun.add(BuildArtifact(artifactName).also { it.artifactName = artifactName })
}

fun moduleName(projectPath: String) = rootProject.name + projectPath.replace(':', '.') + ".main"