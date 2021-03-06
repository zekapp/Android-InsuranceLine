package com.insuranceline.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.config.AppConfig;
import com.insuranceline.event.FitBitLogoutEvent;
import com.insuranceline.event.GeneralErrorEvent;
import com.insuranceline.event.GoalAchieveEvent;
import com.insuranceline.event.LogOutFromEdgeEvent;
import com.insuranceline.event.SubscriberPriority;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.claim.ClaimingRewardActivity;
import com.insuranceline.ui.fragments.MessageFromFragmentInterface;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;
import com.insuranceline.ui.fragments.containers.MoreContainer;
import com.insuranceline.ui.fragments.containers.RewardsContainer;
import com.insuranceline.ui.fragments.containers.dashboard.DashboardContainer;
import com.insuranceline.ui.fragments.containers.goals.GoalsContainer;
import com.insuranceline.utils.DialogFactory;
import com.insuranceline.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MainActivity extends BaseActivity implements MessageFromFragmentInterface, MainActivityMvpView{


    private static final String TAB_1_TAG = "tab_1";
    private static final String TAB_2_TAG = "tab_2";
    private static final String TAB_3_TAG = "tab_3";
    private static final String TAB_4_TAG = "tab_4";

    public static final int DASHBOARD_CONTAINER_INDEX = 0;
    public static final int GOAL_CONTAINER_INDEX      = 1;
    public static final int REWARD_CONTAINER_INDEX    = 2;
    public static final int MORE_CONTAINER_INDEX      = 3;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tabhost) FragmentTabHost mTabHost;

    @Inject MainActivityPresenter mMainActivityPresenter;
    int[] tabIconsIdle = new int[]{
            R.drawable.icon_dash_idle,
            R.drawable.icon_goals_idle,
            R.drawable.icon_rewards_idle,
            R.drawable.icon_more_idle
    };

    int[] tabIconsActive = new int[]{
            R.drawable.icon_dash_active,
            R.drawable.icon_goals_active,
            R.drawable.icon_rewards_active,
            R.drawable.icon_more_active
    };

    @Inject EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_fitbit_main);
        ButterKnife.bind(this);
        mMainActivityPresenter.attachView(this);
        setSupportActionBar(mToolbar);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);

        // Dashboard Container.
        mTabHost.addTab(
                mTabHost.newTabSpec(TAB_1_TAG).setIndicator(
                        this.getTabIndicator(mTabHost.getContext())),
                DashboardContainer.class,
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

        mMainActivityPresenter.getFirstTabIndex();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this, SubscriberPriority.HIGH);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume()");
        boolean isFitBitAppInstalled = Utils.isPackageInstalled(AppConfig.FITBIT_PACKAGE_NAME, getPackageManager());
        if (!isFitBitAppInstalled){
            dispatchWarningForInstallFitBit();
        }

        mMainActivityPresenter.setNextOpenReminderAlarm();

        mMainActivityPresenter.validateTheEdgeUser();

        mMainActivityPresenter.subscribeFetchingData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainActivityPresenter.stopFetchingData();
    }

    private void dispatchWarningForInstallFitBit() {
        String title = "No FitBit App Found";
        String body = "Please download the FitBit App";

        DialogFactory.createGenericDialog(this, title, body, "OK", "Cancel", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openPlayStore();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void dispatchWarningForConnectFitBit() {
        String title = "Session Expired";
        String body = "Your session expired please login again.";

        DialogFactory.createGenericDialog(this, title, body, "OK", "", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openFBConnect();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void dispatchWarningForEdgeSystemLogout() {
        String title = "Loyalty Session Expired";
        String body = "Your FitBit account expired. Please login again.";

        mMainActivityPresenter.stopFetchingData();
        DialogFactory.createGenericDialog(this, title, body, "Login", "", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openLoginPage();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void openLoginPage() {
        mMainActivityPresenter.deleteEdgeUser();
        gotDispatch();
    }

    private void openFBConnect() {
        mMainActivityPresenter.setFitBitDisconnected();
        gotDispatch();
    }

    private void gotDispatch() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openPlayStore() {
        final String appPackageName = AppConfig.FITBIT_PACKAGE_NAME; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * TabWidget related
     *
     * @link setIconSelected to change tab icon.
     * @link R.drawable.tab_selector to change the background of widget for selector.
     * */
    private void setBackGroundOfTabs() {
        float heightValue = getResources().getDimension(R.dimen.tab_widget_height); // tabHeight
        float density = this.getResources().getDisplayMetrics().density;
        //loop through the TabWidget's child Views (the tabs) and set their height value.
        // remove divider from tabwidget
        mTabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = (int)heightValue;//(int) (heightValue * density);
            View tabView = mTabHost.getTabWidget().getChildAt(i);
            tabView.setBackgroundResource(R.drawable.tab_selector);
            ImageView tabIcon = ((ImageView)(tabView.findViewById(R.id.tabImage)));
            tabIcon.setImageResource(tabIconsIdle[i]);
        }
    }

    /**
     * This fuction called by container via Activity Message interface.
     * */
    private void setIconSelected(int selectedContainer) {
        int size = mTabHost.getTabWidget().getChildCount();
        for (int i = 0; i < size; i++) {
            View tabView = mTabHost.getTabWidget().getChildAt(i);
            ImageView tabIcon = ((ImageView)(tabView.findViewById(R.id.tabImage)));
            tabIcon.setImageResource(i == selectedContainer ? tabIconsActive[i]:tabIconsIdle[i]);
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

    private void dipatchRewardClaimActivity() {
        Intent claimingActivity = new Intent(this, ClaimingRewardActivity.class);
        startActivityForResult(claimingActivity, ClaimingRewardActivity.CLAIMING_CODE);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(GoalAchieveEvent event) {
        Timber.d("Goal Achieved");
//        Toast.makeText(this, "GoalAchieveEvent Achieved",Toast.LENGTH_LONG).show();
        dipatchRewardClaimActivity();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(FitBitLogoutEvent event) {
        Timber.d("LogOutFromFitBitEvent");
//        Toast.makeText(this, "Logout Need",Toast.LENGTH_LONG).show();
        stopFetchingData();
        dispatchWarningForConnectFitBit();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(LogOutFromEdgeEvent event) {
//        Toast.makeText(this, "Logout Need",Toast.LENGTH_LONG).show();
        Timber.d("LogOutFromEdgeEvent");
        dispatchWarningForEdgeSystemLogout();
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(GeneralErrorEvent event) {
        Toast.makeText(this, event.getThrowable().getMessage(),Toast.LENGTH_LONG).show();
    }

    private void stopFetchingData() {
        mMainActivityPresenter.stopFetchingData();
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
    public Bundle getShareBundle() {
        return null;
    }

    @Override
    public void changeTabViewIcon(int selectedContainer) {
        setIconSelected(selectedContainer);
    }

    /******************* MVP Fucntions *********************/
    @Override
    public void changeTab(int tabIndex) {
        mTabHost.setCurrentTab(tabIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("Request Code: %s ResulCode: %s",requestCode, resultCode);
        if (requestCode == ClaimingRewardActivity.CLAIMING_CODE
                && resultCode == RESULT_OK){
            mTabHost.setCurrentTab(1);
        }
    }
}
