package au.com.lumo.ameego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.StringUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by appscoredev2 on 8/07/15.
 */
public class SignInActivity extends AppCompatActivity {

    public static final String  TAG         = SplashActivity.class.getSimpleName();

    private EditText mEmailEdit;
    private EditText mPasswordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        mEmailEdit    = (EditText) findViewById(R.id.enter_email);
        mPasswordEdit = (EditText) findViewById(R.id.enter_password);
        /**
         * Karl added : persist user name when login
         */
        try{
            mEmailEdit.setText(PrefUtils.getUserEmail(getApplicationContext()));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void newHereOnClicked(){
        openUrl(Constants.Url.NEW_HERE_USER_URL);
    }

    public void onForgotPasswordClicked(){
        openUrl(Constants.Url.FORGOT_PASSWORD);
    }


    private void openUrl(String url){
        if(url == null || url.isEmpty()) return;

        if (!StringUtils.isUrlValid(url)) url = "http://" + url;

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void goForReward() {

        View    focusView  = null;
        String  password   = mPasswordEdit.getText().toString();
        String  username   = mEmailEdit.getText().toString();
        boolean cancel     = false;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordEdit.setError(getString(R.string.error_field_required));
            focusView = mPasswordEdit;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEmailEdit.setError(getString(R.string.error_field_required));
            focusView = mEmailEdit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else{
            if(Constants.Config.DEVELOPER_MODE) logIn(Constants.LOGIN_USER_NAME, Constants.LOGIN_PASSWORD);
            else                                logIn(username, password);
        }
    }

    public void onGoToMyRewardClick(View view) {
        goForReward();
    }

    private void logIn(final String email, String password) {
        WarningUtilsMD.startProgresslDialog("Logging In...", this);

//        Log.d(TAG, " username: " + email + " pass: " + password);
        NetworkManager.login( email, password, new GenericCallback<MUser>() {
            @Override
            public void done(MUser user, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
//                    Log.d(TAG, "Login token: " + user.getAccess_token());
                    Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    Toast.makeText(SignInActivity.this, "Welcome ",         Toast.LENGTH_LONG).show();
                    user.setUsername(email);
                    AppController.getInstance().saveUser(user);
                    reDirectToBaseActivity();
                } else {
//                    Log.d(TAG, "Error: " + e.getMessage());
//                    Toast.makeText(SignInActivity.this, "The user name or password is incorrect.", Toast.LENGTH_LONG).show();
                    Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void reDirectToBaseActivity() {
        Intent intent = new Intent();
        intent.setClass(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(Constants.Extra.IS_LAUNCHER, true);
        startActivity(intent);
        finish();
    }
}
