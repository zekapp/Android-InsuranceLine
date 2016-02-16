package au.com.lumo.ameego.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuesRespWarning;
import au.com.lumo.ameego.model.MQuestion;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public interface IQuestionnaireView {
    /**
     * This interface helps to update view on Questioner View
     * */
    void startProgress();
    void stopProgress();

    /**
     * Each question index should match with the index of HashMap
     * Integer
     *
     * @param questions : header for questionnaire
     * @param listItems : each sub item of header
     * */
    void questionerDownloadedSuccessfully(ArrayList<MQuestion> questions, HashMap<Integer, ArrayList<MListItem>> listItems);
    void problemOccurredWhileFetchingQuestionnaire(String problem);
    void questionerAnswerUploadedSuccessfully(String exitMessage);
    void problemOccurredWhilePushingQuestionnaireAnswer(ArrayList<MQuesRespWarning> error, String e);
}
