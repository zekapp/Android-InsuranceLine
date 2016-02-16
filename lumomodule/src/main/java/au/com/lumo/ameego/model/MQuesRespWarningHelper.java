package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 3/08/15.
 */
public class MQuesRespWarningHelper implements Serializable{
    private int                         $id;
    private ArrayList<MQuesRespWarning> $values;

    public ArrayList<MQuesRespWarning> getWarnings() {
        return $values;
    }
}
