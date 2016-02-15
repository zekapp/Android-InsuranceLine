package com.insuranceline.ui.claim;

import android.content.Context;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.responses.ClaimRewardResponse;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.di.qualifier.ActivityContext;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by zeki on 15/02/2016.
 */
public class EmailGetPresenter extends BasePresenter<EmailGetMVPView>{

    private final DataManager mDataManager;
    private Context context;

    @Inject
    public EmailGetPresenter(DataManager dataManager,@ActivityContext Context context) {
        mDataManager = dataManager;
        this.context = context;
    }

    @Override
    public void attachView(EmailGetMVPView mvpView) {
        super.attachView(mvpView);
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

        getMvpView().showProgress();
        mDataManager.submitEmailForRewardClaim(email, String.valueOf(activeGoal.getGoalId()))
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
                });
    }

    private void endCurrentGoal() {
        Goal activeGoal = mDataManager.getActiveGoal();
        mDataManager.endGoal(activeGoal.getGoalId());
    }

    private void checkNextGoal() {
        Goal relevantGoal = mDataManager.getRelevantGoal();
        if (relevantGoal.getStatus() == Goal.GOAL_STATUS_IDLE)
            getMvpView().onSuccess();
        else{
            getMvpView().allGoalAchieved();
        }
    }
}
