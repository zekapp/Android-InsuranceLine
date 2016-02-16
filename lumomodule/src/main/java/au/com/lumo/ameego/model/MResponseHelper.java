package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 3/08/15.
 */
public class MResponseHelper implements Serializable{
    private ArrayList<MResponse> values;

    public void setMResponses(ArrayList<MResponse> $values) {
        this.values = $values;
    }
}
