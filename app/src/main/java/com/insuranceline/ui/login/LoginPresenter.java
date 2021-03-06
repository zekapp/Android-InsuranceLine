package com.insuranceline.ui.login;

import android.content.Context;

import com.insuranceline.R;
import com.insuranceline.config.AppConfig;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.responses.APIError;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.di.qualifier.ActivityContext;
import com.insuranceline.ui.base.BasePresenter;
import com.insuranceline.utils.ErrorUtils;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private final DataManager mDataManager;
    private final AppConfig mAppConfig;
    private final @ActivityContext  Context mContext;
    private Subscription mSubscription;

    @Inject
    public LoginPresenter(@ActivityContext Context context, DataManager dataManager, AppConfig appConfig){
        mContext     = context;
        mDataManager = dataManager;
        mAppConfig   = appConfig;
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void attemptToLogin(String email, String password) {
        if (validate(email,password)){
            mDataManager.saveUserName(email);
            mDataManager.savePassword(password);
            login();
        }
    }

    private boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty()){
            getMvpView().onErrorEmail(mContext.getString(R.string.error_field_required));
            valid = false;
        } /*else if (!email.contains("@")){
            getMvpView().onErrorEmail(mContext.getString(R.string.error_invalid_email));
            valid = false;
        }*/

        if (password.isEmpty()){
            getMvpView().onErrorPassword(mContext.getString(R.string.error_field_required));
            valid = false;
        } else  if (password.length() < mAppConfig.getPasswordLength()){
            getMvpView().onErrorPassword(mContext.getString(R.string.error_invalid_password));
            valid = false;
        }

        return valid;
    }

    private void login() {
        getMvpView().showProgress();
        /*mDataManager.setLoginAttemptForFitBitUser(true);*/
        mSubscription = mDataManager.loginEdgeSystem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EdgeUser>() {
            @Override
            public void onCompleted() {
                getMvpView().hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e("LoginError: %s", e.getMessage());
                if (e instanceof HttpException) {
                    HttpException response = (HttpException)e;
                    try {
                        String mes = response.response().errorBody().string();
                        APIError error = ErrorUtils.parseError(mes);
                        Timber.d("Code: %s, Mes: %s", response.code(), mes);
                        //try now for edge server
//                        if (response.code() == 400)
//                            retryForDealApp();
//                        else{
                            getMvpView().hideProgress();
                            getMvpView().showLoginError(error.getmErrorDescription());
//                        }
                    } catch (IOException e1) {
                        getMvpView().showLoginError(response.getLocalizedMessage());
                        e1.printStackTrace();
                    }
                }else{
                    getMvpView().hideProgress();
                    getMvpView().showLoginError(e.getMessage());
                }


            }

            @Override
            public void onNext(EdgeUser edgeResponse) {
                getMvpView().hideProgress();
                getMvpView().loginSuccess();
            }
        });
    }

    private void retryForDealApp() {
        /*mDataManager.setLoginAttemptForFitBitUser(false);*/
        mSubscription = mDataManager.loginEdgeSystem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EdgeUser>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("LoginError: %s", e.getMessage());
                        getMvpView().hideProgress();

                        if (e instanceof HttpException) {
                            HttpException response = (HttpException)e;
                            try {
                                String mes = response.response().errorBody().string();
                                APIError error = ErrorUtils.parseError(mes);
                                Timber.d("Code: %s, Mes: %s", response.code(), mes);
                                getMvpView().showLoginError(error.getmErrorDescription());
                            } catch (IOException e1) {
                                getMvpView().showLoginError(response.getLocalizedMessage());
                                e1.printStackTrace();
                            }
                        }else
                            getMvpView().showLoginError(e.getMessage());


                    }

                    @Override
                    public void onNext(EdgeUser edgeResponse) {
                        getMvpView().hideProgress();
                        getMvpView().loginSuccess();
                    }
                });
    }

/*    public void setUserAsFitBitUser(boolean isFitBitUser) {
        mDataManager.setUserAsFitBit(isFitBitUser);
    }*/
}
