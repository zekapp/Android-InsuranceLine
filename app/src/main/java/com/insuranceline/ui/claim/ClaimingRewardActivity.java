package com.insuranceline.ui.claim;

import android.os.Bundle;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class ClaimingRewardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_claiming_reward);
    }

}
