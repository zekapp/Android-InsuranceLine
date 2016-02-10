package com.insuranceline.ui.fragments.goals;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.fragments.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class GoalFragment extends BaseFragment implements GoalFragmentMvpView {

    @Inject GoalFragmentPresenter mGoalFragmentPresenter;

    @Bind(R.id.goal_cup) ImageView  cupImageView;
    @Bind(R.id.goal_def) TextView   mDefTextView;
    @Bind(R.id.goal_title) TextView mTitleTextView;
    @Bind(R.id.start_button) Button mStartButton;

    public static GoalFragment getInstance(Goal goal){
        return new GoalFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        mGoalFragmentPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Goals");
        final View view = inflater.inflate(R.layout.fragment_goal, container, false);
        ButterKnife.bind(this, view);
        mGoalFragmentPresenter.updateView();
        return view;
    }


    /******* MVP Related Function ********/
    @Override
    public void updateCupImg(@DrawableRes int cupRes) {
        cupImageView.setImageResource(cupRes);
    }

    @Override
    public void updateGoalDef(@StringRes int defRes) {
        mDefTextView.setText(defRes);
    }

    @Override
    public void updateGoalTitle(@StringRes  int titleRes) {
        mTitleTextView.setText(titleRes);
    }

    @Override
    public void updateButtonTitle(@StringRes int buttonStatus, boolean enabled) {
        mStartButton.setText(buttonStatus);
        mStartButton.setEnabled(enabled);
    }

    @OnClick(R.id.start_button)
    public void onStartbuttonClicked(){
        mGoalFragmentPresenter.activityStarted();
    }

}
