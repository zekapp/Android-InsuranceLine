package com.insuranceline.ui.sample;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TestPresenter extends BasePresenter<TestMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public TestPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(TestMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadSamples() {
        checkViewAttached();
        mSubscription = mDataManager.getSamplesFromDbThenUpdateViaApi(0, 30)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Sample>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the samples.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Sample> samples) {
                        if (samples.isEmpty()) {
                            getMvpView().showSamplesEmpty();
                        } else {
                            getMvpView().showSamples(samples);
                        }
                    }
                });
    }

    public void deleteUser() {
        mDataManager.deleteEdgeUser();
    }

}
