package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 3/08/15.
 */
public class MQuestionnaireAnswerResponse implements Serializable{
    private MQuestionnaire          questionnaire;  // THe questionnaire structure with the current page of questions
    private MQuesRespWarningHelper  warnings;
    private boolean                 success;        // Indicates True when the operation was successfully completed or false otherwise
    private String                  exitMessage;    // the exit message to display upon questionnaire completion
    private boolean                 completed;      // whether the last post of resoponses resulted in the completion of the questionnaire
    private MErrorHelper            errors;

    public MQuestionnaire getQuestionnaire() {
        return questionnaire;
    }

    public MQuesRespWarningHelper getWarnings() {
        return warnings;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public boolean isCompleted() {
        return completed;
    }

    public MErrorHelper getErrors() {
        return errors;
    }
}
