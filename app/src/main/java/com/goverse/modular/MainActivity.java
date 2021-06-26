package com.goverse.modular;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.goverse.amodule.api.AModuleManager;
import com.goverse.bmodule.api.BModuleManager;
import com.goverse.modular.base.BaseComponent;
import com.goverse.modular.biz.BizComponent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AModuleManager.getInstance().setAModule("a");
        BModuleManager.getInstance().setBModule("b");
        BaseComponent.main();
        BizComponent.main();
    }
}