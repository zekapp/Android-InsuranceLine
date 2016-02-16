package au.com.lumo.ameego.model;

import java.io.Serializable;

/**
 * Created by zeki on 27/07/15.
 */
public class PaymentDetails implements Serializable{
    private String       name;
    private String       surname;
    private MCardDetails cardDetails;          // The credit card details to be used to make the purchase
    private MAddress     deliveryAddress;      // The billing and delivery address
    private String       contactEmail;         // The email to use for email communications for this order
    private String       contactPhoneNumber;   // The phone number to use for communications for this order

    public MCardDetails getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(MCardDetails cardDetails) {
        this.cardDetails = cardDetails;
    }

    public MAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(MAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public PaymentDetails setName(String name) {
        this.name = name;
        return this;
    }

    public PaymentDetails setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public PaymentDetails setEmail(String email) {
        this.contactEmail = email;
        return this;
    }

    public PaymentDetails setState(String state) {
        if( deliveryAddress == null ) deliveryAddress = new MAddress();
        deliveryAddress.setState(state);
        return this;
    }

    public PaymentDetails setPostCode(String postcode) {
        if( deliveryAddress == null ) deliveryAddress = new MAddress();
        deliveryAddress.setPostCode(postcode);
        return this;
    }

    public PaymentDetails setSuburb(String suburb) {
        if( deliveryAddress == null ) deliveryAddress = new MAddress();
        deliveryAddress.setSuburb(suburb);
        return this;
    }

    public PaymentDetails setStreetAddress(String streetAd) {
        if( deliveryAddress == null ) deliveryAddress = new MAddress();
        deliveryAddress.setAddress1(streetAd);
        return this;
    }

    public PaymentDetails setPhone(String phone) {
        this.contactPhoneNumber = phone;
        return this;
    }

    public String getState() {
        if(deliveryAddress == null) return " ";
        return deliveryAddress.getState();
    }

    public String getSuburb() {
        if(deliveryAddress == null) return "";
        return deliveryAddress.getSuburb();
    }

    public String getPhone() {
        return this.contactPhoneNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getPostcode() {
        if (deliveryAddress == null) return "";
        return deliveryAddress.getPostCode();
    }

    public String getStreetAd() {
        if (deliveryAddress == null ) return "";
        return deliveryAddress.getAddress1();
    }

    public PaymentDetails setNameOnCard(String nameOnCard) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setCardName(nameOnCard);
        return this;
    }

    public PaymentDetails setCardNumber(String cardnumber) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setCardNumber(cardnumber);
        return this;
    }

    public PaymentDetails setCardCCV(String cardCvv) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setCvv(cardCvv);
        return this;
    }

    public PaymentDetails setExpMont(int setExpMonth) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setExpiryMonth(setExpMonth);
        return this;
    }

    public PaymentDetails setExpYear(int setExpYear) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setExpiryYear(setExpYear);
        return this;
    }

    public PaymentDetails setCardType(Integer caryType) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setCardType(caryType);
        return this;
    }

    public PaymentDetails setExpMonth(int mExpMonth) {
        if(cardDetails == null ) cardDetails = new MCardDetails();
        cardDetails.setExpiryMonth(mExpMonth);
        return this;
    }
}
