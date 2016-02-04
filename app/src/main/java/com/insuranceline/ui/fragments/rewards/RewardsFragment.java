package com.insuranceline.ui.fragments.rewards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.utils.BaseFragmentStatePagerAdapter;
import com.insuranceline.utils.ZoomOutPageTransformer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class RewardsFragment extends BaseFragment {

    private static final int REWARD_COUNT = 3;

    @Bind(R.id.rewards_pager) ViewPager mRewardPager;
    @Bind(R.id.dot_holder) ViewGroup mDotContainer;

    private ScreenSlidePagerAdapter mPagerAdapter;
    private View[] mDotItemViews = new View[REWARD_COUNT];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Rewards");
        final View view = inflater.inflate(R.layout.fragment_reward, container, false);
        ButterKnife.bind(this,view);
        setupViewPager();
        updatePhotoDots();
        return view;
    }

    private void setupViewPager() {
        mRewardPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mRewardPager.setAdapter(mPagerAdapter);
        mRewardPager.addOnPageChangeListener(new CustomPageViewChanger());
    }

    private void updatePhotoDots() {
        mDotContainer.removeAllViews();

        for (int itemId = 0; itemId < REWARD_COUNT; itemId++) {
            mDotItemViews[itemId] = makeTabBarItem(itemId,mDotContainer);
            mDotContainer.addView(mDotItemViews[itemId]);
        }

        setDotAsSelected(0);
    }

    private View makeTabBarItem(int itemId, ViewGroup container) {
        View view           = getActivity().getLayoutInflater().inflate(R.layout.dot_layout, container, false);
        ImageView dotView   = (ImageView) view.findViewById(R.id.dot);
        dotView.setImageResource(R.drawable.icon_pag_idle);
        return view;
    }

    private void setDotAsSelected(final int itemId){
        for (int item = 0 ;  item < REWARD_COUNT; item++) {
            View dotItemlayout = mDotItemViews[item];
            ImageView iconView = (ImageView)dotItemlayout.findViewById(R.id.dot);
            int iconId = itemId == item?
                    R.drawable.icon_pag_active:
                    R.drawable.icon_pag_idle;
            iconView.setImageResource(iconId);
        }
    }

    private class CustomPageViewChanger implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDotAsSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class ScreenSlidePagerAdapter extends BaseFragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return FirstRewardFragment.getInstance();
            } else if(position == 1){
                return SecondRewardFragment.getInstance();
            } else{
                return ThirdRewardFragment.getInstance();
            }
        }

        @Override
        public int getCount() {
            return REWARD_COUNT;
        }
    }



}
