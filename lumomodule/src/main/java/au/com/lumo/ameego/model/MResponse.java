package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 3/08/15.
 */
public class MResponse implements Serializable{
    private String              questionName;
//    private MResponseItemHelper responseItems;
    private ArrayList<MResponseItem> responseItems;

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

//    public void setResponseHelper(MResponseItemHelper responseItems) {
//        this.responseItems = responseItems;
//    }


    public void setResponseItems(ArrayList<MResponseItem> responseItems) {
        this.responseItems = responseItems;
    }
}
