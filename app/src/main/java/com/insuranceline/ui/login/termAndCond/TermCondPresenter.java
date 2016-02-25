package com.insuranceline.ui.login.termAndCond;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.responses.EdgeWhoAmIResponse;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TermCondPresenter extends BasePresenter<TermCondMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public TermCondPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(TermCondMvpView mvpView) {
        super.attachView(mvpView);
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void accept() {
        getMvpView().showProgress();
        mSubscription = mDataManager.edgeSystemTermsAndConditionAccepted()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<EdgeWhoAmIResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("LoginError: %s", e.getMessage());
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onNext(EdgeWhoAmIResponse edgeWhoAmIResponse) {
                        getMvpView().hideProgress();
                        if (edgeWhoAmIResponse.success){
                            getMvpView().onSuccess();
                        }else {
                            Timber.d("Error: %s", edgeWhoAmIResponse.getErrorsAsText() );
                            getMvpView().error(edgeWhoAmIResponse.getErrorsAsText());
                        }
                    }
                });
    }
}
