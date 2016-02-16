package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MAddressHelper implements Serializable{
    private int                 $id;
    private ArrayList<MAddress> $values;

    public ArrayList<MAddress> get$values() {
        return $values;
    }
}
