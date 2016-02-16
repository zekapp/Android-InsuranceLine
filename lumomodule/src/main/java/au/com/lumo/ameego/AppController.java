package au.com.lumo.ameego;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import au.com.lumo.ameego.model.MUser;

import au.com.lumo.ameego.utils.PrefUtils;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private MUser        mMdUser;
    private RequestQueue mRequestQueue = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mMdUser = MUser.getUser(getApplicationContext());

    }
    public static synchronized AppController getInstance() {
        if (mInstance == null) {mInstance = new AppController();}
        return mInstance;
    }

    public MUser getUser(){
        return this.mMdUser;
    }

    public void saveUser(MUser mdUser) {
        this.mMdUser = mdUser;
        MUser.saveUser(getApplicationContext(), mdUser);
        if(mdUser != null) {
            PrefUtils.saveUserEmail(getApplicationContext(), mdUser.getUsername());
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {mRequestQueue = Volley.newRequestQueue(getApplicationContext());}
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
}
