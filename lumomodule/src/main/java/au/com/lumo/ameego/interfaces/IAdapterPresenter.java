package au.com.lumo.ameego.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuesRespWarning;
import au.com.lumo.ameego.model.MQuestion;

/**
 * Created by Zeki Guler on 31/07/15.
 */
public interface IAdapterPresenter {
    void addAllItemsObject (ArrayList<MQuestion> headers, HashMap<Integer,ArrayList<MListItem>> children);

    HashMap<Integer,ArrayList<MListItem>> getOptions();

    int getQuestionnaireID();

    void setWarning(ArrayList<MQuesRespWarning> warnings);

    ArrayList<MQuestion> getQuestions();
}
