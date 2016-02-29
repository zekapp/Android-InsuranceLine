package com.insuranceline.ui.claim;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.utils.DialogFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class EmailGetFragment extends BaseFragment implements EmailGetMVPView {

    @Bind(R.id.submit_email_address) EditText mEmailAddress;

    @Inject EmailGetPresenter presenter;

    private ProgressDialog mProcessDialog;
    boolean isRewardClaimed = false;

    public static Fragment newInstance() {
        EmailGetFragment frag = new EmailGetFragment();
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.emailgetting_fragment, container, false);
        ButterKnife.bind(this, contentView);
        presenter.attachView(this);
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (mProcessDialog != null)
            mProcessDialog.dismiss();
    }

    @OnClick(R.id.submit_email)
    public void onSubmitEmail(){
        resetErrors();
        presenter.attemptToSubmit(mEmailAddress.getText().toString());
    }

    private void resetErrors() {
        mEmailAddress.setError(null);
    }


    @Override
    public void fillEmailAddressField(String emailAddress) {
        mEmailAddress.setText(emailAddress);
    }

    /******* MVP FUNCTIONS ********/

    @Override
    public void onErrorEmail(String error) {
        mEmailAddress.setError(error);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess() {
//        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        this.getFragmentManager().popBackStack();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.container, NextGoalFragment.newInstance(),NextGoalFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void hideProgress() {
        Timber.d("hideProgress() called");
        if (mProcessDialog != null){
            mProcessDialog.hide();
            mProcessDialog.dismiss();
        }
        else Timber.e("mProcessDialog is null");
    }

    @Override
    public void showProgress() {
        mProcessDialog = DialogFactory.createProgressDialog(getActivity(), "Please wait...");
        mProcessDialog.show();
    }

    @Override
    public void allGoalAchieved() {
        ((ClaimingRewardActivity)getActivity()).finishWithCode();
    }

}
