package com.goverse.modular.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.PluginAware
import com.goverse.modular.utils.ModularUtil

class ModularPlugin implements Plugin<PluginAware> {

    @Override
    void apply(PluginAware pluginAware) {

        ModularUtil.printLog("ModularPlugin apply ...");

        ModularUtil.printLog(pluginAware.toString())
        if (pluginAware instanceof Settings) {
            Settings settings = (Settings) pluginAware;
            settings.getPlugins().apply(com.goverse.modular.plugin.settings.SettingsPlugin)
        } else if (pluginAware instanceof Project) {
            Project project = (Project) pluginAware
            project.plugins.apply(com.goverse.modular.plugin.publish.ModularPublishPlugin)
            project.plugins.apply(com.goverse.modular.plugin.dependency.ModularDependencyPlugin)
        }
    }
}