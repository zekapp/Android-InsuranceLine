package com.insuranceline.ui.claim;

import android.content.Context;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.responses.ClaimRewardResponse;
import com.insuranceline.data.remote.responses.EdgePayResponse;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.di.qualifier.ActivityContext;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import au.com.lumo.ameego.LumoController;
import au.com.lumo.ameego.model.MUser;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class EmailGetPresenter extends BasePresenter<EmailGetMVPView>{

    private final DataManager mDataManager;
    private Context context;
    private MUser lumoUser;

    @Inject
    public EmailGetPresenter(DataManager dataManager,@ActivityContext Context context) {
        mDataManager = dataManager;
        this.context = context;
        lumoUser = LumoController.getInstance().getUser();
    }

    @Override
    public void attachView(EmailGetMVPView mvpView) {
        super.attachView(mvpView);
        updateView();
    }

    private void updateView() {
        getMvpView().fillEmailAddressField(lumoUser.getEmail());
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void attemptToSubmit(String email) {
        boolean cancel = false;


        if (email.isEmpty()){
            getMvpView().onErrorEmail(context.getString(R.string.error_field_required));
            cancel = true;
        }else if (!email.contains("@")){
            getMvpView().onErrorEmail(context.getString(R.string.error_invalid_email));
            cancel= true;
        }

        if (!cancel){
            submitEmail(email);
        }

    }

    private void submitEmail(String email) {
        Goal activeGoal = mDataManager.getActiveGoal();

        if (activeGoal == null) {
            Timber.e("This active goal is null. It shoudn't have been");
            return;
        }

        getMvpView().showProgress();
        mDataManager.claimReward(activeGoal.getStockItemId(), email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EdgePayResponse>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("OnError: %s", e.getMessage());
                        getMvpView().hideProgress();
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(EdgePayResponse edgePayResponse) {
                        getMvpView().hideProgress();

                        if (edgePayResponse.success){
                            endCurrentGoal();
                            checkNextGoal();
                        } else
                            getMvpView().onError(edgePayResponse.getErrorsAsText());
                    }
                });
    }

    private void endCurrentGoal() {
        Goal activeGoal = mDataManager.getActiveGoal();
        if (activeGoal != null)
            mDataManager.endGoal(activeGoal.getGoalId());
    }

    private void checkNextGoal() {
        Goal relevantGoal = mDataManager.getRelevantGoal();
        if ((relevantGoal.getStatus() == Goal.GOAL_STATUS_IDLE) && !mDataManager.isCampaignEnd())
            getMvpView().onSuccess();
        else{
            getMvpView().allGoalAchieved();
        }
    }


    /*        mDataManager.submitEmailForRewardClaim(email, String.valueOf(activeGoal.getGoalId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ClaimRewardResponse>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                        getMvpView().hideProgress();
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(ClaimRewardResponse claimRewardResponse) {
                        Timber.e("onNext called");
                        getMvpView().hideProgress();
                        if (claimRewardResponse.isSuccess()){
                            endCurrentGoal();
                            checkNextGoal();
                        } else
                            getMvpView().onError(claimRewardResponse.getErrorMessage());
                    }
                });*/
}
