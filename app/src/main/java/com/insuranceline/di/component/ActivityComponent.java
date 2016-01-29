package com.insuranceline.di.component;

import com.insuranceline.di.module.ActivityModule;
import com.insuranceline.di.scope.PerActivity;
import com.insuranceline.ui.login.LoginActivity;
import com.insuranceline.ui.main.MainActivity;

import dagger.Component;

/**
 * Created by zeki on 17/01/2016.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);
}
