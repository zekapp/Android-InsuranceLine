package com.insuranceline.ui.claim;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by zeki on 14/02/2016.
 */
public interface CongratulationMVpView extends MvpView{

    void updateCongDef(String congDef);

    void updateRewardPriceDef(String priceDef);

    void updatePriceImg(int priceImgRes);
}
