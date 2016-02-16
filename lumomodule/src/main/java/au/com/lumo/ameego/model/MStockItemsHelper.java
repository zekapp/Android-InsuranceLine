package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MStockItemsHelper implements Serializable{
    private int                   $id;
    private ArrayList<MStockItem> $values;

    public ArrayList<MStockItem> getStockItems() {
        return $values;
    }

    public void setStockItems(ArrayList<MStockItem> $values) {
        this.$values = $values;
    }
}
