package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 24/07/15.
 */
public class MShoppingCartHelper implements Serializable {
    private int              $id;
    private MShoppingCartVM  shoppingCart;
    private boolean          success;
    private MErrorHelper     errors;

    public MShoppingCartVM getShoppingCartOverall() {
        return shoppingCart;
    }

    public boolean isSuccess() {
        return success;
    }

    public MErrorHelper getErrors() {
        return errors;
    }
}
