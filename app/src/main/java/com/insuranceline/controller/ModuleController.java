package com.insuranceline.controller;

import android.content.Context;
import android.content.Intent;

import com.insuranceline.di.qualifier.ApplicationContext;
import com.insuranceline.ui.login.LoginActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import au.com.lumo.ameego.MainAppCallback;

/**
 * Created by zeki on 21/02/2016.
 */
@Singleton
public class ModuleController implements MainAppCallback{

    private Context context;

    @Inject
    ModuleController(@ApplicationContext Context context){
        this.context = context;
    }

    /**** MODULE COMMUNICATION INTERFACE *****/
    @Override
    public void directUserToWelcomePage() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
