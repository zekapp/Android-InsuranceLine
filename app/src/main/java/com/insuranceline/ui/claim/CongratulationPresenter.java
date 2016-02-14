package com.insuranceline.ui.claim;

import android.support.annotation.DrawableRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import java.text.DecimalFormat;

import javax.inject.Inject;

/**
 * Created by zeki on 14/02/2016.
 */
public class CongratulationPresenter extends BasePresenter<CongratulationMVpView>{

    private DataManager mDataManager;

    String[] orderString = {"first", "second", "third"};

    @DrawableRes
    int[] rewImgSrc = {
            R.drawable.img_village,
            R.drawable.img_nb,
            R.drawable.img_mag,
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
        Goal activeGoal = mDataManager.getActiveGoal();
        int indx = (int)activeGoal.getGoalId();

        String congDef = String.format("You have completed your %s goal of %s steps",
                orderString[indx],
                formatter.format(activeGoal.getTarget()));

        String priceDef = "You have won two Village Movie Tickets";

        getMvpView().updateCongDef(congDef);
        getMvpView().updateRewardPriceDef(priceDef);
        getMvpView().updatePriceImg(rewImgSrc[indx]);
    }
}
