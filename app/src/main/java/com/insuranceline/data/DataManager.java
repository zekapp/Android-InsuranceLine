package com.insuranceline.data;

import com.insuranceline.data.job.fetch.FetchSamplesJob;
import com.insuranceline.data.local.DatabaseHelper;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.remote.ApiService;
import com.insuranceline.data.remote.EdgeApiService;
import com.insuranceline.data.remote.FitBitApiService;
import com.insuranceline.data.remote.responses.EdgeResponse;
import com.insuranceline.data.remote.responses.RefreshTokenResponse;
import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.remote.responses.TermCondResponse;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.data.vo.Sample;
import com.path.android.jobqueue.JobManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import kotlin.jvm.Throws;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Singleton
public class DataManager {

    private final EdgeApiService mEdgeApiService;
    private final FitBitApiService mFitBitApiService;
    private final DatabaseHelper mDatabaseHelper;
    private final JobManager mJobHelper;
    private final ApiService mApiService;
    private final PreferencesHelper mPreferencesHelper;


    @Inject
    public DataManager(ApiService apiService, EdgeApiService edgeApiService, FitBitApiService fitBitApiService, DatabaseHelper databaseHelper, JobManager jobManager, PreferencesHelper preferencesHelper){
        this.mEdgeApiService = edgeApiService;
        this.mFitBitApiService = fitBitApiService;
        this.mDatabaseHelper = databaseHelper;
        this.mJobHelper = jobManager;
        this.mApiService = apiService;
        this.mPreferencesHelper = preferencesHelper;
    }

    /**
     * Fetch eventually data from api and save them to Db.
     * */
    public Observable<Sample> syncSample(int page, int perPage) {
        return mApiService.getSamples(page,perPage)
                .concatMap(new Func1<SampleResponseData, Observable<Sample>>() {
                    @Override
                    public Observable<Sample> call(SampleResponseData data) {
                        Timber.d("Sample -> Observable<Sample>");
                        return mDatabaseHelper.setSamples(data.getSampleResponse().getSampleList());
                    }
                });
    }

    /**
     * Fetch all samples from db
     * */
    public Observable<List<Sample>> getAllSamples() {
        return Observable.create(new Observable.OnSubscribe<List<Sample>>() {
            @Override
            public void call(Subscriber<? super List<Sample>> subscriber) {
                subscriber.onNext(mDatabaseHelper.sampleListQuery());
            }
        });
    }

    /**
     * Fetch data api and save it eventually to Db.
     *
     * It check internet connection and other issue. If error occured related to
     * */
    public void fetchSamplesAsync(int page, int perPage){
        mJobHelper.addJobInBackground(new FetchSamplesJob(page, perPage));
    }


    /**
     * Update UI with old data from db.
     * Then fetch new data from Api and update Db.
     * Then Update UI with fresh data again.
    * */
    public Observable<List<Sample>> getSamplesFromDbThenUpdateViaApi(final int page, final int perPage){
        return Observable.create(new Observable.OnSubscribe<List<Sample>>() {
            @Override
            public void call(final Subscriber<? super List<Sample>> subscriber) {
                subscriber.onNext(mDatabaseHelper.sampleListQuery());

                mApiService.getSamples(page,perPage).subscribe(new Observer<SampleResponseData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("mApiService: %s", e.getMessage());
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(SampleResponseData sampleResponseData) {

                        mDatabaseHelper.setSamples(sampleResponseData.getSampleResponse().getSampleList()).subscribe(new Observer<Sample>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onNext(mDatabaseHelper.sampleListQuery());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e("mDatabaseHelper: %s", e.getMessage());
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(Sample sample) {

                            }
                        });
                    }
                });
            }
        });
    }

    public Observable<EdgeUser> loginEdgeSystem(final String email, String password) {
        return mEdgeApiService.loginToEdgeSystem(email,password,"password")
                .concatMap(new Func1<EdgeResponse, Observable<? extends EdgeUser>>() {
                    @Override
                    public Observable<? extends EdgeUser> call(EdgeResponse edgeResponse) {
                        return mDatabaseHelper.createEdgeUser(email,edgeResponse);
                    }
                });
    }

    public Observable<EdgeUser> getEdgeUser() {
        return Observable.create(new Observable.OnSubscribe<EdgeUser>() {
            @Override
            public void call(Subscriber<? super EdgeUser> subscriber) {
                subscriber.onNext(mDatabaseHelper.getEdgeUser());
            }
        });
    }

    public void deleteEdgeUser() {
        mDatabaseHelper.deleteEdgeUser();
    }

    public Observable<Boolean> fetchTokenAndNotifyTCAccepted() {
        return getEdgeUser().concatMap(new Func1<EdgeUser, Observable<? extends Boolean>>() {
            @Override
            public Observable<? extends Boolean> call(EdgeUser edgeUser) {
                return termsAndConditionAcceptRequest(edgeUser.getmAccessToken());
            }
        });
    }

    public  Observable<Boolean> termsAndConditionAcceptRequest(String edgeToken){
        return mApiService.tcResponse(edgeToken)
                .concatMap(new Func1<TermCondResponse, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(TermCondResponse termCondResponse) {
                        return Observable.just(termCondResponse.isResponse());
                    }
                });
    }
}
