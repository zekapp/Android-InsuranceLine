package au.com.lumo.ameego;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.PrefUtils;

/**
 * Created by zeki on 4/08/15.
 */
public class TermAndConditionActivity extends AppCompatActivity {

    private static final String TAG = TermAndConditionActivity.class.getSimpleName();

    private WebView mWebView;
    private ProgressBar mProgress;
    private Toolbar mToolbar;

    private String initilaPageUrl = "https://www.google.com.au/";
    private MUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_cond);
        mWebView = (WebView) findViewById(R.id.webview);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        findViewById(R.id.term_cond_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClick();
            }
        });

        setToolbar();


        mUser = PrefUtils.getUser(this);
        initilaPageUrl = String.format(Constants.Url.TERMS_CONDITION, mUser.getAccess_token(), mUser.getAppId());

//        Log.d(TAG, "T&C url: " + initilaPageUrl);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mProgress.setMax(100);

        updateWebView();

    }

    private void setToolbar() {

        if (mToolbar == null) {
//            Log.d("TEST" ,"Didn't find a toolbar");
            return;
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        mToolbar.setTitle("Terms And Conditions");
//        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    private void updateWebView() {

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                setValue(progress);
                super.onProgressChanged(view, progress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(TermAndConditionActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        mWebView.loadUrl(initilaPageUrl);

        mWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setValue(int progress) {
        this.mProgress.setProgress(progress);
    }

    /*@OnClick(R.id.term_cond_done)*/
    void onDoneClick() {

        PrefUtils.setTermAndCondSeen(this, true);

        Intent i = new Intent(this, DispatchActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
