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
import com.insuranceline.ui.fragments.goals.FirstGoalFragment;
import com.insuranceline.ui.fragments.goals.SecondGoalFragment;
import com.insuranceline.ui.fragments.goals.ThirdGoalFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class GoalsContainer extends BaseContainerFragment implements GoalContainerMvpView {

    public static final String TAG = GoalsContainer.class.getSimpleName();

    private boolean mIsViewInitiated;

    @Inject
    GoalContainerPresenter mGoalContainerPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreated");
        getActivityComponent().inject(this);
        mGoalContainerPresenter.attachView(this);
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
            mGoalContainerPresenter.fetchNextView();
        }
    }

    @Override
    public void initView(Goal activeGoal) {
        Timber.d("%s init view", TAG);

        if (activeGoal != null){
            long goalId = activeGoal.getGoalId();

            if (goalId == 1) replaceFragment(FirstGoalFragment.getInstance(), false);
            else if (goalId == 2) replaceFragment(SecondGoalFragment.getInstance(), false);
            else replaceFragment(ThirdGoalFragment.getInstance(), false);
        }else {
            Toast.makeText(getActivity(), "No goal left",Toast.LENGTH_LONG).show();
            replaceFragment(FirstGoalFragment.getInstance(), false);
        }

    }


}
