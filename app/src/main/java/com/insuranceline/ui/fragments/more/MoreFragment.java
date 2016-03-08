package com.insuranceline.ui.fragments.more;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.config.AppConfig;
import com.insuranceline.data.DataManager;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.ui.login.connect.FBConnectActivity;
import com.insuranceline.utils.DialogFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MoreFragment extends BaseFragment {

    @Inject
    DataManager mDataManager;
    @Bind(R.id.version_number)
    TextView mVersionNumber;

    @Inject
    AppConfig mAppConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        updateVersionNumber();
        return view;
    }

    private void updateVersionNumber() {
        try {
            String versionName = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
            mVersionNumber.setText(String.format("v.%s" ,versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mVersionNumber.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("More");
        setHasOptionsMenu(true);
    }

    @OnClick(R.id.more_1)
    public void more1() {
//        Intent intent = new Intent(getActivity(), FBConnectActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        openFitbitPerm();
    }


    @OnClick(R.id.more_2)
    public void more2() {

    }

    @OnClick(R.id.more_3)
    public void more3() {
        startFragment(TermsAndCondition.getInstance());
    }

    @OnClick(R.id.more_4)
    public void more4() {
        startFragment(PrivacyPolicy.getInstance());
    }

    @OnClick(R.id.more_5)
    public void more5() {
//        startFragment(AboutFragment.getInstance());
        proceedOpenUrl();
    }

    @OnClick(R.id.more_6)
    public void more6() {

    }

    @OnClick(R.id.more_7)
    public void more7() {
        logOutWarning();
    }

    private void logOutWarning() {

        String title = "Logout";
        String body = "Do you want to logout?";

        DialogFactory.createGenericDialog(getActivity(), title, body, "OK", "Cancel", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void logout() {
        mDataManager.deleteEdgeUser();
        Intent intent = new Intent(getActivity(), DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            openTestFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTestFragment() {

        startFragment(DebugModeFragment.getInstance());

/*
        final EditText input = new EditText(getActivity());
        input.setHeight(100);
        input.setWidth(140);
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setGravity(Gravity.LEFT);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);


        DialogFactory.createDialogWithEditText
                (getActivity(), input, "Test Alert", "Set test Goal.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String target = input.getText().toString();
                                try{
                                    mDashboardPresenter.resetGoal(Integer.valueOf(target));
                                }catch (Exception e){
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .show();
*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void proceedOpenUrl() {
        Uri uri = Uri.parse("https://www.insuranceline.com.au/");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.au")));
        }
    }

    private void openFitbitPerm() {
        Uri uri = Uri.parse(mAppConfig.getFitBitBrowserUrl());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.au")));
        }
    }
}
