package au.com.lumo.ameego.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.lumo.ameego.DispatchActivity;
import au.com.lumo.ameego.LumoController;
import au.com.lumo.ameego.R;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.utils.Constants;

/**
 * Created by appscoredev2 on 10/07/15.
 */
public class SettingsFragment extends BaseFragment{

    TextView mLogedNameTv;
    TextView mAppVersionTv;
    ImageView mCallSupImg;

    private MUser mUser;
    private TextView mPhoneNumber;
    private TextView mEmailNo;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = LumoController.getInstance().getUser();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogedNameTv     = (TextView) view.findViewById(R.id.logged_in_as);
        mAppVersionTv    = (TextView) view.findViewById(R.id.app_version);
        mCallSupImg      = (ImageView) view.findViewById(R.id.call_support);
        mPhoneNumber     = (TextView) view.findViewById(R.id.phone_number);
        mEmailNo         = (TextView) view.findViewById(R.id.email_no);



        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrivacyClicked();
            }
        });
        view.findViewById(R.id.terms_condition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTermCond();
            }
        });
        view.findViewById(R.id.email_customer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailCliecked();
            }
        });
        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogOut();
            }
        });
        view.findViewById(R.id.call_customer_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallSuppotClicked();
            }
        });

        updateLoggedAs();
        updateAppversion();
        updatePhoneNumber();
        updateEmailAddress();
    }

    private void updateEmailAddress() {
        mEmailNo.setText(Constants.SUPPORT_EMAIL);
    }

    private void updatePhoneNumber() {
        mPhoneNumber.setText(Constants.SUPPORT_PHONE_NUMBER);
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.setting_support);
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }

    private void updateAppversion() {
        try {
            String versionName = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
            mAppVersionTv.setText("v." + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mAppVersionTv.setText("");
        }
    }

    private void updateLoggedAs() {
        mLogedNameTv.setText(mUser.getUsername());
    }

    /*@OnClick(R.id.privacy_policy)*/
    void onPrivacyClicked(){
        openUrl(Constants.Url.PRIVACY_POLICY);
    }
    /*@OnClick(R.id.terms_condition)*/
    void onTermCond(){
        openUrl(String.format(Constants.Url.TERMS_CONDITION, mUser.getAccess_token(), mUser.getAppId()));
    }
    /*@OnClick(R.id.email_customer)*/
    void onEmailCliecked(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { Constants.SUPPORT_EMAIL});
        startActivity(Intent.createChooser(intent, ""));
    }

    /*@OnClick(R.id.logout_button)*/
    void onLogOut(){
//
//        WarningUtilsMD.startProgresslDialog("Logging Out...", getActivity());
        NetworkManager.logout(new GenericCallback<MUser>() {
            @Override
            public void done(MUser user, Exception e) {
//                WarningUtilsMD.stopProgress();
//                if (e == null) {
////                    Log.d(TAG, "Login token: " + user.getAccess_token());
//
//                } else {
////                    Log.d(TAG, "Error: " + e.getMessage());
////                    Toast.makeText(SignInActivity.this, "The user name or password is incorrect.", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
//                }
            }
        });

//        Toast.makeText(getActivity(), "Logout successful", Toast.LENGTH_LONG).show();
        LumoController.getInstance().saveUser(null);
        Intent intent = new Intent(getActivity(), DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    /*@OnClick(R.id.call_customer_support)*/
    void onCallSuppotClicked(){
        String uri = "tel:" + Constants.SUPPORT_PHONE_NUMBER;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openUrl(String url){
        if(url == null || url.isEmpty())
            return;

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

//        Log.d("SettingsFragment" , " url: " + url);
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(url)));
    }


}
