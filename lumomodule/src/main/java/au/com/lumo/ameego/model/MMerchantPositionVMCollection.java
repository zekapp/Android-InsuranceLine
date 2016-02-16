package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zeki on 26/07/15.
 */
public class MMerchantPositionVMCollection implements Serializable{
    private int                             $id;
    private ArrayList<MMerchantPositionVM> $values;

    public ArrayList<MMerchantPositionVM> getMercHPosList() {
        return $values;
    }

}
