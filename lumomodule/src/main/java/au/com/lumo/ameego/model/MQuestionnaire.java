package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class MQuestionnaire implements Serializable{
    private int              $id;
    private int              quesionnaireId;        // The id of the questionnaire
    private String           questionnaireTitle;    // The name of the questionnaire
    private String           introductionText;      // Instroductory text to display to the user prior to answering the first question
    private MQuestionsHelper questions;             // the set of questions to be answered

    public int getQuesionnaireId() {
        return quesionnaireId;
    }

    public void setQuesionnaireId(int quesionnaireId) {
        this.quesionnaireId = quesionnaireId;
    }

    public String getQuestionnaireTitle() {
        return questionnaireTitle;
    }

    public void setQuestionnaireTitle(String questionnaireTitle) {
        this.questionnaireTitle = questionnaireTitle;
    }

    public String getIntroductionText() {
        return introductionText;
    }

    public void setIntroductionText(String introductionText) {
        this.introductionText = introductionText;
    }

    public MQuestionsHelper getQuestions() {
        return questions;
    }

    public void setQuestions(MQuestionsHelper questions) {
        this.questions = questions;
    }
}
