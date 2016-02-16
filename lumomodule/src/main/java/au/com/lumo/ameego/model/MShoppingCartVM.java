package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by Zeki Guler on 24/07/15.
 */
public class MShoppingCartVM implements Serializable{
    private int                  shoppingCartId;        // The system key for identifying this shopping cart
    private MShoppingCartItems   shoppingCartItems;
    private double               subTotal;
    private double               total;
    private double               freightCharge;
    private MCourierType         courierType;           // CourierType
    private double               creditCardFee;
    private double               handlingFee;
    private String               deliveryType;
    private double               gst;
    private boolean              requiresFreight;
    private boolean              isValid;               //
    private MAddress             deliveryAddress;       // AddressVM
    private MAddress             billingAddress;        // AddressVM
    private MPhoneNumber         contactPhoneNumber;    // PhoneNumber
    private MCardDetails         cardDetails;           // CardDetails
    private int                  shoppingCartStatus;
    private String               emailAddress;
    private MCartErrorMessage    errorMessages;

    public MCartErrorMessage getErrorMessages() {
        return errorMessages;
    }

    public int getShoppingCartId() {
        return shoppingCartId;
    }

    public MShoppingCartItems getShoppingCartItemsHelper() {
        return shoppingCartItems;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotal() {
        return total;
    }

    public double getFreightCharge() {
        return freightCharge;
    }

    public MCourierType getCourierType() {
        return courierType;
    }

    public double getCreditCardFee() {
        return creditCardFee;
    }

    public double getHandlingFee() {
        return handlingFee;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public double getGst() {
        return gst;
    }

    public boolean isRequiresFreight() {
        return requiresFreight;
    }

    public boolean isValid() {
        return isValid;
    }

    public MAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public MAddress getBillingAddress() {
        return billingAddress;
    }

    public MPhoneNumber getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public MCardDetails getCardDetails() {
        return cardDetails;
    }

    public int getShoppingCartStatus() {
        return shoppingCartStatus;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
