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

    public static final int GOAL_COUNTS = 3;
    private final EdgeApiService mEdgeApiService;
    private final FitBitApiService mFitBitApiService;
    private final DatabaseHelper mDatabaseHelper;
    private final JobManager mJobHelper;
    private final ApiService mApiService;
    private final PreferencesHelper mPreferencesHelper;
    private final AppConfig mAppConfig;
    private final EventBus mEventBus;

    private List<Goal> mCatchedGoals = null;

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

        createGoalsAsDefaultIfNotCreated();

        createFirstDailySummaryIfNotCreated();
    }

    private void createFirstDailySummaryIfNotCreated() {
        if(!mDatabaseHelper.isAnyDailySummaryCreated()){
            mDatabaseHelper.saveDailySummary(DailySummary.createDefaultGoal());
        }
    }

    private void createGoalsAsDefaultIfNotCreated() {
        if(!mDatabaseHelper.isAnyGoalCreated()){
            for (int i = 0; i < GOAL_COUNTS; i++) {
                mDatabaseHelper.saveGoal(Goal.createDefaultGoal(i));
            }
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

    /**
     *  AUTHENTICATION FUNCTION
     *
     * Before calling this function be sure that token set empty.
     * Otherwise OauthInterceptors will be called and modify your header
     *
     */
    public Observable<String> getTokenWithAuthCode(String code) {
        mPreferencesHelper.deleteFitBitToken();
        return mFitBitApiService
                .accessTokenRequest(
                        mAppConfig.getFitBitClientId(),
                        mAppConfig.getFitBitReDirectUri(),
                        code,
                        "authorization_code")
                .map(new Func1<FitBitTokenResponse, String>() {
                    @Override
                    public String call(FitBitTokenResponse fitBitTokenResponse) {
                        mPreferencesHelper.saveFitBitToken(fitBitTokenResponse);
                        return fitBitTokenResponse.getTokenAsString();
                    }
                })
                .onErrorResumeNext(Observable.<String>empty());
    }

    public Observable<String> getFitBitProfile() {
        return mFitBitApiService.getProfile(mPreferencesHelper.getFitBitUserId());
    }

    public boolean isAnyGoalActive() {
        Goal goal = getActiveGoal();
        if (goal != null) return goal.isActive();
        else              return false;
    }


    /**
     *  Getting active goal.
     * */
    public Goal getActiveGoal() {
        return mDatabaseHelper.fetchActiveGoal();
    }


    /**
     *  Get Goal for for goal fragment.
     *
     *  This function returns the active goal. If there is not active goal it
     *  returns the idle one with lowest id. If all goal done it just return the latest
     *  goal.
     *
     * */
    public Goal getRelevantGoal() {
        if (mCatchedGoals == null)
            mCatchedGoals = mDatabaseHelper.fetchAllGoalInAscendingOrder();

        Goal res = null;

        for (Goal goal : mCatchedGoals) {
            if (goal.getStatus() == Goal.GOAL_STATUS_ACTIVE) {
                return goal;
            } else if (goal.getStatus() == Goal.GOAL_STATUS_IDLE) {
                if (res == null) res = goal;
                else if (goal.getGoalId() < res.getGoalId()) {
                    res = goal;
                }
            }
        }

        if (res == null)
            return mCatchedGoals.get(2); // get latest goal

        return res;
    }

/*    *//**
     *  Getting goal For Goal Section.
     * *//*
    public Observable<Goal> getGoalForGoalFragHost() {

        return getGoalData().map(new Func1<List<Goal>, Goal>() {
            @Override
            public Goal call(List<Goal> goals) {
                Goal res = null;
                for (Goal goal : mCatchedGoals) {
                    if (goal.getStatus() == Goal.GOAL_STATUS_ACTIVE) {
                        res = goal;
                    } else if (goal.getStatus() == Goal.GOAL_STATUS_IDLE) {
                        res = goal;
                        break;
                    }
                }

                if (res == null)
                    res = goals.get(goals.size() - 1);

                return res;
            }
        });
    }

    private Observable<List<Goal>> getGoalData(){
        return Observable.
                concat(getCachedGoalData(), getDiskGoalData())
                .takeFirst(new Func1<List<Goal>, Boolean>() {
                    @Override
                    public Boolean call(List<Goal> goals) {
                        return goals!=null;
                    }
                });
    }

    private Observable<List<Goal>> getDiskGoalData() {
        Timber.d("Catch Goald Data");
        return mDatabaseHelper
                .fetchAllGoalInAscendingOrder()
                .doOnNext(new Action1<List<Goal>>() {
                    @Override
                    public void call(List<Goal> goals) {
                        Timber.d("GoalData Catch Updated");
                        mCatchedGoals = goals;
                    }
                });
    }

    private Observable<List<Goal>> getCachedGoalData() {
        Timber.d("Disk Goal Data");
        return Observable.just(mCatchedGoals);
    }*/


    /******** DAILY ACTIVITY SUMMARY AND STEPS SUM COUNT **************/

    /**
     * Get Daily Summary Activiy. This function combines two observables
     *
     * 1- get daily data from db
     * 2- get daily summary from api and save it to db.
     * this observable always return the getDashboardFromDb resul if success.
     * At the same time it hits the getDashboardFromApiWithSave but it never emits the object from
     * it. getDashboardFromApiWithSave saves the data to Db. Next time you called this model
     * it returns the updated value
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


    /**
     * Observable fetch 2 different data from Db and make
     * */
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
        return Observable.zip(
                getDailySummaryFromApiWithSave(),
                getAchievedStepsCountsForActiveGoalFromApiWithSave(),
                Observable.just(getActiveGoal()),
                new Func3<DailySummary, Integer, Goal, DashboardModel>() {
                    @Override
                    public DashboardModel call(DailySummary dailySummary, Integer achieved, Goal goal) {
                        DashboardModel dashboardModel = new DashboardModel();
                        dashboardModel.setmDailySummary(dailySummary);
                        goal.setAchievedSteps(achieved);
                        dashboardModel.setActiveGoal(goal);
                        return dashboardModel;
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


    public Observable<Integer> getAchievedStepsCountsForActiveGoalFromApiWithSave(){
        final Goal activeGoal = getActiveGoal();
        if (activeGoal != null){
            String baseDate = TimeUtils.convertReadableDate(activeGoal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_5);
            return mFitBitApiService.getStepsCountsBetweenDates(baseDate, "today")
                    .map(new Func1<StepsCountResponse, Integer>() {
                        @Override
                        public Integer call(StepsCountResponse stepsCountResponse) {
                            return stepsCountResponse.getTotalStepsCount();
                        }
                    }).doOnNext(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            activeGoal.setAchievedSteps(integer);
                            activeGoal.save();
                        }
                    })
                    .doOnError(handleNetworkError());
        } else {
            return Observable.empty();
        }
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
                } else if(throwable != null){
                    mEventBus.post(new GeneralErrorEvent(throwable));
                    Timber.e("Error: %s", throwable.getMessage());
                } else {
                    Timber.e("Unknown Network Error. Throwable is null");
                }
            }
        };
    }

    public boolean isFitBitScopePermissionDone() {
        String scopeText = mPreferencesHelper.getPermissionGrantedFitBitScopes().toLowerCase();

        if (scopeText.isEmpty())
            return false;
        else
            return scopeText.contains("activity");
    }

    public boolean isFirstLaunch() {
        boolean isFirtLaunching = mPreferencesHelper.isFirstLaunch();
        mPreferencesHelper.setIsFirstLaunch(false);
        return isFirtLaunching;
    }


    /********   Algrithm Calculation   ********/
    public void startActivity(long goalId) {
        Timber.d("Goald Id: %s", goalId);

        Goal firstGoal   = mCatchedGoals.get(0);
        Goal secondGoal = mCatchedGoals.get(1);
        Goal thirdGoal  = mCatchedGoals.get(2);

        //First Goal
        if(goalId == 0){
            firstGoal.setBaseDate(System.currentTimeMillis());
            firstGoal.setStatus(Goal.GOAL_STATUS_ACTIVE);

            mDatabaseHelper.saveGoal(firstGoal);
        }
        //Second Goal
        else if (goalId == 1){
            secondGoal.setBaseDate(System.currentTimeMillis());
            secondGoal.setStatus(Goal.GOAL_STATUS_ACTIVE);

            // Target Calculation
            int reqDay = getHowManyDaysLeft(firstGoal);
            int newTarget  = firstGoal.getNextTarget(reqDay);
            secondGoal.setTarget(newTarget);

            // Daily Req Steps;
            secondGoal.setRequiredDailySteps(newTarget/ reqDay);

            // Daily Active Minute Calculation
            int reqActiveMin = firstGoal.getNextActiveMinute();
            secondGoal.setRequiredDailyActiveMin(reqActiveMin);

            // Daily Req Calorie.
            int reqActiveCalorie = firstGoal.getNextReqCalorie();
            secondGoal.setRequiredDailyCalorie(reqActiveCalorie);


            // Daily Req Distance.
            int reqDistance = firstGoal.getNextDailyReqSteps();
            secondGoal.setRequiredDailySteps(reqDistance);

            mDatabaseHelper.saveGoal(secondGoal);
        }
        //Third Goal
        else if(goalId == 2){
            thirdGoal.setBaseDate(System.currentTimeMillis());
            thirdGoal.setStatus(Goal.GOAL_STATUS_ACTIVE);

            // Target Calculation
            int reqDay = getHowManyDaysLeft(firstGoal, secondGoal);
            int newTarget  = secondGoal.getNextTarget(reqDay);
            thirdGoal.setTarget(newTarget);

            // Daily Req Steps;
            thirdGoal.setRequiredDailySteps(newTarget/ reqDay);

            // Daily Active Minute Calculation
            int reqActiveMin = secondGoal.getNextActiveMinute();
            thirdGoal.setRequiredDailyActiveMin(reqActiveMin);

            // Daily Req Calorie.
            int reqActiveCalorie = secondGoal.getNextReqCalorie();
            thirdGoal.setRequiredDailyCalorie(reqActiveCalorie);

            // Daily Req Distance.
            int reqDistance = secondGoal.getNextDailyReqSteps();
            thirdGoal.setRequiredDailySteps(reqDistance);

            mDatabaseHelper.saveGoal(thirdGoal);
        }
    }

    private int getHowManyDaysLeft(Goal... goals) {
        int size = goals.length;

        if (size < 2) {
            return (mAppConfig.getAppLife() - goals[0].getAchievedInDays()) / 2;
        } else {
            int sumDay = 0;

            for (Goal goal : goals) {
                sumDay += goal.getAchievedInDays();
            }

            return mAppConfig.getAppLife() - sumDay;
        }
    }
}
