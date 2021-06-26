package com.goverse.amodule.api;

import com.goverse.amodule.AModule;

public class AModuleManager {

    private AModuleManager() {}

    private static final class Singleton {
        private static AModuleManager mInstance = new AModuleManager();
    }

    public static AModuleManager getInstance() {
        return Singleton.mInstance;
    }

    public void setAModule(String a) {
        AModule aModule = new AModule();
        aModule.setA(a);
    }
}
