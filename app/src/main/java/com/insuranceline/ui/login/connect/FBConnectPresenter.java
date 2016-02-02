package com.insuranceline.ui.login.connect;

import com.insuranceline.data.DataManager;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by zeki on 3/02/2016.
 */
public class FBConnectPresenter extends BasePresenter<FBMvpView>{

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
}
