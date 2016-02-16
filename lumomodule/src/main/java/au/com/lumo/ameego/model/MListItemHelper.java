package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class MListItemHelper implements Serializable{
    private int $id;
    private ArrayList<MListItem>  $values;

    public ArrayList<MListItem> getListItems() {
        return $values;
    }
}
