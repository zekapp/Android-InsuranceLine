package com.insuranceline.data.local;


import android.database.sqlite.SQLiteDatabase;

import com.insuranceline.data.remote.responses.EdgeResponse;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.data.vo.Goal$Table;
import com.insuranceline.data.vo.Sample;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public class DatabaseHelper {

    private SQLiteDatabase mDb;

    @Inject
    public DatabaseHelper(SQLiteDatabase database){
        mDb = database;
    }


    /**
     * Set current values (checks PrimaryKey). If it is not in Db then add new row.
     * */
    public Observable<Sample> setSamples(final List<Sample> samples) {
        Timber.d("Observable<Sample> setSamples(final List<Sample> samples)");
        return Observable.create( new Observable.OnSubscribe<Sample>(){
            @Override
            public void call(final Subscriber<? super Sample> subscriber) {
                TransactionManager.transact(mDb, new Runnable() {
                    @Override
                    public void run() {
                        for (Sample sample: samples){
                            sample.save();

                            subscriber.onNext(sample);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }


    public List<Sample> sampleListQuery(){
        Timber.d("List<Sample> sampleListQuery()");
        return new Select().from(Sample.class).queryList();
    }

    public List<Sample> sampleListPageQuery(int page, int perPage){
        return new Select().from(Sample.class).where().limit(page*perPage).offset(perPage).queryList();
    }

    public Observable<EdgeUser> createEdgeUser(final String email, final EdgeResponse edgeResponse) {
        return Observable.create(new Observable.OnSubscribe<EdgeUser>() {
            @Override
            public void call(final Subscriber<? super EdgeUser> subscriber) {
                TransactionManager.transact(mDb, new Runnable() {
                    @Override
                    public void run() {
                        EdgeUser edgeUser = new EdgeUser();
                        edgeUser.setEmail(email);
                        edgeUser.setmTokenType(edgeResponse.getmTokenType());
                        edgeUser.setmExpireIn(edgeResponse.getmExpireIn());
                        edgeUser.setmAccessToken(edgeResponse.getmAccessToken());
//                        edgeUser.setFitBitUser(true);
                        edgeUser.save();

                        subscriber.onNext(edgeUser);
                    }
                });
            }
        });
    }

    public EdgeUser getEdgeUser() {
        return new Select().from(EdgeUser.class).querySingle();
    }

    public void deleteEdgeUser() {
        Delete.table(EdgeUser.class);
    }

    public Observable<Boolean> updateEdgeUSer(final boolean response) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                EdgeUser edgeUser = getEdgeUser();
                edgeUser.setTermCondAccepted(response);
                edgeUser.save();
                subscriber.onNext(response);
            }
        });
    }

    public DailySummary getDailySummary(){
        return new Select().from(DailySummary.class).where().querySingle();
    }

    public Observable<DailySummary> getDailySummaryObservable(){
        return Observable.create(new Observable.OnSubscribe<DailySummary>() {
            @Override
            public void call(Subscriber<? super DailySummary> subscriber) {
                DailySummary dailySummary = getDailySummary();
                subscriber.onNext(dailySummary);
                subscriber.onCompleted();
            }
        });
    }

//    public Observable<DailySummary> saveDailySummary(final DailySummaryResponse dailySummaryResponse) {
//        return Observable.create( new Observable.OnSubscribe<DailySummary>(){
//            @Override
//            public void call(final Subscriber<? super DailySummary> subscriber) {
//                Timber.d("saveDailySummary");
//                DailySummary summary = new DailySummary();
//                summary.setmSummaryId(1);
//                summary.setDailyActiveMinutes(dailySummaryResponse.summary.lightlyActiveMinutes);
//                summary.setDailyCalories(dailySummaryResponse.summary.caloriesOut);
//                summary.setDailySteps(dailySummaryResponse.summary.steps);
//                summary.setDailyDistance(dailySummaryResponse.summary.getDistance());
//                summary.setmRefreshTime(System.currentTimeMillis());
//                summary.save();
//
//                subscriber.onNext(summary);
//                subscriber.onCompleted();
//                Timber.d("saveDailySummary competed()");
//            }
//        });
//    }

    public Goal fetchActiveGoal() {
        return new Select().from(Goal.class).where(Condition.column(Goal$Table.MSTATUS).eq(Goal.GOAL_STATUS_ACTIVE)).querySingle();
    }

    public void saveDailySummary(DailySummary dailySummary) {
        dailySummary.save();
    }

    public boolean isAnyGoalCreated() {
        long count = new Select().count().from(Goal.class).count();
        Timber.d("Goal Count %s",count);
        return count > 0;
    }

    public void saveGoal(Goal defaultGoal) {
        defaultGoal.save();
    }

    public boolean isAnyDailySummaryCreated() {
        long count = new Select().count().from(DailySummary.class).count();
        Timber.d("Daily Summary Count %s",count);
        return count > 0;
    }

    public Observable<List<Goal>> fetchAllGoalInAscendingOrder() {
        return Observable.create(new Observable.OnSubscribe<List<Goal>>() {
            @Override
            public void call(Subscriber<? super List<Goal>> subscriber) {
                subscriber.onNext(new Select().from(Goal.class).where().orderBy(true,Goal$Table.MGOALID).queryList());
            }
        });

    }

}
