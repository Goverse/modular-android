package com.goverse.modular.plugin.dependency

import com.goverse.modular.utils.ModularUtil
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

class ModularDependencyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        ModularUtil.printLog("ModularDependencyPlugin apply ...");

        NamedDomainObjectContainer<DependencyExt> dependencyExtContainer = project.container(DependencyExt)
        project.extensions.add("moduleDependencies", dependencyExtContainer)

        project.afterEvaluate {
            dependencyExtContainer.each {
                ModularUtil.printLog(it.toString())
                String moduleName = it.name;
                String buildType = it.buildType;
                if (ModularUtil.isEmpty(moduleName) || ModularUtil.isEmpty(buildType)) {
                    ModularUtil.printLog("buildType can not be null, please check module dependency in module: " + project.name);
                    return
                }

                boolean sourceCode = it.sourceCode;

                String reason
                Dependency dependency
                if (sourceCode) {
                    dependency = project.dependencies.project([path: ":" + moduleName])
                    reason = project.name + " is dependent on " + moduleName + " in sourceCode dependency way."
                } else {
                    String groupId = it.groupId;
                    String artifactId = it.artifactId;
                    String version = it.version;

                    if (ModularUtil.isEmpty(groupId) || ModularUtil.isEmpty(artifactId) || ModularUtil.isEmpty(version)) {
                        ModularUtil.printLog("Please check maven dependency , groupId artifactId version require not null !!!");
                        return
                    }
                    dependency = project.dependencies.create(groupId + ":" + artifactId + ":" + version)
                    reason = project.name + " is dependent on " + moduleName + " in maven dependency way."
                }
                dependency.because(reason)
                project.dependencies.add(buildType, dependency)
                ModularUtil.printLog(dependency.reason);
            }
        }
    }
}