package au.com.lumo.ameego.presenters;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.interfaces.IQuestionnairePresenter;
import au.com.lumo.ameego.interfaces.IQuestionnaireView;
import au.com.lumo.ameego.manager.NetworkManager;
import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuestion;
import au.com.lumo.ameego.model.MQuestionnaire;
import au.com.lumo.ameego.model.MQuestionnaireAnswer;
import au.com.lumo.ameego.model.MQuestionnaireAnswerResponse;
import au.com.lumo.ameego.model.MResponse;
import au.com.lumo.ameego.model.MResponseItem;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class QuestionnairePresenterImp implements IQuestionnairePresenter {

    private IQuestionnaireView       mIQuestionnaireView;

    public QuestionnairePresenterImp(IQuestionnaireView questionView){
        this.mIQuestionnaireView = questionView;
    }

    @Override
    public void fetchQuestionnaire(int questionnaireId) {
        mIQuestionnaireView.startProgress();
        NetworkManager.fetchQuestionnaire(questionnaireId, new GenericCallback<MQuestionnaire>() {
            @Override
            public void done(MQuestionnaire res, Exception e) {
                mIQuestionnaireView.stopProgress();
                if (e == null) {
                    divideDataToPart(res);
                } else {
                    mIQuestionnaireView.problemOccurredWhileFetchingQuestionnaire(e.getMessage());
                }
            }
        });
    }

    @Override
    public void pushAnswers(int questionnaireId, ArrayList<MQuestion> questions, HashMap<Integer, ArrayList<MListItem>> editedOptions) {
        MQuestionnaireAnswer answer = convertToValidModel(questionnaireId, questions, editedOptions);
        mIQuestionnaireView.startProgress();
        NetworkManager.postQuestionnaire(answer, new GenericCallback<MQuestionnaireAnswerResponse>() {
            @Override
            public void done(MQuestionnaireAnswerResponse res, Exception e) {
                mIQuestionnaireView.stopProgress();
                if (e == null) {
                    mIQuestionnaireView.questionerAnswerUploadedSuccessfully(res.getExitMessage());
                } else if (res != null && res.getWarnings() != null && res.getWarnings().getWarnings() != null) {
                    mIQuestionnaireView.problemOccurredWhilePushingQuestionnaireAnswer(res.getWarnings().getWarnings(), null);
                } else {
                    mIQuestionnaireView.problemOccurredWhilePushingQuestionnaireAnswer(null, e.getMessage());
                }
            }
        });

    }

    /**
     * This function converters/make appropriate answer for editedOptions answer set
     *
     * @param questionnaireId   : id of the questionnaire
     * @param questions         : questions with answer associated. editedOptions index matches with
     *                            question index
     * @param editedOptions     : edited options. According the paramater of options
     *                            answer set will be created.
     *
     * */
    private MQuestionnaireAnswer convertToValidModel(int questionnaireId, ArrayList<MQuestion> questions, HashMap<Integer, ArrayList<MListItem>> editedOptions) {

        MQuestionnaireAnswer answer    = new MQuestionnaireAnswer();

        answer.setQuestionnaireId(questionnaireId);
        answer.setPageNumber(1);

//        MResponseHelper      responseHelper = new MResponseHelper();
        ArrayList<MResponse> responses      = new ArrayList<>();

        int qi = 0;
        for (MQuestion question : questions){

            MResponse response  = new MResponse();

            response.setQuestionName(question.getQuestionName());

//            MResponseItemHelper      responseItemHelper = new MResponseItemHelper();
            ArrayList<MResponseItem> responseItems      = new ArrayList<>();

            for (MListItem option : editedOptions.get(qi)){
                MResponseItem item = new MResponseItem();

//                item.setCode(option.getListCode());
//                item.setBooleanValue(option.isItemSelected());
//                item.setDateTimeValue("2015-08-10T09:40:53.8552381+10:00");
//                item.setStringValue("Test");
                if(option.isItemSelected()){
                    item.setCode(option.getListCode());
                    responseItems.add(item);
                }
            }

            response.setResponseItems(responseItems);

//            responseItemHelper.setMResponseItem(responseItems);

//            response.setResponseHelper(responseItemHelper);
            responses.add(response);

            qi++;
        }

//        responseHelper.setMResponses(responses);

//        answer.setResponses(responseHelper);

        answer.setResponses(responses);

        return answer;
    }

    private void divideDataToPart(MQuestionnaire questionnaire) {
        ArrayList<MQuestion> questions;

        HashMap<Integer, ArrayList<MListItem>> hashMap = new HashMap<>();
        if(questionnaire.getQuestions() != null && questionnaire.getQuestions().getQuestions() != null){
            questions = questionnaire.getQuestions().getQuestions();

            int i = 0;
            for(MQuestion question : questions){
                if(question.getListItemsHelper() != null && question.getListItemsHelper().getListItems() != null)
                hashMap.put(i,question.getListItemsHelper().getListItems());
                i++;
            }

            mIQuestionnaireView.questionerDownloadedSuccessfully(questions, hashMap);
        }else{
            mIQuestionnaireView.problemOccurredWhileFetchingQuestionnaire("Couldn't find questionnaire.");
        }
    }
}
