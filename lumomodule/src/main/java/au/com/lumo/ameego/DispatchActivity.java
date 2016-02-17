package au.com.lumo.ameego;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MSiteHelper;
import au.com.lumo.ameego.model.MSiteNodeVm;
import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.LumoSpecificUtils;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;


public class DispatchActivity extends Activity {
    private static final String TAG = DispatchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MUser user = LumoController.getInstance().getUser();

        if(user == null){
            directUserToWelcomePage();
        }else {
            fetchSiteNode();
        }
    }

    private void fetchSiteNode() {
        WarningUtilsMD.startProgresslDialog("Please wait...", this);
        NetworkManager.fetchSiteNodeVM(new GenericCallback<MSiteHelper>() {
            @Override
            public void done(MSiteHelper site, Exception e) {
                WarningUtilsMD.stopProgress();
                if (e == null) {
                    try {

                        updateAndSaveSite(site);
                        updateAndSaveUser(site);

                        directUserToAccordingToResponse(site);
                    } catch (Exception e1) {
                        e1.printStackTrace();
//                        Toast.makeText(DispatchActivity.this, "Error: " + e1.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
//                    Toast.makeText(DispatchActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    //allow user to see categories
                    directUserToMainActivity();
                }
            }
        });
    }

    private void updateAndSaveSite(MSiteHelper site) throws Exception {
        ArrayList<MSiteNodeVm> temp = new ArrayList<>();
        for (MSiteNodeVm node : LumoSpecificUtils.getProperSiteNodeArray(site.getNodes())) {
            if (LumoSpecificUtils.isInMainCategory(node.getSiteNodeId()))
                temp.add(node);
        }

        PrefUtils.saveSiteNodeList(DispatchActivity.this, temp);
    }

    private void directUserToAccordingToResponse(MSiteHelper site) {
        MUser user = site.getMember();

//        directUserToTermAndConditionForSign();
//        directUserToQuestionnaireActivity();

        if(!user.isTermsAndConditionsAccepted() && !PrefUtils.isTermAndCondSeen(this)){
            // go to terms and condition
            directUserToTermAndConditionForSign();
        }else if( !user.isDemographicQuestionsAnswered()){
            // go to questioner.
            PrefUtils.setTermAndCondSeen(this, false);
            directUserToQuestionnaireActivity();
        }else{
            // go to main activity
            PrefUtils.setTermAndCondSeen(this, false);
            directUserToMainActivity();
        }
    }

    private void updateAndSaveUser(MSiteHelper site) {
        MUser temp = site.getMember();

        MUser savedUser = LumoController.getInstance().getUser();

        savedUser.setAppId(temp.getAppId());
        savedUser.setEmail(temp.getEmail());
        savedUser.setClientId(temp.getClientId());
        savedUser.setUsername(temp.getUsername());
        savedUser.setLastName(temp.getLastName());
        savedUser.setFirstName(temp.getFirstName());
        savedUser.setAccountExpiryDate(temp.getAccountExpiryDate());
        savedUser.setContactPhoneNumber(temp.getContactPhoneNumber());
        savedUser.setDemographicQuestionnaireID(temp.getDemographicQuestionnaireID());
        savedUser.setDemographicQuestionsAnswered(temp.isDemographicQuestionsAnswered());
        savedUser.setDemographicQuestionsRequired(temp.isDemographicQuestionsRequired());

        showInLog(savedUser);

        LumoController.getInstance().saveUser(savedUser);
//        PrefUtils.saveUser(this, savedUser);
    }

    private void showInLog(MUser savedUser) {
//        Log.d(TAG, "getAppId                        :" + savedUser.getAppId());
//        Log.d(TAG, "getEmail                        :" + savedUser.getEmail());
//        Log.d(TAG, "getClientId                     :" + savedUser.getClientId());
//        Log.d(TAG, "getUsername                     :" + savedUser.getUsername());
//        Log.d(TAG, "getFirstName                    :" + savedUser.getFirstName());
//        Log.d(TAG, "getLastName                     :" + savedUser.getLastName());
//        Log.d(TAG, "getAccountExpiryDate            :" + savedUser.getAccountExpiryDate());
//        Log.d(TAG, "getContactPhoneNumber           :" + savedUser.getContactPhoneNumber());
//        Log.d(TAG, "isDemographicQuestionsAnswered  :" + savedUser.isDemographicQuestionsAnswered());
//        Log.d(TAG, "isDemographicQuestionsRequired  :" + savedUser.isDemographicQuestionsRequired());
//        Log.d(TAG, "isTermsAndConditionsAccepted    :" + savedUser.isTermsAndConditionsAccepted());
//        Log.d(TAG, "getDemographicQuestionnaireID   :" + savedUser.getDemographicQuestionnaireID());
    }

    private void directUserToTermAndConditionForSign() {
        Intent i = new Intent(this, TermAndConditionActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void directUserToQuestionnaireActivity() {
        Intent i = new Intent(this, QuestionnaireActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void directUserToWelcomePage() {
        MainAppCallback callback = LumoController.getInstance().getMainAppCalback();
        if (callback != null) callback.directUserToWelcomePage();

/*        Intent i = new Intent(this, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);*/
    }

    private void directUserToMainActivity() {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.Extra.IS_LAUNCHER, true);
        startActivity(intent);
    }
}
