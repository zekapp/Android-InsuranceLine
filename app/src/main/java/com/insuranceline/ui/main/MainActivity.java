package com.insuranceline.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.fragments.MessageFromFragmentInterface;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;
import com.insuranceline.ui.fragments.containers.dashboard.DashboardContainerContainer;
import com.insuranceline.ui.fragments.containers.goals.GoalsContainer;
import com.insuranceline.ui.fragments.containers.MoreContainer;
import com.insuranceline.ui.fragments.containers.RewardsContainer;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MainActivity extends BaseActivity implements MessageFromFragmentInterface{


    private static final String TAB_1_TAG = "tab_1";
    private static final String TAB_2_TAG = "tab_2";
    private static final String TAB_3_TAG = "tab_3";
    private static final String TAB_4_TAG = "tab_4";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tabhost) FragmentTabHost mTabHost;

    int[] tabIcons = new int[]{
            R.drawable.icon_dash_idle,
            R.drawable.icon_goals_idle,
            R.drawable.icon_rewards_idle,
            R.drawable.icon_more_idle
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_fitbit_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);

        // Dashboard Container.
        mTabHost.addTab(
                mTabHost.newTabSpec(TAB_1_TAG).setIndicator(
                        this.getTabIndicator(mTabHost.getContext())),
                DashboardContainerContainer.class,
                null);

        // Goals Container Fragment.
        mTabHost.addTab(
                mTabHost.newTabSpec(TAB_2_TAG).setIndicator(
                        this.getTabIndicator(mTabHost.getContext())),
                GoalsContainer.class,
                null);


        // Reward Container Fragment.
        mTabHost.addTab(
                mTabHost.newTabSpec(TAB_3_TAG).setIndicator(
                        this.getTabIndicator(mTabHost.getContext())),
                RewardsContainer.class,
                null);

        // More Container Fragment.
        mTabHost.addTab(
                mTabHost.newTabSpec(TAB_4_TAG).setIndicator(
                        this.getTabIndicator(mTabHost.getContext())),
                MoreContainer.class,
                null);

        setBackGroundOfTabs();

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                hideKeyboard();
            }
        });
    }

    private void setBackGroundOfTabs() {
        int heightValue = 50; // tabHeight
        float density = this.getResources().getDisplayMetrics().density;
        //loop through the TabWidget's child Views (the tabs) and set their height value.
        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = (int) (heightValue * density);
            View tabView = mTabHost.getTabWidget().getChildAt(i);
            tabView.setBackgroundResource(R.drawable.tab_selector);
            ((ImageView)(tabView.findViewById(R.id.tabImage))).setImageResource(tabIcons[i]);
        }
    }

    private View getTabIndicator(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        return view;
    }

    private void hideKeyboard() {
        Timber.d("hideKeyboard called");

        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = this.getWindow().getDecorView().getRootView();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String currentTabTag = mTabHost.getCurrentTabTag();
        if (currentTabTag.equals(TAB_1_TAG)) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_1_TAG)).popFragment();
        } else if (currentTabTag.equals(TAB_2_TAG)) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_2_TAG)).popFragment();
        } else if (currentTabTag.equals(TAB_3_TAG)) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_3_TAG)).popFragment();
        } else if (currentTabTag.equals(TAB_4_TAG)) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag(TAB_4_TAG)).popFragment();
        }

        if (!isPopFragment) {
            finish();
        }
    }


    /**** Action From Fragments *******/

    @Override
    public void onFragmentViewed(Fragment currentfragment, BaseContainerFragment containerFragment) {

    }

    @Override
    public void onBackPresses(Fragment currentfragment) {
        onBackPressed();
    }

    @Override
    public Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    public void showTabHost(boolean show) {

    }

    @Override
    public Bundle getSharebundle() {
        return null;
    }
}
