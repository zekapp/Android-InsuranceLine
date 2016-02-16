package au.com.lumo.ameego.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuestion;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public interface IQuestionnairePresenter {
    void fetchQuestionnaire(int questionnaireId);

    void pushAnswers(int questionnaireId,  ArrayList<MQuestion> questions, HashMap<Integer, ArrayList<MListItem>> editedOptions);
}
