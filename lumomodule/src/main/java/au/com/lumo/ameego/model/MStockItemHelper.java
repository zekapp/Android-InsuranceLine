package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 17/07/15.
 */
public class MStockItemHelper implements Serializable{
    private boolean        success;
    private MStockItem     stockItem;
    private MErrorHelper   errors;

    public boolean isSuccess() {
        return success;
    }

    public MStockItem getStockItem() {
        return stockItem;
    }

    public MErrorHelper getErrors() {
        return errors;
    }
}
