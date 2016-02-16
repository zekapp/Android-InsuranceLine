package au.com.lumo.ameego.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MMerchant implements Serializable{
    private int                      $id;
    private int                      merchantId;            // unique identifier for the merchant
    private String                   name;                  // the name of the merchant
    private String                   email;                 // the contact email address of the merchant
    private String                   shortDescription;      // the short form description of the merchant for use on the sub category page- may contain html
    private String                   longDescription;       // the long form description of the merchant for use on the merchant page - may contain html
    private String                   keywords;              // A list of searchable keywords associated with the merchant
    private MAddressHelper           address;               // A list of addresses where the associated reward can be redeemed
    private MStates                  states;                // A summary of the states of the addresses of the merchant
    private String                   categoryPageImage;
    private String                   mainPageImage;
    private MPhoneNumberHelper       phoneNumbers;          // A list of phone numbers for contacting the merchant
    private MMerchantResourcesHelper merchantResources;     // A list of resources (images etc) associated with the merchant
    private double                   discount;              // The discount that the merchant is offering
    private String                   discountSymbol;        // Provides the display symbol associated with the discount type
    private String                   discountText;          // Textual information that describes the discount where no numeric discount value has been declared (use for DiscountType = Text)
    private int                      discountType;          /** {@link au.com.lumo.ameego.utils.Constants.DiscountType} */
    private int                      associatedStockItemId; // Reverse lookup to find the associated stockitem
    private boolean                  displayAsReward;       // When you iterate through the merchant list, you should include in your display merchants that have DisplayAsReward property set to true
    private String                   programURL;            // Default URL to execute reward. Can be overridden by the associated stock / reward item
    private MStockGroupSummary       stockGroupSummary;     // Aggregated details of the associated stock item

    public int getMerchantId() {
        return merchantId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getKeywords() {
        return keywords;
    }

    public MAddressHelper getAddressHelper() {
        return address;
    }

    public MStates getStates() {
        return states;
    }

    public String getCategoryPageImage() {
        return categoryPageImage;
    }

    public String getMainPageImage() {
        return mainPageImage;
    }

    public MPhoneNumberHelper getPhoneNumbers() {
        return phoneNumbers;
    }

    public MMerchantResourcesHelper getMerchantResources() {
        return merchantResources;
    }

    public double getDiscount() {
        return discount;
    }

    public String getDiscountSymbol() {
        return discountSymbol;
    }

    public int getAssociatedStockItemId() {
        return associatedStockItemId;
    }

    public String getProgramURL() {
        return programURL;
    }

    public MStockGroupSummary getStockGroupSummary() {
        return stockGroupSummary;
    }

    public MAddressHelper getAddress() {
        return address;
    }

    public boolean isDisplayAsReward() {
        return displayAsReward;
    }

    public String getDiscountText() {
        return discountText;
    }

    public int getDiscountType() {
        return discountType;
    }

    public LatLng getLatLng() {
        if(address != null && address.get$values() != null && address.get$values().get(0) != null){
            return new LatLng(address.get$values().get(0).getLatitude(),address.get$values().get(0).getLongtitude());
        }else
            return new LatLng(0,0);
    }

    public Location getLocation() {
        Location loc = new Location("");

        if(address != null && address.get$values() != null && address.get$values().size() > 0 && address.get$values().get(0) != null){
            loc.setLatitude(address.get$values().get(0).getLatitude());
            loc.setLongitude(address.get$values().get(0).getLongtitude());
            return loc;
        }else{
            loc.setLatitude(0);
            loc.setLongitude(0);
            return loc;
        }

    }
}
