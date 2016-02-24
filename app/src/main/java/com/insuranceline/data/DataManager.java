package com.insuranceline.data;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.job.fetch.FetchSamplesJob;
import com.insuranceline.data.local.DatabaseHelper;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.remote.ApiService;
import com.insuranceline.data.remote.EdgeApiService;
import com.insuranceline.data.remote.FitBitApiService;
import com.insuranceline.data.remote.model.DashboardModel;
import com.insuranceline.data.remote.responses.ClaimRewardResponse;
import com.insuranceline.data.remote.responses.DailySummaryResponse;
import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.remote.responses.EdgePayResponse;
import com.insuranceline.data.remote.responses.EdgeShoppingCardResponse;
import com.insuranceline.data.remote.responses.EdgeWhoAmIResponse;
import com.insuranceline.data.remote.responses.FitBitTokenResponse;
import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.remote.responses.StepsCountResponse;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.EdgeShoppingCart;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.data.vo.Pay;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.event.FitBitLogoutEvent;
import com.insuranceline.event.GeneralErrorEvent;
import com.insuranceline.event.GoalAchieveEvent;
import com.insuranceline.event.LogOutFromEdgeEvent;
import com.insuranceline.receiver.NotificationHelper;
import com.insuranceline.utils.CampaignAlgorithm;
import com.insuranceline.utils.TimeUtils;
import com.path.android.jobqueue.JobManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import au.com.lumo.ameego.LumoController;
import au.com.lumo.ameego.model.MUser;
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
    private final LumoController mLumoController;
    private final NotificationHelper mNotificationHelper;

    private List<Goal> mCatchedGoals = null;

    @Inject
    public DataManager(ApiService apiService, EdgeApiService edgeApiService,
                       FitBitApiService fitBitApiService, DatabaseHelper databaseHelper,
                       JobManager jobManager, PreferencesHelper preferencesHelper,
                       AppConfig appConfig, EventBus eventBus, LumoController lumoController,
                       NotificationHelper notificationHelper){

        this.mEdgeApiService = edgeApiService;
        this.mFitBitApiService = fitBitApiService;
        this.mDatabaseHelper = databaseHelper;
        this.mJobHelper = jobManager;
        this.mApiService = apiService;
        this.mPreferencesHelper = preferencesHelper;
        this.mAppConfig = appConfig;
        this.mEventBus = eventBus;
        this.mLumoController = lumoController;
        this.mNotificationHelper = notificationHelper;

        createGoalsAsDefaultIfNotCreated();

        createFirstDailySummaryIfNotCreated();

        mCatchedGoals = mDatabaseHelper.fetchAllGoalInAscendingOrder();
    }

    private void createFirstDailySummaryIfNotCreated() {
        if(!mDatabaseHelper.isAnyDailySummaryCreated()){
            mDatabaseHelper.saveDailySummary(DailySummary.createDefaultGoal());
        }
    }

    private void createGoalsAsDefaultIfNotCreated() {
        if(!mDatabaseHelper.isAnyGoalCreated()){
            for (int i = 0; i < GOAL_COUNTS; i++) {
                mDatabaseHelper.saveGoal(
                        Goal.createDefaultGoal(
                                i,mAppConfig.getEndOfCampaign(),
                                AppConfig.INITIALS_TARGET_STEP_COUNT,
                                AppConfig.SKU[i]));
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

    public Observable<EdgeUser> loginEdgeSystem() {
        return getEdgeToken().flatMap(new Func1<EdgeAuthResponse, Observable<EdgeUser>>() {
            @Override
            public Observable<EdgeUser> call(EdgeAuthResponse response) {
                return Observable.zip(
                        mEdgeApiService.whoami(response.getmTokenType() + " " + response.getmAccessToken()),
                        Observable.just(response),
                        new Func2<EdgeWhoAmIResponse, EdgeAuthResponse, EdgeUser>() {
                            @Override
                            public EdgeUser call(EdgeWhoAmIResponse edgeWhoAmIResponse, EdgeAuthResponse edgeAuthResponse) {

                                mPreferencesHelper.saveEdgeSystemToken(edgeAuthResponse.getmAccessToken());
                                EdgeUser edgeUser = new EdgeUser
                                        .Builder(edgeWhoAmIResponse, edgeAuthResponse)
                                        .createMUser()
                                        /*.setDebugEnable(BuildConfig.DEBUG, mPreferencesHelper.isUseFitBitOwner())*/
                                        .build();

                                edgeUser.save();
                                mLumoController.saveUser(edgeUser.getLumoUser());

                                return edgeUser;
                            }
                        });
            }
        });
    }


    public Observable<EdgeAuthResponse> getEdgeToken() {
        return mEdgeApiService.getAuthToken(mPreferencesHelper.getUserLoginEmail(),mPreferencesHelper.getPassword(),"password")
                .doOnNext(new Action1<EdgeAuthResponse>() {
                    @Override
                    public void call(EdgeAuthResponse edgeAuthResponse) {
                        MUser lumoUSer = mLumoController.getUser();
                        mPreferencesHelper.saveEdgeSystemToken(edgeAuthResponse.getmAccessToken());
                        if (lumoUSer != null)
                            lumoUSer.setAccess_token(edgeAuthResponse.getmAccessToken());
                    }
                })
                .doOnError(handleEdgeNetworkError());
    }


    public Observable<EdgePayResponse> claimReward(final String SKU, final String emailAddress){
        return getEdgeToken()
                .flatMap(function(SKU,emailAddress))
                .flatMap(new Func1<EdgeShoppingCardResponse, Observable<EdgePayResponse>>() {
                    @Override
                    public Observable<EdgePayResponse> call(EdgeShoppingCardResponse edgeShoppingCardResponse) {
                        String token = mPreferencesHelper.getEdgeSystemToken();
                        Timber.d("ClaimReward Token: %s", token);

                        if (edgeShoppingCardResponse.success){
                            Pay payment = new Pay.Builder().build();
                            Timber.d("cardDetails.paymentType: %s", payment.cardDetails.paymentType);
                            return mEdgeApiService.pay("Bearer " + token, payment);
                        } else{
                            return Observable.error(new Throwable(edgeShoppingCardResponse.getErrorsAsText()));
                        }
                    }
                });
    }

    /**
     * After getting  AuthToken successfully then Call ClaimReward Api
     * **/
    public Func1<EdgeAuthResponse, Observable<EdgeShoppingCardResponse>> function(final String SKU, final String emailAddress){
        return new Func1<EdgeAuthResponse, Observable<EdgeShoppingCardResponse>>() {
            @Override
            public Observable<EdgeShoppingCardResponse> call(EdgeAuthResponse edgeAuthResponse) {
                EdgeShoppingCart shoppingCart = new EdgeShoppingCart.Builder().build();

                shoppingCart.shoppingCartItems.SKU = SKU;
                shoppingCart.emailAddress = emailAddress;

                Timber.d("BillingAddress     = $id: %s", shoppingCart.billingAddress.$id);

                Timber.d("Contact Number     = Number: %s, dateCreated: %s, lastUpdated: %s"
                        , shoppingCart.contactPhoneNumber.number
                        , shoppingCart.contactPhoneNumber.dateCreated
                        , shoppingCart.contactPhoneNumber.lastUpdated);

                Timber.d("ShoppingCartItemVM = quantity: %s SKU: %s"
                        , shoppingCart.shoppingCartItems.quantity
                        , shoppingCart.shoppingCartItems.SKU);

                Timber.d("Email Address      = %s", shoppingCart.emailAddress);

                Timber.d(" Get AuthToken Token: %s", edgeAuthResponse.getmAccessToken());
                return mEdgeApiService.claimReward("Bearer " + edgeAuthResponse.getmAccessToken(),shoppingCart);
            }
        };
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

    public  Observable<EdgeWhoAmIResponse> edgeSystemTermsAndConditionAccepted(){
        return mEdgeApiService.whoami("Bearer " + mPreferencesHelper.getEdgeSystemToken())
                .flatMap(new Func1<EdgeWhoAmIResponse, Observable<EdgeWhoAmIResponse>>() {
                    @Override
                    public Observable<EdgeWhoAmIResponse> call(EdgeWhoAmIResponse edgeWhoAmIResponse) {
                        edgeWhoAmIResponse.memberRecord.termsAndConditionsAccepted = true; // Yes we accepted
                        String token = "Bearer " + mPreferencesHelper.getEdgeSystemToken();
                        return mEdgeApiService.postWhoAmI(token,edgeWhoAmIResponse);
                    }
                });
    }


    public boolean isFitBitConnected() {
        return mPreferencesHelper.isFitBitConnected();
    }

    public void setFitBitDisconnect() {
        mPreferencesHelper.deleteFitBitToken();
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

    public boolean isAllGoalDone() {
        boolean isAllDone = true;
        for (Goal goal : mCatchedGoals){
            if (goal.getStatus() != Goal.GOAL_STATUS_DONE){
                isAllDone = false;
                break;
            }
        }
        return isAllDone;
    }

    /**
     *  Getting active goal.
     * */
    @Nullable
    public Goal getActiveGoal() {
        return mDatabaseHelper.fetchActiveGoal();
    }

    /**
     *  Getting Last Done goal.
     * */
    public Goal getLastDoneGoal() {
        return mDatabaseHelper.fetchLastGoal();
    }


    /**
     *  Get Goal for for goal fragment.
     *
     *  This function returns the active goal. If there is not active goal it
     *  returns the idle one with lowest id. If all goal done it just return the latest
     *  goal.
     *
     *  When a goal achieved next goal status change to Idle automatically.
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

    /******** DAILY ACTIVITY SUMMARY AND STEPS SUM COUNT **************/

    /**
     * Get Daily Summary Activiy. This function combines two observables
     *
     * concat: emit the emissions from two or more Observables without interleaving them
     *
     * 1- get daily data from db. It always comes first and it serves first
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
                .repeatWhen(repeatWithDelay())
                .doOnNext(ifGoalAchievedFireEvent());
                /*.onErrorResumeNext(Observable.<DashboardModel>empty())*/
    }

    private Func1<? super Observable<? extends Void>, ? extends Observable<?>> repeatWithDelay() {
        return new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return observable.delay(60, TimeUnit.SECONDS);
            }
        };
    }

    /**
     * Observable fetch 2 different data from Db and zip
     * */
    private Observable<DashboardModel> getDashboardFromDb() {
        return Observable.zip(
                getDailySummaryFromDb(),
                getActiveGoalObservable(),/*Observable.just(getActiveGoal()),*/
                new Func2<DailySummary, Goal, DashboardModel>() {
                    @Override
                    public DashboardModel call(DailySummary dailySummary, Goal goal) {
                        Timber.d("Observable<DashboardModel> getDashboardFromDb() Achieved: %s", goal.getAchievedSteps() );
                        DashboardModel dashboardModel = new DashboardModel();
                        dashboardModel.setActiveGoal(goal);
                        dashboardModel.setmDailySummary(dailySummary);
                        return dashboardModel;
                    }
                });
    }

    /** Fetch daily summary data from db */
    public Observable<DailySummary> getDailySummaryFromDb(){
        return mDatabaseHelper.getDailySummaryObservable();
    }

    public Observable<Goal> getActiveGoalObservable(){
        return mDatabaseHelper.fetchActiveGoalAsObservable();
    }

    private Observable<DashboardModel> getDashboardFromApiWithSave() {
        return Observable.zip(
                getDailySummaryFromApiWithSave(),
                getAchievedStepsCountsForActiveGoalFromApiWithSave().debounce(500, TimeUnit.MILLISECONDS), // debounce for update token
                getActiveGoalObservable(),/*Observable.just(getActiveGoal()),*/
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

    private Action1<? super DashboardModel> ifGoalAchievedFireEvent() {
        return new Action1<DashboardModel>() {
            @Override
            public void call(DashboardModel dashboardModel) {
                Goal activeGoal = dashboardModel.getActiveGoal();
                if ((activeGoal.getAchievedSteps() >= activeGoal.getTarget()) && !isCampaignEnd()){
                    mEventBus.post(new GoalAchieveEvent(activeGoal));
                }
            }
        };
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
                .doOnError(handleFitBitNetworkError());
    }


    public Observable<Integer> getAchievedStepsCountsForActiveGoalFromApiWithSave(){
        Timber.d("getAchievedStepsCountsForActiveGoalFromApiWithSave called");
        final Goal activeGoal = getActiveGoal();
        if (activeGoal != null){
            String baseDate = TimeUtils.convertReadableDate(activeGoal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_5);
            return mFitBitApiService.getStepsCountsBetweenDates(baseDate, "today")
                    .map(new Func1<StepsCountResponse, Integer>() {
                        @Override
                        public Integer call(StepsCountResponse stepsCountResponse) {
                            return (stepsCountResponse.getTotalStepsCount() - activeGoal.getStepsBias());
                        }
                    }).doOnNext(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            Timber.d("setAchievedSteps called(): %s", integer);
                            activeGoal.setAchievedSteps(integer);
                            activeGoal.save();
                        }
                    })
                    .doOnError(handleFitBitNetworkError());
        } else {
            return Observable.empty();
        }
    }

    private Action1<Throwable> handleFitBitNetworkError(){
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof HttpException){
                    HttpException httpException = (HttpException)throwable;
                    // if token expires some reason. Users need to be logout.
                    if (httpException.code() == 401){
                        Timber.e("Network Error: 401 !!!");
                        mEventBus.post(new FitBitLogoutEvent("Session expired."));
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

    private Action1<Throwable> handleEdgeNetworkError(){
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof HttpException){
                    HttpException httpException = (HttpException)throwable;
                    // if token expires some reason. Users need to be logout.
                    if (httpException.code() == 400){
                        Timber.e("Network Error: 400 !!!");
                        mEventBus.post(new LogOutFromEdgeEvent("Edge Session expired."));
                    }
                } else if(throwable != null){
                    mEventBus.post(new GeneralErrorEvent(throwable));
                    Timber.e("HandleEdgeNetworkError: %s", throwable.getMessage());
                } else {
                    Timber.e("Unknown Edge NEtwork Error. Throwable is null");
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


    /**
     * Calculate Bias
     *
     * */
    public Observable<DailySummary> calculateDailyBias(){
        return  mFitBitApiService.getDailySummary()
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
                .doOnError(handleFitBitNetworkError());
    }


    /********   Algrithm Calculation   ********/

    public void startNewGoal(long goalId, int bias) {
        Timber.d("Start Goald Id: %s", goalId);
        mCatchedGoals = CampaignAlgorithm.startGoal(goalId,bias, mCatchedGoals,mAppConfig.getEndOfCampaign());
        saveGoals();
        setBoostNotification();
    }

    public int getNextTarget(long newGoalId){
        return CampaignAlgorithm.calculateNextTarget(newGoalId, mCatchedGoals, mAppConfig.getEndOfCampaign());
    }

    public void endGoal(long goalId) {
        Timber.d("End Goald Id: %s", goalId);
        mCatchedGoals = CampaignAlgorithm.endGoal(goalId,mCatchedGoals);
        saveGoals();
    }


    public Goal getGoalById(long goalId) {
        if(mCatchedGoals == null)
            mCatchedGoals = mDatabaseHelper.fetchAllGoalInAscendingOrder();

        for (Goal goal : mCatchedGoals){
            if (goal.getGoalId() == goalId){
                return goal;
            }
        }

        return mCatchedGoals.get(2);
    }

    public void resetGoals(long target){

        int i =0;
        for (Goal goal : mCatchedGoals) {
            goal.reset(mAppConfig.getEndOfCampaign(),target, AppConfig.SKU[i]);
            i++;
        }

        saveGoals();
    }

    private void saveGoals() {
        mDatabaseHelper.saveGoals(mCatchedGoals);
    }

    /**
     * Claim the reward
     * */
    public Observable<ClaimRewardResponse> submitEmailForRewardClaim(String email, String id) {
//        mEdgeApiService.submitEmail(email, id);
        return Observable.just(new ClaimRewardResponse()).debounce(5, TimeUnit.SECONDS);
    }

    public boolean isCampaignEnd() {
        return System.currentTimeMillis() >= mAppConfig.getEndOfCampaign();
    }


    /*** Notification Related functions *****/
    public void setNextReminderNotification() {
        if (!isCampaignEnd())
            mNotificationHelper.setNextReminderNotification();
    }

    public void resetBoostNotification() {
        if (!isCampaignEnd())
            mNotificationHelper.resetBoostNotification();
    }

    public void resetReminderNotification() {
        if (!isCampaignEnd())
            mNotificationHelper.resetReminderNotification();
    }

    public void setBoostNotification() {
        if (!isCampaignEnd())
            mNotificationHelper.setBoostNotification();
    }

/*    // This function is just for test purposes
    public void setUserAsFitBit(boolean isFitBitUser) {
        mPreferencesHelper.setUserAsFitBit(isFitBitUser);
    }*/

    public void saveUserName(String email) {
        mPreferencesHelper.saveUserLoginEmail(email);
    }

    public void savePassword(String password) {
        mPreferencesHelper.savePassword(password);
    }
}
