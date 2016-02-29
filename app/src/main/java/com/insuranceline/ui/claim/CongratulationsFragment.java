package com.insuranceline.ui.claim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zeki on 14/02/2016.
 */
public class CongratulationsFragment extends BaseFragment implements CongratulationMVpView {

    @Inject
    CongratulationPresenter mCongratulationPresenter;

    @Bind(R.id.reward_img)  ImageView mRewardImgeView;
    @Bind(R.id.cong_def)    TextView mCongDefTextView;
    @Bind(R.id.reward_price) TextView mRewardPriceTextView;

    public static Fragment newInstance() {
        CongratulationsFragment frag = new CongratulationsFragment();
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.congratulations_fragment, container, false);
        ButterKnife.bind(this, contentView);
        mCongratulationPresenter.attachView(this);
        mCongratulationPresenter.updateView();
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCongratulationPresenter.detachView();
    }

    @OnClick(R.id.claim_reward)
    public void onClaimReward(){
        this.getFragmentManager().popBackStack();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.container, EmailGetFragment.newInstance(),EmailGetFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }


    /******* MVP Functions ********/
    @Override
    public void updateCongDef(String congDef) {
        mCongDefTextView.setText(congDef);
    }

    @Override
    public void updateRewardPriceDef(String priceDef) {
        mRewardPriceTextView.setText(priceDef);
    }

    @Override
    public void updatePriceImg(int priceImgRes) {
        mRewardImgeView.setImageResource(priceImgRes);
    }
}
