package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class MQuestionsHelper implements Serializable{
    private int                  $id;
    private ArrayList<MQuestion> $values;

    public ArrayList<MQuestion> getQuestions() {
        return $values;
    }
}
