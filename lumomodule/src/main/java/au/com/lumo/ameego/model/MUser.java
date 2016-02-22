package au.com.lumo.ameego.model;

import android.content.Context;

import au.com.lumo.ameego.utils.PrefUtils;

/**
 * Created by Zeki Guler on 7/07/15.
 */
public class MUser {
    private long    expires_in;
    private String  username;
    private String  access_token;
    private String  token_type;
    private String  error;
    private String  error_description;
    private String  password;
    private String  firstName;
    private String  lastName;
    private String  email;
    private String  contactPhoneNumber;
    private boolean termsAndConditionsAccepted;
    private boolean demographicQuestionsAnswered;
    private boolean demographicQuestionsRequired;
    private int     demographicQuestionnaireID;
    private String  appId;
    private String  clientId;
    private String  accountExpiryDate;

    public static MUser getUser(Context context) {
        return PrefUtils.getUser(context);
    }

    public static void saveUser(Context context, MUser mdUser) {
        PrefUtils.saveUser(context, mdUser);
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public boolean isTermsAndConditionsAccepted() {
        return termsAndConditionsAccepted;
    }

    public void setTermsAndConditionsAccepted(boolean termsAndConditionsAccepted) {
        this.termsAndConditionsAccepted = termsAndConditionsAccepted;
    }

    public boolean isDemographicQuestionsAnswered() {
        return demographicQuestionsAnswered;
    }

    public void setDemographicQuestionsAnswered(boolean demographicQuestionsAnswered) {
        this.demographicQuestionsAnswered = demographicQuestionsAnswered;
    }

    public boolean isDemographicQuestionsRequired() {
        return demographicQuestionsRequired;
    }

    public void setDemographicQuestionsRequired(boolean demographicQuestionsRequired) {
        this.demographicQuestionsRequired = demographicQuestionsRequired;
    }

    public int getDemographicQuestionnaireID() {
        return demographicQuestionnaireID;
    }

    public void setDemographicQuestionnaireID(int demographicQuestionnaireID) {
        this.demographicQuestionnaireID = demographicQuestionnaireID;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAccountExpiryDate() {
        return accountExpiryDate;
    }

    public void setAccountExpiryDate(String accountExpiryDate) {
        this.accountExpiryDate = accountExpiryDate;
    }

}
