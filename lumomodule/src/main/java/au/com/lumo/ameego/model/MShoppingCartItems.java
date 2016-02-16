package au.com.lumo.ameego.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zeki Guler on 24/07/15.
 */
public class MShoppingCartItems implements Serializable{

    private int                          $id;                // The system key for identifying this shopping cart
    private ArrayList<MShoppingCartItem> $values;            // Collection of ShoppingCartItemVM

    public ArrayList<MShoppingCartItem> getItems() {
        return $values;
    }
}
