package com.goverse.modular.plugin.dependency
import com.goverse.modular.utils.ModularUtil

class DependencyExt {

    String name
    String buildType
    String groupId
    String artifactId
    String version
    boolean sourceCode = false

    DependencyExt(String name) {
        this.name = name
    }

    void sourceCode(boolean sourceCode) {
        this.sourceCode = sourceCode
    }

    void buildType(String buildType) {
        this.buildType = buildType
    }

    void groupId(String groupId) {
        this.groupId = groupId
    }

    void artifactId(String artifactId) {
        this.artifactId = artifactId
    }

    void version(String version) {
        this.version = version
    }

    String toString() {
        return "DependencyExt [" +
                "name: " + name + "\n" +
                "buildType:" + buildType + "\n" +
                "groupId:" + groupId + "\n" +
                "artifactId:" + artifactId + "\n" +
                "version:" + version + "\n" +
                "sourceCode:" + sourceCode + "\n" +
                "]"
    }

}