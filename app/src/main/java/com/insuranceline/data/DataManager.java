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
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
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
    private Goal mActiveGoal;

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

        mActiveGoal = getActiveGoalFromCatch();
    }

    @Nullable
    private Goal getActiveGoalFromCatch() {

        Goal resGoal = mActiveGoal = null;
        for (Goal goal : mCatchedGoals){
            int status = goal.getStatus();
            if ((status == Goal.GOAL_STATUS_ACTIVE) || (status == Goal.GOAL_STATUS_CLAIMING)){
                resGoal = goal;
                break;
            }
        }

        return resGoal;
    }

    public List<Goal> getCatchedGoalList(){
        return mCatchedGoals;
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
                                mAppConfig.getStockItemId(i)));
            }
        }
    }

    @Nullable
    public Goal getIdleGoal() {
        Goal res = null;
        for (Goal goal : mCatchedGoals){
            if (goal.getStatus() == Goal.GOAL_STATUS_IDLE ){
                res = goal;
                break;
            }
        }
        Timber.d("getIdleGoal() -> %s",res);
        return res;
    }

    public Goal getLastGoal() {
        return mCatchedGoals.get(2);
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
                        mEdgeApiService.whoami(),
                        Observable.just(response),
                        new Func2<EdgeWhoAmIResponse, EdgeAuthResponse, EdgeUser>() {
                            @Override
                            public EdgeUser call(EdgeWhoAmIResponse edgeWhoAmIResponse, EdgeAuthResponse edgeAuthResponse) {

/*                                if (mPreferencesHelper.isFakeUserAsFitBitUser()){
                                    edgeWhoAmIResponse = modifyIncomingData(edgeWhoAmIResponse);
                                }*/

                                mPreferencesHelper.saveEdgeSystemToken(edgeAuthResponse.getmAccessToken());
                                EdgeUser edgeUser = new EdgeUser
                                        .Builder(edgeWhoAmIResponse, edgeAuthResponse, mAppConfig.isFitBitUser(edgeWhoAmIResponse.memberRecord.appId))
                                        .createMUser()
                                        /*.setDebugEnable(BuildConfig.DEBUG, mPreferencesHelper.isUseFitBitOwner())*/
                                        .build();
                                edgeUser.setTermCondAccepted(mPreferencesHelper.isEdgeTandCAccepted());
                                edgeUser.save();
                                mLumoController.saveUser(edgeUser.getLumoUser());

                                return edgeUser;
                            }
                        });
            }
        });
    }

/*    private EdgeWhoAmIResponse modifyIncomingData(EdgeWhoAmIResponse edgeWhoAmIResponse) {
        edgeWhoAmIResponse.memberRecord.appId = "21beee2a-a162-4f6d-8465-0be5f1b42fb9";
        return edgeWhoAmIResponse;
    }*/


    public Observable<EdgeAuthResponse> getEdgeToken() {
        return mEdgeApiService.getAuthToken( mPreferencesHelper.getUserLoginEmail(),mPreferencesHelper.getPassword(),"password")
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

/*    private String getUserLogin() {
        String username = mPreferencesHelper.getUserLoginEmail();

        mPreferencesHelper.setFakeUserAsFitBitUser(false);

        if (username.equals("dealingapp")) {
            return "dealingapp";
        } else if (username.equals("testuser")) {
            mPreferencesHelper.setFakeUserAsFitBitUser(true);
            return "dealingapp";
        } else {
            return username;
        }

    }*/


    public Observable<EdgePayResponse> claimReward(final String emailAddress){
        return getEdgeToken()
                .flatMap(claimRewardFunction(emailAddress))
                .flatMap(new Func1<EdgeShoppingCardResponse, Observable<EdgePayResponse>>() {
                    @Override
                    public Observable<EdgePayResponse> call(EdgeShoppingCardResponse edgeShoppingCardResponse) {
                        if (edgeShoppingCardResponse.success){
                            Pay payment = new Pay.Builder(emailAddress).build();
                            Timber.d("cardDetails.paymentType: %s", payment.cardDetails.paymentType);
                            return mEdgeApiService.pay(payment);
                        } else{
                            return Observable.error(new Throwable(edgeShoppingCardResponse.getErrorsAsText()));
                        }
                    }
                });
    }

    public Observable<EdgePayResponse> claimReward_(final int stockItemId, String emailAddress){
        return Observable.create(new Observable.OnSubscribe<EdgePayResponse>() {
            @Override
            public void call(Subscriber<? super EdgePayResponse> subscriber) {
                EdgePayResponse payResponse = new EdgePayResponse();
                payResponse.success = true;
                subscriber.onNext(payResponse);
                subscriber.onCompleted();
            }
        });
    }

    /**
     * After getting  AuthToken successfully then Call ClaimReward Api
     * **/
    public Func1<EdgeAuthResponse, Observable<EdgeShoppingCardResponse>> claimRewardFunction(final String emailAddress){
        return new Func1<EdgeAuthResponse, Observable<EdgeShoppingCardResponse>>() {
            @Override
            public Observable<EdgeShoppingCardResponse> call(EdgeAuthResponse edgeAuthResponse) {
                EdgeShoppingCart shoppingCart = new EdgeShoppingCart.Builder().build();

                shoppingCart.shoppingCartItems.$values.get(0).stockItemId = mActiveGoal.getStockItemId();
                shoppingCart.emailAddress = emailAddress;

                Timber.d("Contact Number     = Number: %s, dateCreated: %s, lastUpdated: %s"
                        , shoppingCart.contactPhoneNumber.number
                        , shoppingCart.contactPhoneNumber.dateCreated
                        , shoppingCart.contactPhoneNumber.lastUpdated);

                Timber.d("ShoppingCartItemVM = quantity: %s SKU: %s"
                        , shoppingCart.shoppingCartItems.$values.get(0).quantity
                        , shoppingCart.shoppingCartItems.$values.get(0).stockItemId);

                Timber.d("Email Address      = %s", shoppingCart.emailAddress);

                Timber.d(" Get AuthToken Token: %s", edgeAuthResponse.getmAccessToken());
                return mEdgeApiService.claimReward(shoppingCart);
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
        return mEdgeApiService.whoami()
                .flatMap(new Func1<EdgeWhoAmIResponse, Observable<EdgeWhoAmIResponse>>() {
                    @Override
                    public Observable<EdgeWhoAmIResponse> call(EdgeWhoAmIResponse edgeWhoAmIResponse) {
                        edgeWhoAmIResponse.memberRecord.termsAndConditionsAccepted = true; // Yes we accepted
                        // T&C set accepted in the response. Don't trust the server response.
                        // After server fix this isue remove setEdgeTandCAccepted and isEdgeTandCAccepted functions
                        return mEdgeApiService.putWhoAmI(edgeWhoAmIResponse.memberRecord);
                    }
                }).doOnNext(new Action1<EdgeWhoAmIResponse>() {
                    @Override
                    public void call(EdgeWhoAmIResponse edgeWhoAmIResponse) {
                        if (edgeWhoAmIResponse.success){
                            EdgeUser edgeUser = mDatabaseHelper.getEdgeUser();
                            edgeUser.setTermCondAccepted(true);
                            mPreferencesHelper.setEdgeTandCAccepted(true);
                            edgeUser.save();
                        }
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
     *  Authorization Code Grant flow
     *
     * Before calling this claimRewardFunction be sure that token set empty.
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

    /**
     *  AUTHENTICATION FUNCTION
     *
     *  Authorization Code Grant flow
     *
     */
    public void setFitBitAccessToken(FitBitTokenResponse accessToken) {
        mPreferencesHelper.saveFitBitToken(accessToken);
    }

    public Observable<String> getFitBitProfile() {
        return mFitBitApiService.getProfile(mPreferencesHelper.getFitBitUserId());
    }

    @Nullable
    public Goal getActvGoal(){
        return mActiveGoal;
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
                .retryWhen(retryIfNetworkErrorWithDelay())
                .doOnNext(ifGoalAchievedThenFireEvent());
                /*.onErrorResumeNext(Observable.<DashboardModel>empty())*/
    }

    private Func1<Observable<? extends Throwable>, Observable<?>> retryIfNetworkErrorWithDelay(){
        return new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> observable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {

                        Timber.e("retryIfNetworkErrorWithDelay Error: %s is Http exp: %s",
                                throwable.getMessage(), throwable instanceof HttpException);

                        if (throwable instanceof HttpException)
                            return Observable.just(null).delay(20, TimeUnit.SECONDS);
                        else
                            return Observable.just(throwable).delay(20, TimeUnit.SECONDS);
                    }
                });
            }
        };
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
    public Observable<DashboardModel> getDashboardFromDb() {
        return getDailySummaryFromDb().map(new Func1<DailySummary, DashboardModel>() {
            @Override
            public DashboardModel call(DailySummary dailySummary) {
                DashboardModel dashboardModel = new DashboardModel();

                // if there is no active Goal create a dummy for show purposes.
                if (mActiveGoal != null) {
                    dashboardModel.setActiveGoal(mActiveGoal);
                    Timber.d("Observable<DashboardModel> getDashboardFromDb() Achieved: %s", mActiveGoal.getAchievedSteps() );
                }
                else dashboardModel.setActiveGoal(Goal.createDefaultGoal(Goal.DUMMY_ID,System.currentTimeMillis(),0L,123));

                dashboardModel.setmDailySummary(dailySummary);
                return dashboardModel;
            }
        });
    }

    /** Fetch daily summary data from db */
    private Observable<DailySummary> getDailySummaryFromDb(){
        return mDatabaseHelper.getDailySummaryObservable();
    }

    private Observable<DashboardModel> getDashboardFromApiWithSave() {
        return Observable.zip(
                getDailySummaryFromApiWithSave(),
                getAchievedStepsCountsForActiveGoalFromApiWithSave().debounce(500, TimeUnit.MILLISECONDS), // debounce for update token//*Observable.just(getActiveGoal()),*//*
                new Func2<DailySummary, Integer, DashboardModel>() {
                    @Override
                    public DashboardModel call(DailySummary dailySummary, Integer achieved) {

                        DashboardModel dashboardModel = new DashboardModel();

                        if (mActiveGoal != null) {
                            mActiveGoal.setAchievedSteps(achieved);
                            dashboardModel.setActiveGoal(mActiveGoal);
                        }
                        else {
                            Goal dummy = Goal.createDefaultGoal(Goal.DUMMY_ID,System.currentTimeMillis(),0L,123);
                            dummy.setAchievedSteps(achieved);
                            dashboardModel.setActiveGoal(dummy);
                        }

                        dashboardModel.setmDailySummary(dailySummary);
                        return dashboardModel;
                    }
                });
    }

    private Action1<? super DashboardModel> ifGoalAchievedThenFireEvent() {
        return new Action1<DashboardModel>() {
            @Override
            public void call(DashboardModel dashboardModel) {

                if (mActiveGoal == null || isCampaignEnd()){
                    Timber.d("Activity is %s OR Campaing end %s", mActiveGoal == null, isCampaignEnd() );
                    return;
                }

                Timber.d("Activity %s status is %s ", mActiveGoal.getGoalId(), mActiveGoal.getStatus());

                if (mActiveGoal.getStatus() == Goal.GOAL_STATUS_CLAIMING)
                    mEventBus.post(new GoalAchieveEvent(mActiveGoal));
                else if ((mActiveGoal.getAchievedSteps() >= mActiveGoal.getTarget())){
                    endActiveGoal();
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
                .doOnError(handleFitBitNetworkError("FitBitApi.getDailySummary"));
    }


    public Observable<Integer> getAchievedStepsCountsForActiveGoalFromApiWithSave(){
        Timber.d("getAchievedStepsCountsForActiveGoalFromApiWithSave called");
        if (mActiveGoal != null){
            String baseDate = TimeUtils.convertReadableDate(mActiveGoal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_5);
            return mFitBitApiService.getStepsCountsBetweenDates(baseDate, "today")
                    .map(new Func1<StepsCountResponse, Integer>() {
                        @Override
                        public Integer call(StepsCountResponse stepsCountResponse) {
                            return (stepsCountResponse.getTotalStepsCount() - mActiveGoal.getStepsBias());
                        }
                    }).doOnNext(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            Timber.d("setAchievedSteps called(): %s", integer);
                            mActiveGoal.setAchievedSteps(integer);
                            mActiveGoal.save();
                        }
                    })
                    .doOnError(handleFitBitNetworkError("FitBitApi.getStepsCountsBetweenDates"));
        } else {
            return Observable.empty();
        }
    }

    private Action1<Throwable> handleFitBitNetworkError(final String errType){
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof HttpException){
                    HttpException httpException = (HttpException)throwable;
                    // if token expires some reason. Users need to be logout.
                    if (httpException.code() == 401){
                        Timber.e("Error Type: %s Network Error: 401 !!!", errType);
                        mEventBus.post(new FitBitLogoutEvent("Session expired."));
                    }
                } else if(throwable != null){
                    mEventBus.post(new GeneralErrorEvent(throwable));
                    Timber.e("Error Type: %s Error: %s", errType, throwable.getMessage());
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
                .doOnError(handleFitBitNetworkError("FitBitApiService.getDailySummary"));
    }


    /********   Algrithm Calculation   ********/

    public void startNewGoal(long goalId, int bias) {
        Timber.d("Start Goald Id: %s", goalId);
        printGoalsStatus();
        mCatchedGoals = CampaignAlgorithm.startGoal(goalId,bias, mCatchedGoals);
        mActiveGoal = getActiveGoalFromCatch();
        saveGoals();
        setBoostNotification();
    }

    public int getNextTarget(long newGoalId){
        printGoalsStatus();
        return CampaignAlgorithm.getNextTarget(newGoalId, mCatchedGoals);
    }

    public void endActiveGoal() {
        mCatchedGoals = CampaignAlgorithm.endGoal(mActiveGoal.getGoalId(),mCatchedGoals, mAppConfig.getEndOfCampaign());
        mActiveGoal = getActiveGoalFromCatch();
        saveGoals();
        mEventBus.post(new GoalAchieveEvent(mActiveGoal));
    }
    public void rewardClaimedSuccessfullyForActiveGoal() {
        Timber.d("rewardClaimedSuccessfullyForActiveGoal: %s", mActiveGoal.getGoalId());
        mCatchedGoals = CampaignAlgorithm.rewardClaimed(mActiveGoal.getGoalId(),mCatchedGoals);
        mActiveGoal = getActiveGoalFromCatch();
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
            goal.reset(mAppConfig.getEndOfCampaign(),target, mAppConfig.getStockItemId(i));
            i++;
        }

        saveGoals();
    }

    private void saveGoals() {
        mDatabaseHelper.saveGoals(mCatchedGoals);
    }

    /**
     * Claim the reward`
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

/*    // This claimRewardFunction is just for test purposes
    public void setUserAsFitBit(boolean isFitBitUser) {
        mPreferencesHelper.setUserAsFitBit(isFitBitUser);
    }*/

    public void saveUserName(String email) {
        mPreferencesHelper.saveUserLoginEmail(email);
    }

    public void savePassword(String password) {
        mPreferencesHelper.savePassword(password);
    }


    /**** LOG ******/

    private void printGoalsStatus() {
        for (Goal goal : mCatchedGoals){
            Timber.d("Goald id: %s, getTarget %s, mAchievedSteps: %s, mStatus: %s "
                    ,goal.getGoalId()
                    ,goal.getTarget()
                    ,goal.getAchievedSteps()
                    ,goal.getStatus());
        }
    }

/*    public void setLoginAttemptForFitBitUser(boolean isForFitBit) {
        mPreferencesHelper.setLoginAttemptForFitBitUser(isForFitBit);
    }*/
}
