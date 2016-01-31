package com.insuranceline.data.vo;

import com.insuranceline.data.local.AppDatabase;
import com.insuranceline.utils.Validation;
import com.insuranceline.utils.ValidationFailedException;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by zeki on 31/01/2016.
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
}
