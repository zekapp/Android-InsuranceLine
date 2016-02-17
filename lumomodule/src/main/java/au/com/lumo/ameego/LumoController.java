package au.com.lumo.ameego;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.utils.PrefUtils;

/**
 * Created by Zeki Guler on 17,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public enum LumoController {
    INSTANCE;

    private Context mContext;
    private MUser   mUser;
    private RequestQueue mRequestQueue;
    private MainAppCallback mCallback;

    private LumoController build(Builder builder) {
        this.mContext = builder.mContext;
        this.mCallback = builder.mCallback;

        mUser         = MUser.getUser(mContext);
        mRequestQueue = Volley.newRequestQueue(mContext);

        return this;
    }

    public static LumoController getInstance() {
        return INSTANCE;
    }

    public void  setMainAppCallback(MainAppCallback callback){
        mCallback = callback;
    }

    public MUser getUser(){
        return this.mUser;
    }

    public void saveUser(MUser mdUser) {
        this.mUser = mdUser;
        MUser.saveUser(mContext, mdUser);
        if(mdUser != null) {
            PrefUtils.saveUserEmail(mContext, mdUser.getUsername());
        }
    }

    public MainAppCallback getMainAppCalback(){
        return mCallback;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "LumoController" : tag);
        mRequestQueue.add(req);
    }

    public Class<?> getSplashActivity() {
        return DispatchActivity.class;
    }

    public static class Builder{
        private Context         mContext;
        private MainAppCallback mCallback;

        private Builder(){
            mContext = null;
        }

        public Builder(Context context){
            this.mContext = context;
        }

        public Builder setCallback(MainAppCallback callback){
            mCallback = callback;
            return this;
        }

        public LumoController build(){
            return LumoController.INSTANCE.build(this);
        }

    }










/*    private static LumoController mInstance;

    private MUser        mMdUser;
    private RequestQueue mRequestQueue = null;
    private Context mContext;

    public LumoController(Context context){

        mContext  = context;
        mMdUser   = MUser.getUser(mContext);
        mInstance = this;
    }


    public static synchronized LumoController getInstance() {
//        if (mInstance == null) {mInstance = new LumoController(context);}
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
    }*/
}
