package au.com.lumo.ameego.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by zeki on 26/07/15.
 */
public class WebViewFragment extends BaseFragment{

    private static String CUSTOM_WEB_VIEW_INITIAL_PAGE_KEY = "CUSTOM_WEB_VIEW_INITIAL_PAGE_KEY";
    private static String CUSTOM_WEB_VIEW_PAGE_TITLE_KEY   = "CUSTOM_WEB_VIEW_PAGE_TITLE_KEY";

    /*@Bind(R.id.webview)     */WebView mWebView;
    /*@Bind(R.id.progressBar) */ProgressBar mProgress;
    /*@Bind(R.id.empty_state) */TextView mEmptyState;

    private String initilaPageUrl ;
    private String mTitle = "Custome Link";

    private ConnectivityManager mConManager;

    public static WebViewFragment newInstance(String webUrl, String titleName) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle                bundle   = new Bundle();

        bundle.putString(CUSTOM_WEB_VIEW_INITIAL_PAGE_KEY, webUrl);
        bundle.putString(CUSTOM_WEB_VIEW_PAGE_TITLE_KEY,   titleName);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            initilaPageUrl = StringUtils.getValidatedUrl(getArguments().getString(CUSTOM_WEB_VIEW_INITIAL_PAGE_KEY));
            mTitle         = getArguments().getString(CUSTOM_WEB_VIEW_PAGE_TITLE_KEY);
//            Log.d("WebViewFragment" , "initilaPageUrl: " + initilaPageUrl);
        }
    }


    @Override
    protected String getTitle() {
        return mTitle;
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
    protected int getLayout() {return R.layout.fragment_web_view;}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mConManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mProgress.setMax(100);
        updateWebView();
    }

    private void updateWebView() {

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                WebViewFragment.this.setValue(progress);
                super.onProgressChanged(view, progress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                checkForEmtyState();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                checkForEmtyState();
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

        checkForEmtyState();

    }

    private void checkForEmtyState() {
        if(checkInternetConnection()){
            mWebView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(View.GONE);
        }else{
            mWebView.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkInternetConnection() {
        if (mConManager.getActiveNetworkInfo() != null
                && mConManager.getActiveNetworkInfo().isAvailable()
                && mConManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void setValue(int progress) {
        this.mProgress.setProgress(progress);
    }

    /*@OnClick(R.id.webview_goback)*/
    void goBack(){
        if(mWebView == null) return;

        if(mWebView.canGoBack()){
            mWebView.goBack();
        }
    }

    /*@OnClick(R.id.webview_goforward)*/
    void goForward(){
        if(mWebView == null) return;

        if(mWebView.canGoForward()){
            mWebView.goForward();
        }
    }

    /*@OnClick(R.id.webview_refresh)*/
    void refresh(){
        if(mWebView == null) return;

        mWebView.loadUrl(initilaPageUrl);
    }
}
