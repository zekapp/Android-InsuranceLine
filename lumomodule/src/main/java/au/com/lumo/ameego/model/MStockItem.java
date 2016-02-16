package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MStockItem implements Serializable{
    private int                 $id;
    private int                 stockItemId;
    private String              sku;                        // unique code for this item
    private String              name;                       // The human friendly name for this item
    private String              descriptionShort;           // A short summary of the item - may contain Html
    private MResourceHelper     resources;                  // A list of images that are associated with the item
    private String              descriptionLong;            // A detailed description of the item - may contain Html
    private double              purchasePrice;              // The price the item is sold for
    private int                 productType;                /** {@link au.com.lumo.ameego.utils.Constants.ProductType}    */
    private int                 fulfilmentType;             /** {@link au.com.lumo.ameego.utils.Constants.FulfilmentType} */
    private MStockItemsHelper   children;                   // Other stock items that should be grouped together
    private boolean             isShoppingCartItem;         // Whether the stock item is a saleable item - it may only be information about a deal available from a merchant
    private boolean             displayMerchantsAsRewards;  // Merchants that sell this product should be displayed as salable items
    private boolean             displayAddressesAsRewards;  // whether the addresses of the merchants who sell this product should be displayed or not
    private MMerchantsHelper    merchants;                  // The list of merchants who sell this product
    private double              discount;                   // The discount off the face value that the member is charged
    private int                 discountType;               /** {@link au.com.lumo.ameego.utils.Constants.DiscountType} */
    private String              discountText;               // the text to display when the discount type = DiscountType.Text
    private String              discountSymbol;             // Provides the display symbol associated with the discount type
    private String              terms;                      // details about how to claim the reward
    private String              categoryPageImage;          // image reference for use with stock items displayed on the sub category page
    private String              mainPageImage;              // image reference for use with stock items displayed on their main page
    private String              redemptionInstructions;     // specific instructions for redeeming a merchant offer
    private String              redemptionButtonText;       // text to populate the redemption button
    private boolean             displayNameAsCardType;      // If true, when displaying the list of purchase options, use the name of the product in the option list instead of the purchase price
    private String              redemptionURL;              // url to use in order to redeem a merchant deal - will default to the merchant url if not present.

    public int getStockItemId() {
        return stockItemId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public MResourceHelper getResources() {
        return resources;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public int getProductType() {
        return productType;
    }

    public int getFulfilmentType() {
        return fulfilmentType;
    }

    public MStockItemsHelper getChildren() {
        return children;
    }

    public boolean isShoppingCartItem() {
        return isShoppingCartItem;
    }

    public boolean isDisplayMerchantsAsRewards() {
        return displayMerchantsAsRewards;
    }

    public boolean isDisplayAddressesAsRewards() {
        return displayAddressesAsRewards;
    }

    public MMerchantsHelper getMerchantsHelper() {
        return merchants;
    }

    public double getDiscount() {
        return discount;
    }

    public int getDiscountType() {
        return discountType;
    }

    public String getDiscountText() {
        return discountText;
    }

    public String getDiscountSymbol() {
        return discountSymbol;
    }

    public String getTerms() {
        return terms;
    }

    public String getCategoryPageImage() {
        return categoryPageImage;
    }

    public String getMainPageImage() {
        return mainPageImage;
    }

    public String getRedemptionInstructions() {
        return redemptionInstructions;
    }

    public String getRedemptionButtonText() {
        return redemptionButtonText;
    }

    public String getRedemptionURL() {
        return redemptionURL;
    }

    public MMerchantsHelper getMerchants() {
        return merchants;
    }

    public boolean isDisplayNameAsCardType() {
        return displayNameAsCardType;
    }


}
