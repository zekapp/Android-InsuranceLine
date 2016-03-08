package com.insuranceline.ui.claim;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
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

    @DrawableRes
    int[] rewImgSrc = {
            R.drawable.img_village_,
            R.drawable.img_nb,
            R.drawable.img_mag_,
    };

    @Inject
    public EmailGetPresenter(DataManager dataManager,@ActivityContext Context context) {
        mDataManager = dataManager;
        this.context = context;
        lumoUser = LumoController.getInstance().getUser();

    }

    @Override
    public void attachView(EmailGetMVPView mvpView) {
        super.attachView(mvpView);
        updateEmailAddress();
        updateImages();
    }

    private void updateImages() {
        Goal activeGoal = mDataManager.getActvGoal();

        if (activeGoal == null) {
            Timber.e("This active goal shoudn't have been null");
            return;
        }

        int indx = (int)activeGoal.getGoalId();

        getMvpView().updateImage(rewImgSrc[indx]);
    }

    private void updateEmailAddress() {
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
        getMvpView().showProgress();
        mDataManager.claimReward(email)
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
        mDataManager.rewardClaimedSuccessfullyForActiveGoal();
    }

    private void checkNextGoal() {
        Goal idleGoal = mDataManager.getIdleGoal();
        if (idleGoal != null && !mDataManager.isCampaignEnd()){
            getMvpView().onSuccess();
        }else{
            getMvpView().allGoalAchieved();
        }
    }
}
