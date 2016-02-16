package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by zeki on 26/07/15.
 */
public class MMerchantPositionVM implements Serializable{
    private int                  merchantId;
    private String               merchantName;
    private String               address1;
    private String               address2;
    private String               suburb;
    private String               state;
    private String               postCode;
    private double               latitude;
    private double               longitude;
    private String               phoneNumber;
    private MStockItemCollection merchantStockItems;

    public int getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getState() {
        return state;
    }

    public String getPostCode() {
        return postCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public MStockItemCollection getMerchantStockItems() {
        return merchantStockItems;
    }
}
