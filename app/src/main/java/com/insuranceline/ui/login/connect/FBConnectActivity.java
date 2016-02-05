package com.insuranceline.ui.login.connect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.config.AppConfig;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.login.LoginActivity;
import com.insuranceline.utils.DialogFactory;

import org.chromium.customtabsclient.shared.CustomTabsHelper;
import org.chromium.customtabsclient.shared.ServiceConnection;
import org.chromium.customtabsclient.shared.ServiceConnectionCallback;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class FBConnectActivity extends BaseActivity implements FBMvpView, ServiceConnectionCallback {

    @Inject FBConnectPresenter mPresenter;
    @Inject AppConfig mAppConfig;

    private CustomTabsIntent customTabsIntent;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private String mPackageNameToBind;
    private CustomTabsServiceConnection mConnection;

    @Bind(R.id.connect_fit_bit) Button mConnectFitBitButton;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private ProgressDialog mProcessDialog;
    private boolean isMenuVisible = false;


    private static class NavigationCallback extends CustomTabsCallback {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            Timber.w("onNavigationEvent: Code = %s", navigationEvent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getData() != null){
            String incomingData = intent.getDataString();
            Timber.d(incomingData);
            mPresenter.getAccessToken(incomingData);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_fit_bit_connect);
        ButterKnife.bind(this);
        onNewIntent(getIntent());
        Timber.d("onCreate");
        setHomeAsUpEnabled();
    }

    public void setHomeAsUpEnabled(){
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProcessDialog != null)
            mProcessDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isMenuVisible)
            getMenuInflater().inflate(R.menu.menu_connect_fit_bit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        item.setActionView(R.layout.menu_text_item);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_finish) {
            dispatchActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.connect_fit_bit)
    @SuppressWarnings("unused")
    public void onFitBitConnect(){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
        customTabsIntent.launchUrl(this, Uri.parse(mAppConfig.getFitBitBrowserUrl()));

    }

    private void dispatchActivity() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /************  MVP Calbacks ***********/

    @Override
    public void showProgress() {
        mProcessDialog = DialogFactory.createProgressDialog(this, "Please wait...");
        mProcessDialog.show();
    }

    @Override
    public void hideProgress() {
        mProcessDialog.dismiss();
    }

    @Override
    public void onSuccess() {
        isMenuVisible = true;
        invalidateOptionsMenu();
        mConnectFitBitButton.setBackgroundResource(R.drawable.btn_connected);
        mConnectFitBitButton.setEnabled(false);
    }

    @Override
    public void error(String localizedMessage) {
        Toast.makeText(this,"Error: " + localizedMessage,Toast.LENGTH_LONG).show();
    }


    private CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new NavigationCallback());
        }
        return mCustomTabsSession;
    }

    public void startService() {
        if (mClient != null) return;
        if (TextUtils.isEmpty(mPackageNameToBind)) {
            mPackageNameToBind = CustomTabsHelper.getPackageNameToUse(this);
            if (mPackageNameToBind == null) return;
        }
        mConnection = new ServiceConnection(this);
        boolean ok = CustomTabsClient.bindCustomTabsService(this, mPackageNameToBind, mConnection);
        if (ok) {
            mConnectFitBitButton.setEnabled(false);
        } else {
            mConnection = null;
        }
    }


    /******** ServiceConnectionCallback *******/


    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mClient = client;
        mConnectFitBitButton.setEnabled(true);
    }

    @Override
    public void onServiceDisconnected() {
        mConnectFitBitButton.setEnabled(false);
        mClient = null;
    }

}
