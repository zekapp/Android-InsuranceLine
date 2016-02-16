package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 3/08/15.
 */
public class MQuestionnaireAnswer implements Serializable{
    private int             questionnaireId; // The id of the questionnaire that these responses relate to
//    private MResponseHelper responses;       // The responses to the questionnaire
    private int             pageNumber;      // The current page of questions being answered (1 for single page questionnaries)
    private ArrayList<MResponse> responses;

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

//    public void setResponses(MResponseHelper responses) {
//        this.responses = responses;
//    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setResponses(ArrayList<MResponse> responses) {
        this.responses = responses;
    }
}
