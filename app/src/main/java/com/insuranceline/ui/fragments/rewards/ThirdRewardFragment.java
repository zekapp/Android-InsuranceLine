package com.insuranceline.ui.fragments.rewards;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.utils.DialogFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class ThirdRewardFragment extends BaseFragment  implements RewardMvpView{
    private static final long RELATED_REWARD_INDEX = 2;

    @Bind(R.id.start_button) Button startButton;
    @Bind(R.id.reward_def) TextView mTextView;

    @Inject RewardPresenter mRewardPresenter;

    private ProgressDialog mProcessDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_third_reward, container, false);
        ButterKnife.bind(this,view);
        mRewardPresenter.attachView(this);
        mRewardPresenter.updateViewAccordingToGoalIndex(RELATED_REWARD_INDEX);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRewardPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Rewards");
    }

    public static ThirdRewardFragment getInstance() {
        return new ThirdRewardFragment();
    }

    /******** MVP Func *******/
    @Override
    public void updateButtonStatus(String title, @DrawableRes int background, boolean isButtonEnabled) {
        startButton.setText(title);
        startButton.setBackgroundResource(background);
        startButton.setEnabled(isButtonEnabled);
    }

    @Override
    public void updateDefinition(@StringRes int defResId) {
        mTextView.setText(defResId);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        mProcessDialog = DialogFactory.createProgressDialog(getActivity(), "Please wait...");
        mProcessDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProcessDialog != null) mProcessDialog.dismiss();
    }

    @OnClick(R.id.start_button)
    public void onStartButton(){
        mRewardPresenter.startGoal(RELATED_REWARD_INDEX);
    }
}
