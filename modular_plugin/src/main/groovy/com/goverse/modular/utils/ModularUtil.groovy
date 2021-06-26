package com.goverse.modular.utils

import org.gradle.api.Project

class ModularUtil {

    static String TAG = "> Modular Log: ";

    static def printLog(String log) {
        println(TAG + log)
    }

    static def isApplicationProject(Project project) {
        assert project != null;
        return project.plugins.hasPlugin("com.android.application")
    }

    static def isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }

    static def isNetworkUrl(CharSequence url) {
        if (!isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
            return true
        }
        return false
    }
}