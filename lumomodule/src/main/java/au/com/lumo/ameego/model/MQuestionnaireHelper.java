package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class MQuestionnaireHelper implements Serializable{
    private int            $id;
    private MQuestionnaire questionnaire;
    private boolean        completed;
    private boolean        success;
    private MErrorHelper   errors;

    public int get$id() {
        return $id;
    }

    public MQuestionnaire getQuestionnaire() {
        return questionnaire;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isSuccess() {
        return success;
    }

    public MErrorHelper getErrors() {
        return errors;
    }
}
