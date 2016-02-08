package com.insuranceline.ui.login.connect;

import com.insuranceline.data.DataManager;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class FBConnectPresenter extends BasePresenter<FBMvpView>  {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public FBConnectPresenter(DataManager mDataManager){
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(FBMvpView mvpView) {
        super.attachView(mvpView);
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void getAccessToken(String incomingData) {
        String code = extractCode(incomingData);
        Timber.d("Code: %s",code);

        getMvpView().showProgress();
        mSubscription = mDataManager.getTokenWithAuthCode(code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        Timber.e("getTokenWithAuthCode Error: %s", e.getMessage() );
                        getMvpView().hideProgress();
                        getMvpView().error(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        String[] strings = s.split(":");

                        for (String sub : strings){
                            Timber.d("tokens: %s", sub);
                        }

                        getMvpView().hideProgress();
                        getMvpView().onSuccess();
                    }
                });
    }

    private String extractCode(String incomingData) {
        return incomingData.substring(incomingData.indexOf("?code=")+6);
    }

    public void getFitBitProfile() {
        getMvpView().showProgress();
        mSubscription = mDataManager.getFitBitProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d(s);
                    }
                });
    }

    public void unSubscribe() {
        mSubscription.unsubscribe();
    }
}
