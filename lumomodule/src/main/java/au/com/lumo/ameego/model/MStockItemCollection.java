package au.com.lumo.ameego.model;

import java.util.ArrayList;

/**
 * Created by zeki on 26/07/15.
 */
public class MStockItemCollection {
    private int                   $id;
    private ArrayList<MStockItem> $values;

    public ArrayList<MStockItem> getStockItems() {
        return $values;
    }
}
