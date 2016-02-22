package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import au.com.lumo.ameego.model.MUser;

/**
 * Created by Zeki Guler on 22,February,2016
 * ©2015 Appscore. All Rights Reserved
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WhoAmIResponse {

    public MemberRecord memberRecord;

    public MUser convertMUser() {
        MUser user = new MUser();

        user.setAppId(memberRecord.appId);
        user.setEmail(memberRecord.email);
        user.setClientId(memberRecord.clientId);
        user.setUsername(memberRecord.username);
        user.setLastName(memberRecord.lastName);
        user.setFirstName(memberRecord.firstName);
        user.setAccountExpiryDate(memberRecord.accountExpiryDate);
        user.setContactPhoneNumber(memberRecord.contactPhoneNumber);
        user.setDemographicQuestionnaireID(memberRecord.demographicQuestionnaireID);
        user.setDemographicQuestionsAnswered(memberRecord.demographicQuestionsAnswered);
        user.setDemographicQuestionsRequired(memberRecord.demographicQuestionsRequired);

        return user;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class MemberRecord {
        public String username;
        public String firstName;
        public String lastName;
        public String email;
        public String contactPhoneNumber;
        public String appId;
        public String password;
        public String clientId;
        public String accountExpiryDate;
        public int     demographicQuestionnaireID;
        public boolean termsAndConditionsAccepted;
        public boolean demographicQuestionsAnswered;
        public boolean demographicQuestionsRequired;
    }
}
