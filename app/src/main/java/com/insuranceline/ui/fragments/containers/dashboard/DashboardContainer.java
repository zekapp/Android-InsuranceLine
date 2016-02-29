package com.insuranceline.ui.fragments.containers.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;
import com.insuranceline.ui.fragments.dashboard.DashboardEmptyFragment;
import com.insuranceline.ui.fragments.dashboard.DashboardFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 20,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardContainer extends BaseContainerFragment implements DashboardContainerMvpView {
    public static final String TAG = DashboardContainer.class.getSimpleName();
    @Inject
    DashboardContainerPresenter mPresenter;

    private boolean mIsViewInitiated = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreated");
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s onCreateView", TAG);
        msgToActvInterface.changeTabViewIcon(0);
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("%s container on activity created", TAG);
        mPresenter.fetchNextView();
    }

    @Override
    public void initView(boolean isAnyGoalSet, boolean isPermissionDone, boolean isAllGoalDone) {
        Timber.d("%s init view", TAG);

        /*if (mIsViewInitiated && keepInitializedView) return;*/

        if ((isAnyGoalSet && isPermissionDone) || (isAllGoalDone && isPermissionDone)) {
            replaceFragment(DashboardFragment.getInstance(), false);
        }
        else {
            replaceFragment(DashboardEmptyFragment.getInstance(isPermissionDone ?
                    R.string.dashboard_goal_need_be_start_info :
                    R.string.dashboard_permission_need_info), false);
        }


        /*mIsViewInitiated = isPermissionDone && isAnyGoalSet;*/

    }
}