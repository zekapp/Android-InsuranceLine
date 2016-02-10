package com.insuranceline.ui.fragments.containers.goals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;
import com.insuranceline.ui.fragments.goals.GoalFragment;
import com.insuranceline.ui.fragments.more.MoreFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class GoalsContainer extends BaseContainerFragment {

    public static final String TAG = GoalsContainer.class.getSimpleName();

    private boolean mIsViewInitiated = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s onCreateView", TAG);
        msgToActvInterface.changeTabViewIcon(1);
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("%s container on activity created", TAG);
        if (!mIsViewInitiated) {
            mIsViewInitiated = true;
            initView();
        }
    }

    private void initView() {
        Timber.d("%s init view", TAG);

        replaceFragment(new GoalFragment(), false);

    }

/*    @Override
    public void initView(Goal activeGoal, boolean keepInitializedView) {
        Timber.d("%s init view", TAG);

        if (mIsViewInitiated && keepInitializedView) return;

        if (activeGoal != null){
            long goalId = activeGoal.getGoalId();

            if      (goalId == 1) replaceFragment(GoalFragment.getInstance(), false);
            else if (goalId == 2) replaceFragment(SecondGoalFragment.getInstance(), false);
            else replaceFragment(ThirdGoalFragment.getInstance(), false);
        }else {
            Toast.makeText(getActivity(), "No goal left",Toast.LENGTH_LONG).show();
            replaceFragment(GoalFragment.getInstance(), false);
        }

        mIsViewInitiated = true;

    }*/


}
