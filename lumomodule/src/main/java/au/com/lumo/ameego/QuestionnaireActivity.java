package au.com.lumo.ameego;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.adapters.QuestionnaireAdapter;
import au.com.lumo.ameego.callbacks.WarnYesNoSelectCallback;
import au.com.lumo.ameego.interfaces.IAdapterPresenter;
import au.com.lumo.ameego.interfaces.IQuestionnairePresenter;
import au.com.lumo.ameego.interfaces.IQuestionnaireView;
import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuesRespWarning;
import au.com.lumo.ameego.model.MQuestion;
import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.presenters.QuestionnairePresenterImp;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.PrefUtils;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by Zeki Guler on 4/08/15.
 */
public class QuestionnaireActivity extends AppCompatActivity implements IQuestionnaireView {

    /*@Bind(R.id.list)    */ExpandableListView mListView;
    /*@Bind(R.id.toolbar) */Toolbar            mToolbar;

    private int        mQuestionerId;

    private IQuestionnairePresenter mIQuestionnairePresenter;
    private IAdapterPresenter       mIAdapterPresenter;
    private QuestionnaireAdapter    mAdapter;
    private MUser                   mUser;

/*    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        mUser = PrefUtils.getUser(this);

        setToolbar();

        mQuestionerId = mUser.getDemographicQuestionnaireID();
//        mQuestionerId = 1;

        if (mIQuestionnairePresenter == null)
            mIQuestionnairePresenter = new QuestionnairePresenterImp(this);

        if (mAdapter == null){
            mAdapter = new QuestionnaireAdapter(this);
            mIAdapterPresenter = mAdapter;
        }

        setListView();

        mIQuestionnairePresenter.fetchQuestionnaire(mQuestionerId);
    }

    @Override
    public void startProgress() {
        WarningUtilsMD.startProgresslDialog("Please wait...", this);
    }

    @Override
    public void stopProgress() {
        WarningUtilsMD.stopProgress();
    }

    @Override
    public void questionerDownloadedSuccessfully(ArrayList<MQuestion> questions, HashMap<Integer, ArrayList<MListItem>> listItems) {
        mIAdapterPresenter.addAllItemsObject(questions, listItems);

        for(int i=0; i < mAdapter.getGroupCount(); i++)
            mListView.expandGroup(i);
    }

    @Override
    public void problemOccurredWhileFetchingQuestionnaire(String problem) {
//        Toast.makeText(this, "Error: " + problem, Toast.LENGTH_LONG).show();
        /**
         * Karl added
         * if no questionanaire
         */
        closeQuestioner();
    }

    @Override
    public void questionerAnswerUploadedSuccessfully(String exitMessage) {
        Toast.makeText(this, exitMessage, Toast.LENGTH_LONG).show();
//        goToDispatch();\
        closeQuestioner();
    }

    private void goToDispatch() {
        Intent i = new Intent(this, DispatchActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void problemOccurredWhilePushingQuestionnaireAnswer(ArrayList<MQuesRespWarning> warnings, String error) {
        if(error == null){
            Toast.makeText(this,"Please fill the form correctly",Toast.LENGTH_LONG).show();
            mIAdapterPresenter.setWarning(warnings);
        }
//        else
//            Toast.makeText(this,"Error: " + error, Toast.LENGTH_LONG).show();

//        goToDispatch();
    }

    private void setListView() {
        mListView.setAdapter(mAdapter);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
    }

    private void setToolbar() {
        if(mToolbar == null) {
//            Log.d("TEST", "Didn't find a toolbar");
            return;
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        mToolbar.setTitle("Questionnaire");
//        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    /*@OnClick(R.id.ques_done)*/
    void onDoneClicked(){

        String title   = "Submit questionnaire";
        String message = "Thanks for completing our questionnaire. Would you like to submit this to our team?";

        WarningUtilsMD.alertDialogYesNo(title, message, this, "Ok", "Cancel", new WarnYesNoSelectCallback() {
            @Override
            public void done(boolean isYes) {
                if (isYes) {

                    HashMap<Integer, ArrayList<MListItem>> editedOptions = mIAdapterPresenter.getOptions();
                    ArrayList<MQuestion> questions = mIAdapterPresenter.getQuestions();

                    mIQuestionnairePresenter.pushAnswers(mQuestionerId, questions, editedOptions);
                }
            }
        });
    }

    /*@OnClick(R.id.ques_clear)*/
    void closeQuestioner(){
        Intent intent = new Intent(this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.Extra.IS_LAUNCHER, true);
        startActivity(intent);
    }
}
