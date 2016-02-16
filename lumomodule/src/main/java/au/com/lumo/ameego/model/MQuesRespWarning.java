package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 3/08/15.
 */
public class MQuesRespWarning implements Serializable{
    private int $id;
    private String questionName;
    private String code;
    private String warning;

    public String getQuestionName() {
        return questionName;
    }

    public String getCode() {
        return code;
    }

    public String getWarning() {
        return warning;
    }
}
