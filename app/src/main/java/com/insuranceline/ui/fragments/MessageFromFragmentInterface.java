package com.insuranceline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.insuranceline.ui.fragments.containers.BaseContainerFragment;

public interface MessageFromFragmentInterface {

    void onFragmentViewed(Fragment currentfragment, BaseContainerFragment containerFragment);

    void onBackPresses(Fragment currentfragment);

    Toolbar getToolBar();

    void showTabHost(boolean show);

    Bundle getShareBundle();

    void changeTabViewIcon(int selectedContainer);
}