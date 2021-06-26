package com.goverse.amodule;

import com.goverse.bmodule.api.BModuleManager;

public class AModule {
    private String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public static void main(String[] args) {

        //调用BModule暴露的api
        BModuleManager.getInstance().setBModule("b");
    }
}
