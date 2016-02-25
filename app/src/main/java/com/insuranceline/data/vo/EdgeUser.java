package com.insuranceline.data.vo;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.local.AppDatabase;
import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.remote.responses.EdgeWhoAmIResponse;
import com.insuranceline.utils.Validation;
import com.insuranceline.utils.ValidationFailedException;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import au.com.lumo.ameego.model.MUser;

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

    @Column(defaultValue = "false")
    boolean isFitBitUser = false;

    @Column(defaultValue = "false")
    boolean isTermCondAccepted = false;

    private MUser lumoUser;

    public EdgeUser(){

    }

    private EdgeUser(Builder builder) {
        email = builder.mEdgeWhoAmIResponse.memberRecord.email;
        mAccessToken = builder.mEdgeAuthResponse.getmAccessToken();
        mTokenType = builder.mEdgeAuthResponse.getmTokenType();
        mExpireIn = builder.mEdgeAuthResponse.getmExpireIn();
        isTermCondAccepted = builder.mEdgeWhoAmIResponse.memberRecord.termsAndConditionsAccepted;
        isFitBitUser = builder.isFitBitUser;
        lumoUser = builder.lumoUser;
    }

    @Override
    public void validate() {
        if (email == null || email.isEmpty())
            throw new ValidationFailedException("invalid user email");
    }

    public MUser getLumoUser() {
        return lumoUser;
    }


    public static class Builder {

        private final EdgeWhoAmIResponse mEdgeWhoAmIResponse;
        private final EdgeAuthResponse mEdgeAuthResponse;
        private MUser lumoUser;
        private boolean isDebugEnabled = false;
        private boolean isFitBitUser;

        private Builder() {
            mEdgeWhoAmIResponse = null;
            mEdgeAuthResponse = null;

        }

        public Builder(EdgeWhoAmIResponse edgeWhoAmIResponse, EdgeAuthResponse edgeAuthResponse, boolean isFitbitUser) {
            mEdgeWhoAmIResponse = edgeWhoAmIResponse;
            mEdgeAuthResponse = edgeAuthResponse;
            isFitBitUser = isFitbitUser;
        }


        public EdgeUser build() {
            return new EdgeUser(this);
        }

        public Builder createMUser() {
            lumoUser = new MUser();

            // Update Field Using mEdgeWhoAmIResponse
            assert mEdgeWhoAmIResponse != null;
            lumoUser.setAppId(mEdgeWhoAmIResponse.memberRecord.appId);
            lumoUser.setEmail(mEdgeWhoAmIResponse.memberRecord.email);
            lumoUser.setClientId(mEdgeWhoAmIResponse.memberRecord.clientId);
            lumoUser.setUsername(mEdgeWhoAmIResponse.memberRecord.username);
            lumoUser.setLastName(mEdgeWhoAmIResponse.memberRecord.lastName);
            lumoUser.setFirstName(mEdgeWhoAmIResponse.memberRecord.firstName);
            lumoUser.setAccountExpiryDate(mEdgeWhoAmIResponse.memberRecord.accountExpiryDate);
            lumoUser.setContactPhoneNumber(mEdgeWhoAmIResponse.memberRecord.contactPhoneNumber);
            lumoUser.setDemographicQuestionnaireID(mEdgeWhoAmIResponse.memberRecord.demographicQuestionnaireID);
            lumoUser.setDemographicQuestionsAnswered(mEdgeWhoAmIResponse.memberRecord.demographicQuestionsAnswered);
            lumoUser.setDemographicQuestionsRequired(mEdgeWhoAmIResponse.memberRecord.demographicQuestionsAnswered);

            // Update Field Using mEdgeAuthResponse
            assert mEdgeAuthResponse != null;
            lumoUser.setAccess_token(mEdgeAuthResponse.getmAccessToken());
            lumoUser.setToken_type(mEdgeAuthResponse.getmTokenType());
            lumoUser.setExpires_in(mEdgeAuthResponse.getmExpireIn());

            return this;
        }

        public Builder setDebugEnable(boolean debugEnable, boolean fitbitUser) {
            isDebugEnabled = debugEnable;
            isFitBitUser = fitbitUser;
            return this;
        }
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
