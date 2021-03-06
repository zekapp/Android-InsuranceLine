package com.insuranceline.di.component;

import com.insuranceline.di.module.ActivityModule;
import com.insuranceline.di.scope.PerActivity;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.claim.ClaimingRewardActivity;
import com.insuranceline.ui.claim.CongratulationsFragment;
import com.insuranceline.ui.claim.EmailGetFragment;
import com.insuranceline.ui.claim.NextGoalFragment;
import com.insuranceline.ui.fragments.containers.dashboard.DashboardContainer;
import com.insuranceline.ui.fragments.containers.goals.GoalsContainer;
import com.insuranceline.ui.fragments.dashboard.DashboardFragment;
import com.insuranceline.ui.fragments.goals.GoalFragment;
import com.insuranceline.ui.fragments.more.DebugModeFragment;
import com.insuranceline.ui.fragments.more.MoreFragment;
import com.insuranceline.ui.fragments.rewards.FirstRewardFragment;
import com.insuranceline.ui.fragments.rewards.SecondRewardFragment;
import com.insuranceline.ui.fragments.rewards.ThirdRewardFragment;
import com.insuranceline.ui.login.LoginActivity;
import com.insuranceline.ui.login.connect.FBConnectActivity;
import com.insuranceline.ui.login.termAndCond.TermCondActivity;
import com.insuranceline.ui.main.MainActivity;
import com.insuranceline.ui.sample.TestActivity;

import dagger.Component;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(TestActivity testActivity);

    void inject(LoginActivity loginActivity);

    void inject(DispatchActivity dispatchActivity);

    void inject(TermCondActivity termCondActivity);

    void inject(FBConnectActivity FBConnectActivity);

    void inject(MainActivity mainActivity);

    void inject(ClaimingRewardActivity claimingRewardActivity);

    void inject(DashboardFragment dashboardFragment);

    void inject(MoreFragment moreFragment);

    void inject(GoalsContainer goalsContainer);

    void inject(DashboardContainer dashboardContainer);

    void inject(GoalFragment goalFragment);

    void inject(FirstRewardFragment firstRewardFragment);

    void inject(SecondRewardFragment secondRewardFragment);

    void inject(ThirdRewardFragment thirdRewardFragment);

    void inject(CongratulationsFragment congratulationsFragment);

    void inject(NextGoalFragment nextGoalFragment);

    void inject(EmailGetFragment emailGetFragment);

    void inject(DebugModeFragment debugModeFragment);

}
