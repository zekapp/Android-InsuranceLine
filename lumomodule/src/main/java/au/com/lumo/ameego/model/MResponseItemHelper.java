package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zeki on 3/08/15.
 */
public class MResponseItemHelper implements Serializable{
    private ArrayList<MResponseItem> values;

    public void setMResponseItem(ArrayList<MResponseItem> $values) {
        this.values = $values;
    }
}
