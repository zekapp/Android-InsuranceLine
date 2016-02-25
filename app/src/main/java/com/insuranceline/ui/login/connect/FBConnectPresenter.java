package com.insuranceline.ui.login.connect;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.responses.FitBitTokenResponse;
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

    public void getAuthorizationCodeGrant(String incomingData) {
        String code = extractCode(incomingData,"?code=");
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

    public void getImplicitGrantFlow(String incomingData) {
        FitBitTokenResponse fitBitTokenResponse = new FitBitTokenResponse();
        fitBitTokenResponse.scope         = extractCodeBetween(incomingData, "#scope=", "&user_id=");
        fitBitTokenResponse.user_id       = extractCodeBetween(incomingData, "&user_id=", "&token_type=");
        fitBitTokenResponse.token_type    = extractCodeBetween(incomingData, "&token_type=", "&expires_in");
        fitBitTokenResponse.access_token  = extractCode(incomingData, "&access_token=");
        fitBitTokenResponse.refresh_token = "";


        Timber.d("scope info %s", fitBitTokenResponse.scope);
        Timber.d("user_id info %s", fitBitTokenResponse.user_id);
        Timber.d("access_token info %s", fitBitTokenResponse.access_token);
        Timber.d("token_type info %s", fitBitTokenResponse.token_type);
        Timber.d("refresh_token info %s", fitBitTokenResponse.refresh_token);

        mDataManager.setFitBitAccessToken(fitBitTokenResponse);

        getMvpView().onSuccess();
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

    private String extractCode(String incomingData, String type) {
        return incomingData.substring(incomingData.indexOf(type)+type.length());
    }

    private String extractCodeBetween(String incomingData, String start, String end) {
        return incomingData.substring(incomingData.indexOf(start)+start.length(), incomingData.indexOf(end));
    }


}
