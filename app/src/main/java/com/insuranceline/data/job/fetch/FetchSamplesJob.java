package com.insuranceline.data.job.fetch;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.job.BaseJob;
import com.insuranceline.di.component.AppComponent;
import com.insuranceline.data.vo.Sample;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

import rx.Observer;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class FetchSamplesJob extends BaseJob{
    private static final String GROUP = "new_samples";

    private final int mPage;
    private final int mPerPage;

    @Inject
    DataManager mDataManager;


    public FetchSamplesJob(int page, int perPage) {
        super(new Params(BACKGROUND).groupBy(GROUP).requireNetwork().persist());
        mPage = page;
        mPerPage = perPage;
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {
        Timber.d("onAdded() -> ");
    }

    @Override
    public void onRun() throws Throwable {
        Timber.d("onRun");

        mDataManager.syncSample(mPage,mPerPage).subscribe(new Observer<Sample>() {
            @Override
            public void onCompleted() {
                Timber.i("onCompleted() -> Synced successfully!");
            }

            @Override
            public void onError(Throwable e) {
                Timber.w(e, "onError() -> Error syncing. TypeOfError:%s, Error: %s ",e.getClass(), e.getMessage());
            }

            @Override
            public void onNext(Sample sample) {
                Timber.d("onNext() -> ");
            }
        });
    }

    @Override
    protected void onCancel() {
        Timber.d("OnCancel() -> ");
    }
}
