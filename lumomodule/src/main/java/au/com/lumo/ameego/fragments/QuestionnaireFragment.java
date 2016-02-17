package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.adapters.QuestionnaireAdapter;
import au.com.lumo.ameego.callbacks.WarnYesNoSelectCallback;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.interfaces.IAdapterPresenter;
import au.com.lumo.ameego.interfaces.IQuestionnairePresenter;
import au.com.lumo.ameego.interfaces.IQuestionnaireView;
import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuesRespWarning;
import au.com.lumo.ameego.model.MQuestion;
import au.com.lumo.ameego.presenters.QuestionnairePresenterImp;
import au.com.lumo.ameego.utils.WarningUtilsMD;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class QuestionnaireFragment extends BaseFragment implements IQuestionnaireView {

    private static final String QUESTIONNAIRE_ID_KEY = "QUESTIONNAIRE_ID_KEY";

    ExpandableListView mListView;

    private IQuestionnairePresenter mIQuestionnairePresenter;
    private IAdapterPresenter       mIAdapterPresenter;
    private int mQuestionerId = 3; //// TODO: 3/08/15 get id from activity
    private QuestionnaireAdapter    mAdapter;

    public static QuestionnaireFragment newInstance() {
        QuestionnaireFragment fragment = new QuestionnaireFragment();
        Bundle                bundle   = new Bundle();

//        bundle.putSerializable(QUESTIONNAIRE_ID_KEY, stockItem);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mQuestionerId = getArguments().getInt(QUESTIONNAIRE_ID_KEY, 3); // TODO: 3/08/15 default
        }

        if (mIQuestionnairePresenter == null)
            mIQuestionnairePresenter = new QuestionnairePresenterImp(this);

        if (mAdapter == null){
            mAdapter = new QuestionnaireAdapter(getActivity());
            mIAdapterPresenter = mAdapter;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ExpandableListView) view.findViewById(R.id.list);
        view.findViewById(R.id.ques_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneClicked();
            }
        });
        setListView();
        mIQuestionnairePresenter.fetchQuestionnaire(mQuestionerId);
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

    @Override
    protected String getTitle() {
        return getString(R.string.questionnaire_title);
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
    protected int getLayout() {
        return R.layout.fragment_questionnaire;
    }

    @Override
    public void startProgress() {
        WarningUtilsMD.startProgresslDialog("Please wait...", getActivity());
    }

    @Override
    public void stopProgress() {
        WarningUtilsMD.stopProgress();
    }

    @Override
    public void questionerDownloadedSuccessfully(ArrayList<MQuestion> questions, HashMap<Integer, ArrayList<MListItem>> options) {
        mIAdapterPresenter.addAllItemsObject(questions, options);

        for(int i=0; i < mAdapter.getGroupCount(); i++)
            mListView.expandGroup(i);
    }

    @Override
    public void problemOccurredWhileFetchingQuestionnaire(String problem) {
        Toast.makeText(getActivity(),"" + problem,Toast.LENGTH_LONG).show();
    }

    @Override
    public void questionerAnswerUploadedSuccessfully(String exitMessage) {
        Toast.makeText(getActivity(), ""+exitMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void problemOccurredWhilePushingQuestionnaireAnswer(ArrayList<MQuesRespWarning> warnings, String error) {
        if(error == null){
//            Toast.makeText(getActivity(),"Please fill the form correctly",Toast.LENGTH_LONG).show();
            mIAdapterPresenter.setWarning(warnings);
        }
        else
            Toast.makeText(getActivity(),"" + error, Toast.LENGTH_LONG).show();
    }

    /*@OnClick(R.id.ques_done)*/
    void onDoneClicked(){

        String title   = "Submit questionnaire";
        String message = "Thanks for completing our questionnaire. Would you like to submit this to our team?";

        WarningUtilsMD.alertDialogYesNo(title, message, getActivity(), "Ok", "Cancel", new WarnYesNoSelectCallback() {
            @Override
            public void done(boolean isYes) {
                if (isYes) {

                    HashMap<Integer, ArrayList<MListItem>> editedOptions = mIAdapterPresenter.getOptions();
                    ArrayList<MQuestion> questions                       = mIAdapterPresenter.getQuestions();

                    mIQuestionnairePresenter.pushAnswers(mQuestionerId,questions, editedOptions);
                }
            }
        });

    }
}
