package com.insuranceline.data;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.job.fetch.FetchSamplesJob;
import com.insuranceline.data.local.DatabaseHelper;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.remote.ApiService;
import com.insuranceline.data.remote.EdgeApiService;
import com.insuranceline.data.remote.FitBitApiService;
import com.insuranceline.data.remote.model.DashboardModel;
import com.insuranceline.data.remote.responses.DailySummaryResponse;
import com.insuranceline.data.remote.responses.EdgeResponse;
import com.insuranceline.data.remote.responses.FitBitTokenResponse;
import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.remote.responses.StepsCountResponse;
import com.insuranceline.data.remote.responses.TermCondResponse;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.event.GeneralErrorEvent;
import com.insuranceline.event.LogOutEvent;
import com.insuranceline.utils.TimeUtils;
import com.path.android.jobqueue.JobManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import retrofit.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
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
    private final AppConfig mAppConfig;
    private final EventBus mEventBus;

    @Inject
    public DataManager(ApiService apiService, EdgeApiService edgeApiService,
                       FitBitApiService fitBitApiService, DatabaseHelper databaseHelper,
                       JobManager jobManager, PreferencesHelper preferencesHelper,
                       AppConfig appConfig, EventBus eventBus){

        this.mEdgeApiService = edgeApiService;
        this.mFitBitApiService = fitBitApiService;
        this.mDatabaseHelper = databaseHelper;
        this.mJobHelper = jobManager;
        this.mApiService = apiService;
        this.mPreferencesHelper = preferencesHelper;
        this.mAppConfig = appConfig;
        this.mEventBus = eventBus;

        createFistGoalAsDefaultIfNotCreated();
    }

    private void createFistGoalAsDefaultIfNotCreated() {
        if(!mDatabaseHelper.isAnyGoalCreated()){
            mDatabaseHelper.saveGoal(Goal.createDefaultGoal());
        }
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
                        mPreferencesHelper.saveEdgeSystemToken(edgeResponse.getmAccessToken());
                        return mDatabaseHelper.createEdgeUser(email,edgeResponse);
                    }
                });
    }

    public Observable<EdgeUser> getUser() {
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

    public  Observable<Boolean> edgeSystemTermsAndConditionAccepted(){
        return mApiService.tcResponse(mPreferencesHelper.getEdgeSystemToken())
                .concatMap(new Func1<TermCondResponse, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(TermCondResponse termCondResponse) {
                        return mDatabaseHelper.updateEdgeUSer(termCondResponse.isResponse());
                    }
                });
    }

//    public String getFitBitAccessToken(){
//        return mPreferencesHelper.getFitBitAccessToken();
//    }

    public boolean isFitBitConnected() {
        return mPreferencesHelper.isFitBitConnected();
    }

    public Observable<String> getTokenWithAuthCode(String code) {
        return mFitBitApiService.accessTokenRequest(
                mAppConfig.getFitBitClientId(),mAppConfig.getFitBitReDirectUri(),code,"authorization_code")
                .concatMap(new Func1<FitBitTokenResponse, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(FitBitTokenResponse fitBitTokenResponse) {
                        mPreferencesHelper.saveFitBitAccessToken(fitBitTokenResponse.getAccess_token());
                        mPreferencesHelper.saveFitBitRefreshToken(fitBitTokenResponse.getRefresh_token());
                        mPreferencesHelper.saveFitBitUserId(fitBitTokenResponse.getUser_id());
                        mPreferencesHelper.setFitBitConnected(true);
                        String rAnda = fitBitTokenResponse.getAccess_token() + ":" +
                                fitBitTokenResponse.getRefresh_token()       + ":" +
                                fitBitTokenResponse.getUser_id();
                        return Observable.just(rAnda);
                    }
                });
    }

    public Observable<String> getFitBitProfile() {
        return mFitBitApiService.getProfile(mPreferencesHelper.getFitBitUserId());
    }

    public boolean isAnyGoalActive() {
        Goal goal = getActiveGoal();
        return goal != null;
    }


    /**
     *  Getting active goal.
     * */
    public Goal getActiveGoal() {
        return mDatabaseHelper.fetchActiveGoal();
    }


    /****** DAILY ACTIVITY SUMMARY RELATED FUNCTIONS **************/

    /**
     * Get Daily Summary Activiy. This function combines two observables
     *
     * 1- get daily data from db
     * 2- get daily summary from api and save it to db.
     *
     *
     * */
    public Observable<DashboardModel> getDashboardModel(){
        return Observable
                .concat(getDashboardFromDb(), getDashboardFromApiWithSave())
                .onErrorResumeNext(Observable.<DashboardModel>empty());
    }

//    public Observable<DailySummary> getDailySummary(){
//        return Observable
//                .merge(getDailySummaryFromDb(), getDailySummaryFromApiWithSave())
//                .onErrorResumeNext(Observable.<DailySummary>empty());
//    }


    private Observable<DashboardModel> getDashboardFromDb() {
        return Observable.zip(
                getDailySummaryFromDb(),
                Observable.just(getActiveGoal()),
                new Func2<DailySummary, Goal, DashboardModel>() {
                    @Override
                    public DashboardModel call(DailySummary dailySummary, Goal goal) {
                        DashboardModel dashboardModel = new DashboardModel();
                        dashboardModel.setActiveGoal(goal);
                        dashboardModel.setmDailySummary(dailySummary);
                        return dashboardModel;
                    }
                });
    }

    /** Fetch daily summary data from db */
    public Observable<DailySummary> getDailySummaryFromDb(){
        Timber.d("getDailySummaryFromDb");
        return mDatabaseHelper.getDailySummaryObservable();
    }

    private Observable<DashboardModel> getDashboardFromApiWithSave() {
        Observable<DailySummary> dailySumObs    = getDailySummaryFromApiWithSave();
        Observable<Integer> totalStepsCountObsv = getAchievedStepsCountsForActiveGoalFromApiWithSave();

        return Observable.zip(
                dailySumObs,
                totalStepsCountObsv,
                Observable.just(getActiveGoal()),
                new Func3<DailySummary, Integer, Goal, DashboardModel>() {
                    @Override
                    public DashboardModel call(DailySummary dailySummary, Integer achieved, Goal goal) {
                        DashboardModel dashboadModel = new DashboardModel();
                        dashboadModel.setmDailySummary(dailySummary);
                        goal.setAchieved(achieved);
                        dashboadModel.setActiveGoal(goal);
                        return dashboadModel;
                    }
                });
    }



    /** Fetch data and then save to db*/
    public Observable<DailySummary> getDailySummaryFromApiWithSave(){
        return mFitBitApiService.getDailySummary()
                .map(new Func1<DailySummaryResponse, DailySummary>() {
                    @Override
                    public DailySummary call(DailySummaryResponse dailySummaryResponse) {
                        return dailySummaryResponse.getDailySummary();
                    }
                })
                .doOnNext(new Action1<DailySummary>() {
                    @Override
                    public void call(DailySummary dailySummary) {
                        mDatabaseHelper.saveDailySummary(dailySummary);
                    }
                })
                .doOnError(handleNetworkError());

    }


    private Action1<Throwable> handleNetworkError(){
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof HttpException){
                    HttpException httpException = (HttpException)throwable;
                    // if token expires some reason. Users need to be logout.
                    if (httpException.code() == 401){
                        Timber.e("Network Error: 401 !!!");
                        mEventBus.post(new LogOutEvent("Session expired."));
                    }
                } else {
                    mEventBus.post(new GeneralErrorEvent(throwable));
                    Timber.e("Error: %s", throwable.getMessage());
                }
            }
        };
    }

    public Observable<Integer> getAchievedStepsCountsForActiveGoalFromApiWithSave(){
        final Goal activeGoal = getActiveGoal();
        if (activeGoal != null){
            String baseDate = TimeUtils.convertReadableDate(activeGoal.getStartDate(), TimeUtils.DATE_FORMAT_TYPE_5);
            return mFitBitApiService.getStepsCountsBetweenDates(baseDate, "today")
                    .map(new Func1<StepsCountResponse, Integer>() {
                        @Override
                        public Integer call(StepsCountResponse stepsCountResponse) {
                            return stepsCountResponse.getTotlaStepsCount();
                        }
                    }).doOnNext(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            activeGoal.setAchieved(integer);
                            activeGoal.save();
                        }
                    })
                    .doOnError(handleNetworkError());
        } else {
            return Observable.empty();
        }
    }

}










/*
        //Working sample
return Observable
                .concat(getDailySummaryFromDb(), getDailySummaryFromApiWithSave())
                .onErrorResumeNext(getDailySummaryFromDb());*/



//        return getDailySummaryFromApiWithSave()
//                .startWith(getDailySummaryFromDb());


/*                    .takeFirst(new Func1<DailySummary, Boolean>() {
                        @Override
                        public Boolean call(DailySummary dailySummary) {
                            Timber.d("dailySummary null: %s, dailySummary fresh: %s", dailySummary == null, dailySummary != null && dailySummary.isUpToDate() );
                            return dailySummary != null;// && dailySummary.isUpToDate();
                        }
                    });*/

//    public Observable<DailySummary> getDailySummary(){
//            return Observable
//                    .concat(getDailySummaryFromDb(), getDailySummaryFromApiWithSave());
///*                    .takeFirst(new Func1<DailySummary, Boolean>() {
//                        @Override
//                        public Boolean call(DailySummary dailySummary) {
//                            Timber.d("dailySummary null: %s, dailySummary fresh: %s", dailySummary == null, dailySummary != null && dailySummary.isUpToDate() );
//                            return dailySummary != null;// && dailySummary.isUpToDate();
//                        }
//                    });*/
//    }

/*    *//** Fetch data and then save to db*//*
    public Observable<DailySummary> getDailySummaryFromApiWithSave(){
        return mFitBitApiService.getDailySummary()
                .flatMap(new Func1<DailySummaryResponse, Observable<? extends DailySummary>>() {
                    @Override
                    public Observable<? extends DailySummary> call(DailySummaryResponse dailySummaryResponse) {
                        Timber.d("getDailySummaryFromApiWithSave");
                        return mDatabaseHelper.saveDailySummary(dailySummaryResponse);
                    }
                });
    }*/
