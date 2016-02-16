package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 24/07/15.
 */
public class MShoppingCartItem implements Serializable{
    private int     $id;
    private String  sku;                    // Unique identifier for reward
    private String  productType;            // Product type
    private String  itemName;               // Name of product in shopping cart
    private int     quantity;               // Number of units
    private double  price;                  // Chargable price
    private int     discount;               // Discount obtained for this item
    private String  thumbnailImage;         // image reference for displaying stock image in shopping cart
    private int     fulfilmentType;         /**{@link au.com.lumo.ameego.utils.Constants.ProductType}    */
    private int     stockItemId;            // reference to stock item
    private int     discountType;           /**{@link au.com.lumo.ameego.utils.Constants.DiscountType} */
    private String  discountSymbol;         // The symbol to use when displaying information about the discount offered on the associated stock item
    private String  discountText;           // String
    private String  freightType;            // String
    private int     shoppingCartItemId;     // Unique identifier for this shopping cart item

    public String getSku() {
        return sku;
    }

    public String getProductType() {
        return productType;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public int getFulfilmentType() {
        return fulfilmentType;
    }

    public int getStockItemId() {
        return stockItemId;
    }

    public int getDiscountType() {
        return discountType;
    }

    public String getDiscountSymbol() {
        return discountSymbol;
    }

    public String getDiscountText() {
        return discountText;
    }

    public String getFreightType() {
        return freightType;
    }

    public int getShoppingCartItemId() {
        return shoppingCartItemId;
    }
}
