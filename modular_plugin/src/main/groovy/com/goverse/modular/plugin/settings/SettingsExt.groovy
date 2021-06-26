package com.goverse.modular.plugin.settings
import com.goverse.modular.utils.ModularUtil
import org.gradle.api.initialization.Settings

class SettingsExt {

    Settings settings

    SettingsExt(Settings settings) {
        this.settings = settings
    }

    void include(String name, String moduleDir) {
        assert !ModularUtil.isEmpty(name) || !ModularUtil.isEmpty(moduleDir)

        File moduleDirFile = new File(moduleDir);
        if (!moduleDirFile.exists() || !moduleDirFile.canRead()) {
            ModularUtil.printLog("Please check the include moduleDir path: " + moduleDir + " is correctly !!!");
            return
        }
        String module = ":" + name;
        settings.include(module)
        settings.project(module).projectDir = moduleDirFile
        ModularUtil.printLog("settings include external dependency, " + "name: " + name + ", moduleDir: " + moduleDir)
    }

}

