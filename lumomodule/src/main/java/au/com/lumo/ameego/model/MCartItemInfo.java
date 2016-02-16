package au.com.lumo.ameego.model;

/**
 * Created by zeki on 27/07/15.
 */
public class MCartItemInfo {
    private int stockItemId;    // The identifier of the stockitem in the shopping cart
    private int quantity;       // The number of units required of this item

    public int getStockItemId() {
        return stockItemId;
    }

    public void setStockItemId(int stockItemId) {
        this.stockItemId = stockItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void decreaseQuantity() {
        quantity = quantity > 0 ? quantity - 1 : 0;
    }

    public void increaseQuantity() {
        quantity++;
    }

}
