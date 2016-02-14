package com.insuranceline.ui.claim;

import android.app.Activity;
import android.os.Bundle;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class ClaimingRewardActivity extends BaseActivity {
    public static final int CLAIMING_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_claiming_reward);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,
                            CongratulationsFragment.newInstance(),
                            CongratulationsFragment.class.getName()).commit();
        }
    }

    public void finishWithCode() {
        setResult(Activity.RESULT_OK);
        finishActivity(CLAIMING_CODE);
    }
}
