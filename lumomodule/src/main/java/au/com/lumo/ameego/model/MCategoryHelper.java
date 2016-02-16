package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MCategoryHelper implements Serializable{
    private int                  $id;
    private ArrayList<MCategory> $values;

    public ArrayList<MCategory> get$values() {
        return $values;
    }
}
