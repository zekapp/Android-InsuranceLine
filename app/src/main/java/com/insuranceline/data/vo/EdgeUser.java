package com.insuranceline.data.vo;

import com.insuranceline.data.local.AppDatabase;
import com.insuranceline.utils.Validation;
import com.insuranceline.utils.ValidationFailedException;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Table(databaseName = AppDatabase.NAME)
public class EdgeUser extends BaseModel implements Validation {
    @Column
    @PrimaryKey(autoincrement = false)
    String email;

    @Column
    String mAccessToken;

    @Column
    String mTokenType;

    @Column
    long mExpireIn;

    @Column(defaultValue = "true")
    boolean isFitBitUser = true;

    @Column(defaultValue = "false")
    boolean isTermCondAccepted = false;

    @Override
    public void validate() {
        if (email == null || email.isEmpty())
            throw new ValidationFailedException("invalid user email");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getmTokenType() {
        return mTokenType;
    }

    public void setmTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }

    public long getmExpireIn() {
        return mExpireIn;
    }

    public void setmExpireIn(long mExpireIn) {
        this.mExpireIn = mExpireIn;
    }

    public boolean isFitBitUser() {
        return isFitBitUser;
    }

    public void setFitBitUser(boolean fitBitUser) {
        isFitBitUser = fitBitUser;
    }

    public boolean isTermCondAccepted() {
        return isTermCondAccepted;
    }

    public void setTermCondAccepted(boolean termCondAccepted) {
        isTermCondAccepted = termCondAccepted;
    }
}
