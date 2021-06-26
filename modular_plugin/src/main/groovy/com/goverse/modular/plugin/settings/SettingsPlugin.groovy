package com.goverse.modular.plugin.settings

import com.goverse.modular.utils.ModularUtil
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class SettingsPlugin implements Plugin<Settings> {

    @Override
    void apply(Settings settings) {
        ModularUtil.printLog("SettingsPlugin apply ...")
        SettingsExt settingsExt = settings.extensions.create("ExternalDependency", SettingsExt, settings)
        settings.gradle.settingsEvaluated {
            ModularUtil.printLog("settingsEvaluated")
        }
    }
}
