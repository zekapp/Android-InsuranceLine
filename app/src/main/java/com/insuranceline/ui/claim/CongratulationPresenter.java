package com.insuranceline.ui.claim;

import android.support.annotation.DrawableRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import java.text.DecimalFormat;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class CongratulationPresenter extends BasePresenter<CongratulationMVpView>{

    private DataManager mDataManager;
/*    */
    String idle_goal1def = "You have completed your first goal of %s steps.";
    String idle_goal2def = "You have completed your second goal of %s steps.";
    String idle_goal3def = "You have completed your third goal of %s steps.";

    String idle_goal1def_sub_def = "Claim your reward of Village Movie Ticket for a great night out!";
    String idle_goal2def_sub_def = "Claim your reward of a $30 New Balance Voucher to spend on the latest gear.";
    String idle_goal3def_sub_def = "Claim your reward of a 3 month subscription to Good Health Magazine and get great health tips.";

    @DrawableRes
    int[] rewImgSrc = {
            R.drawable.img_village,
            R.drawable.img_nb,
            R.drawable.img_mag,
    };

    String[] goalPriceDef = {
            idle_goal1def,
            idle_goal2def,
            idle_goal3def
    };

    String[] goalPriceSubDef = {
            idle_goal1def_sub_def,
            idle_goal2def_sub_def,
            idle_goal3def_sub_def
    };


    DecimalFormat formatter = new DecimalFormat("#,###,###");

    @Inject
    public CongratulationPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(CongratulationMVpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void updateView() {
        Goal activeGoal = mDataManager.getActvGoal();

        if (activeGoal == null) {
            Timber.e("This active goal shoudn't have been null");
            return;
        }

        int indx = (int)activeGoal.getGoalId();

        String congDef = String.format(goalPriceDef[indx], formatter.format(activeGoal.getTarget()));
        String priceDef = goalPriceSubDef[indx];


        getMvpView().updateCongDef(congDef);
        getMvpView().updateRewardPriceDef(priceDef);
        getMvpView().updatePriceImg(rewImgSrc[indx]);
    }
}
