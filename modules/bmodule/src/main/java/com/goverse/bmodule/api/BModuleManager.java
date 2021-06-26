package com.goverse.bmodule.api;

import com.goverse.bmodule.BModule;

public class BModuleManager {

    private BModuleManager() {}

    private static final class Singleton {
        private static BModuleManager mInstance = new BModuleManager();
    }

    public static BModuleManager getInstance() {
        return Singleton.mInstance;
    }

    public void setBModule(String a) {
        BModule bModule = new BModule();
        bModule.setB(a);
    }
}
