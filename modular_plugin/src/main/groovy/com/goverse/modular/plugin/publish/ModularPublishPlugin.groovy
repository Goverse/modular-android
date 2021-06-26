package com.goverse.modular.plugin.publish

import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExcludeRule
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.tasks.bundling.Jar
import com.goverse.modular.utils.ModularUtil
import com.android.build.gradle.LibraryExtension

class ModularPublishPlugin implements Plugin<Project> {

    void handleApiPublish(Project project, ApiExt apiExt) {

        ModularUtil.printLog("start handle ApiPublish: " + apiExt);
        assert apiExt != null
        def apiExtRepo = apiExt.repository;
        def apiExtGroupId = apiExt.groupId;
        def apiExtVariant = apiExt.variant;

        if (ModularUtil.isEmpty(apiExtRepo) || ModularUtil.isEmpty(apiExtGroupId) || ModularUtil.isEmpty(apiExtVariant)) {
            ModularUtil.printLog("Please config api publishing configuration ...");
            return
        }
        project.publishing {
            repositories {
                maven {
                    if (ModularUtil.isNetworkUrl(apiExtRepo)) {
                        credentials {
                            username apiExt.username
                            password apiExt.password
                        }
                    }
                    url (apiExtRepo)
                }
            }

            publications {
                LibraryExtension android = project.extensions.getByName("android")

                if (android == null) {
                    ModularUtil.printLog("ApiPublish Can not be used for this type of module !!!");
                    return
                }

                android.libraryVariants.all { variant ->
                    if (isSameVariant(variant.name.capitalize(), apiExtVariant.capitalize())) {

                        project.task("buildApiJar", type: Jar) {
                            archiveBaseName = getArtifactId(project, apiExt.artifactId, null) + "_api"
                            archiveVersion = getApiVersion(apiExt.version)
                            archiveExtension = "jar"
                            from (variant.javaCompileProvider.get().destinationDir.getPath())
                            destinationDirectory = project.file(project.buildDir.path + File.separator + "api")
                            include(apiExt.srcDirs)
                        }

                        Api(MavenPublication) {
                            from project.components.findByName("android${variant.name.capitalize()}")
                            groupId apiExt.groupId
                            artifactId getArtifactId(project, apiExt.artifactId, null)
                            version getApiVersion(apiExt.version)
                            artifact project.tasks.findByName("buildApiJar")
                        }
                    }
                }
            }
        }
    }

    void handleAarPublish(Project project, com.goverse.modular.plugin.publish.AarExt aarExt) {
        ModularUtil.printLog("start handle AarPublish: " + aarExt);
        assert aarExt != null
        def aarExtRepo = aarExt.repository;
        def aarExtGroupId = aarExt.groupId;
        def aarExtVariant = aarExt.variant;

        if (ModularUtil.isEmpty(aarExtRepo) || ModularUtil.isEmpty(aarExtGroupId) || ModularUtil.isEmpty(aarExtVariant)) {
            ModularUtil.printLog("Please config aar publishing configuration ...");
            return
        }
        project.publishing {
            repositories {
                maven {
                    if (ModularUtil.isNetworkUrl(aarExtRepo)) {
                        credentials {
                            username aarExt.username
                            password aarExt.password
                        }
                    }
                    url (aarExtRepo)
                }
            }

            publications {
                LibraryExtension android = project.extensions.getByName("android")
                if (android == null) {
                    ModularUtil.printLog("AarPublish Can not be used for this type of module !!!");
                    return
                }
                android.libraryVariants.all { variant ->
                    if (isSameVariant(variant.name.capitalize(), aarExtVariant)) {
                        Aar(MavenPublication) {
                            groupId aarExt.groupId
                            artifactId getArtifactId(project, aarExt.artifactId, variant)
                            version getAarVersion(aarExt.version, android)
                            artifact(getModuleBuildOutputPath(project, variant))
                            pom.withXml {
                                final dependenciesNode = asNode().appendNode('dependencies')

                                ext.addDependency = { Dependency dep, String scope ->
                                    if (!isDependencyValid(dep)) return
                                    String groupId = dep.group
                                    String artifactId = dep.name
                                    String version = dep.version
                                    final dependencyNode = dependenciesNode.appendNode('dependency')
                                    dependencyNode.appendNode('groupId', groupId)
                                    dependencyNode.appendNode('artifactId', artifactId)
                                    dependencyNode.appendNode('version', version)
                                    dependencyNode.appendNode('scope', scope)
                                    ModularUtil.printLog("dependency add dep: " + dep.toString());
                                    if (!dep.transitive) {
                                        final exclusionNode = dependencyNode.appendNode('exclusions').appendNode('exclusion')
                                        exclusionNode.appendNode('groupId', '*')
                                        exclusionNode.appendNode('artifactId', '*')
                                    } else if (!dep.properties.excludeRules.empty) {
                                        final exclusionNode = dependencyNode.appendNode('exclusions').appendNode('exclusion')
                                        dep.properties.excludeRules.each { ExcludeRule rule ->
                                            exclusionNode.appendNode('groupId', rule.group ?: '*')
                                            exclusionNode.appendNode('artifactId', rule.module ?: '*')
                                        }
                                    }
                                }
                                // List all "compile" dependencies (for old Gradle)
                                project.configurations.compile.getDependencies().each { dep -> addDependency(dep, "compile") }
                                // List all "api" dependencies (for new Gradle) as "compile" dependencies
                                project.configurations.api.getDependencies().each { dep -> addDependency(dep, "compile") }
                                // List all "implementation" dependencies (for new Gradle) as "runtime" dependencies
                                project.configurations.implementation.getDependencies().each { dep -> addDependency(dep, "runtime") }

                            }
                        }
                    }
                }
            }

            project.tasks.all { task ->
                if (task instanceof AbstractPublishToMaven) {
                    Task variantTask = project.tasks.findByName("assemble" + aarExtVariant)
                    if (variantTask != null) {
                        task.dependsOn variantTask
                    }
                }
            }
        }
    }

    def getArtifactId(Project project, String artifactId, def variant) {
        if (!ModularUtil.isEmpty(artifactId)) return artifactId
        if (variant == null) return project.name
        if (!ModularUtil.isEmpty(variant.flavorName)) {
            return "${project.name}-${variant.flavorName}-${variant.buildType.name}"
        } else {
            return "${project.name}-${variant.buildType.name}"
        }
    }

    def getApiVersion(String version) {
        if (ModularUtil.isEmpty(version)) return "1.0.0"
        return version;
    }

    def isDependencyValid(Dependency dep) {
        if (dep.group == null || dep.version == null || dep.name == null || dep.name == "unspecified") {
            return false
        }
        return true
    }

    def getAarVersion(String version, def android) {
        if (!ModularUtil.isEmpty(version)) return version
        if ((android.defaultConfig == null) || Util.isEmpty(android.defaultConfig.versionName)) {
            return "1.0.0"
        }
        return android.defaultConfig.versionName
    }

    def getModuleBuildOutputPath(Project project, LibraryVariant variant) {
        return !ModularUtil.isEmpty(variant.flavorName) ?
                "$project.buildDir\\outputs\\aar\\${project.name}-${variant.flavorName}-${variant.buildType.name}.aar" :
                "$project.buildDir\\outputs\\aar\\${project.name}-${variant.buildType.name}.aar"
    }

    def isSameVariant(String libraryVariant, String variant) {
        variant = ModularUtil.isEmpty(variant) ? "Debug": variant;
        return libraryVariant.equalsIgnoreCase(variant)
    }

    @Override
    void apply(Project project) {

        ModularUtil.printLog("ModularPublishPlugin apply ...");
        def isApplicationProject = ModularUtil.isApplicationProject(project);
        if (isApplicationProject) {
            ModularUtil.printLog("This plugin can not be used fot Application module !!!");
            return
        }
        PublishExt publishExt = project.getExtensions().create("publish", PublishExt);

        project.plugins.apply(MavenPublishPlugin)
        project.afterEvaluate {

            ApiExt apiExt = publishExt.apiExt;
            if (apiExt == null) ModularUtil.printLog("Can not find apiExt configuration in module:" + project.getName());
            else  handleApiPublish(project, apiExt)

            com.goverse.modular.plugin.publish.AarExt aarExt = publishExt.aarExt;
            if (aarExt == null) ModularUtil.printLog("Can not find aarExt configuration in module:" + project.getName());
            else  handleAarPublish(project, aarExt)
        }
    }
}