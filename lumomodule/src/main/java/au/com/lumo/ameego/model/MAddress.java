package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class MAddress implements Serializable{
    private int    $id;
    private int    addressId;
    private int    addressType;  /** {@link au.com.lumo.ameego.utils.Constants.AddressType} */
    private String address1;
    private String address2;
    private String suburb;
    private String state;
    private String postCode;
    private double longtitude = 0; //default
    private double latitude   = 0; //default
    private String title;
    private int    merchantId;
    private String displayName;


    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setLongtitude(long longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getAddressType() {
        return addressType;
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

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getTitle() {
        return title;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
