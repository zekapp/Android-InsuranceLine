package com.insuranceline.ui.claim;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zeki on 15/02/2016.
 */
public class NextGoalFragment extends BaseFragment implements NextGoalMvpView{

    @Bind(R.id.start_goal) Button mStartNextGoalButton;
    @Bind(R.id.next_reward_coupe) ImageView mNextCupImageView;
    @Bind(R.id.next_target) TextView mNextTargetTextView;
    @Inject NextGoalPresenter presenter;

    public static NextGoalFragment newInstance() {
        NextGoalFragment fragment = new NextGoalFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.next_goal_fragment, container, false);
        ButterKnife.bind(this, contentView);
        presenter.attachView(this);
        presenter.updateView();
        return contentView;
    }

    @Override
    public void updateNextGoal(String nextTarget, boolean enableStartButton, @DrawableRes int cupResId) {
        mStartNextGoalButton.setEnabled(enableStartButton);
        mNextCupImageView.setImageResource(cupResId);
        mNextTargetTextView.setText(nextTarget);
    }

    @Override
    public void newActivityStarted() {
        ((ClaimingRewardActivity) getActivity()).finishWithCode();
    }

    @OnClick(R.id.start_goal)
    public void onStarGoalClicked(){
        Toast.makeText(getActivity(), "Start", Toast.LENGTH_LONG).show();
        presenter.startNewGoal();
    }
}
