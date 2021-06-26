package com.goverse.modular.plugin.publish

import org.gradle.api.Action

class PublishExt {

    ApiExt apiExt = new ApiExt();
    com.goverse.modular.plugin.publish.AarExt aarExt = new com.goverse.modular.plugin.publish.AarExt();

    void api(Action<ApiExt> action) {
        action.execute(apiExt)
    }

    void aar(Action<com.goverse.modular.plugin.publish.AarExt> action) {
        action.execute(aarExt)
    }

    String toString() {
        return "PublishExt ApiExt: " + apiExt + ",AarExt: " + aarExt
    }
}

class AarExt {
    String variant
    String groupId;
    String artifactId;
    String version;
    String username;
    String password;
    String repository;

    void variant(String variant) {
        this.variant = variant;
    }

    void groupId(String groupId) {
        this.groupId = groupId;
    }

    void artifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    void version(String version) {
        this.version = version;
    }

    void username(String username) {
        this.username = username;
    }

    void password(String password) {
        this.password = password;
    }

    void repository(String repository) {
        this.repository = repository;
    }

    String toString() {
        return "AarExt [" +
                "variant: " + variant + "\n" +
                "groupId:" + groupId + "\n" +
                "artifactId:" + artifactId + "\n" +
                "version:" + version + "\n" +
                "username:" + username + "\n" +
                "password:" + password + "\n" +
                "repository:" + repository + "\n" +
                "]"
    }

}

class ApiExt extends com.goverse.modular.plugin.publish.AarExt {

    String srcDirs;

    void srcDirs(String srcDirs) {
        this.srcDirs = srcDirs;
    }

    String toString() {
        return "ApiExt [" +
                "variant: " + variant + "\n" +
                "groupId:" + groupId + "\n" +
                "artifactId:" + artifactId + "\n" +
                "version:" + version + "\n" +
                "username:" + username + "\n" +
                "password:" + password + "\n" +
                "repository:" + repository + "\n" +
                "srcDirs:" + srcDirs + "\n" +
                "]"
    }
}