package com.insuranceline.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.insuranceline.App;
import com.insuranceline.di.component.ActivityComponent;
import com.insuranceline.di.component.DaggerActivityComponent;
import com.insuranceline.di.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .appComponent(App.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
