package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class MQuestion implements Serializable{
    private int             $id;                //
    private String          questionName;       // The name of the question
    private boolean         showQuestionNumber; // Whether the QuestionName should be displayed on the page
    private int             pageNumber;         // The page upon which this question should be displayed
    private String          questionText;       // THe text of the question to display to the respondent
    private boolean         mandatory;          // Whether the respondent must provide an answer to the question or not
    private int             pageOrder;          // The order on the page in which this question should be displayed
    private MListItemHelper listItems;          // The list of possible responses for this question when the questiondatatype is of type List
    private int             listColums;         // The number of suggested colums to break the associated list into
    private int             questionType;       /** {@link au.com.lumo.ameego.utils.Constants.QuestionType} */
    private int             questionDataType;   /** {@link au.com.lumo.ameego.utils.Constants.QuestionDataType} */

    public String getQuestionName() {
        return questionName;
    }

    public boolean isShowQuestionNumber() {
        return showQuestionNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public int getPageOrder() {
        return pageOrder;
    }

    public MListItemHelper getListItemsHelper() {
        return listItems;
    }

    public int getListColums() {
        return listColums;
    }

    public int getQuestionType() {
        return questionType;
    }

    public int getQuestionDataType() {
        return questionDataType;
    }
}
