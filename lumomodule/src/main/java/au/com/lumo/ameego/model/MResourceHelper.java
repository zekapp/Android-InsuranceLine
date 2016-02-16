package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MResourceHelper implements Serializable{
    private int                  $id;
    private ArrayList<MResourse> $values;

    public ArrayList<MResourse> get$values() {
        return $values;
    }
}
