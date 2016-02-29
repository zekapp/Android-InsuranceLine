package com.insuranceline.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Zeki Guler on 29,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Pay {
    public CardDetails cardDetails;
    public DeliveryAddress deliveryAddress;
    public String contactEmail = "zeki.guler@appscore.com.au";
    public String contactPhoneNumber = "0413841765";

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class CardDetails{
        public String   cardNumber  =  "000000000000000";
        public int      cardType    =  1;
        public int      paymentType =  99; // don't change
        public String   cardName    =  "dummy user";
        public int      expiryMonth =  9; // get for this months
        public int      expiryYear  =  2017;
        public String   cvv         =  "000";
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class DeliveryAddress{
        public int addressId       = 1;
        public int addressType     = 0;
        public int merchantId      = 10;
        public String $id          = "3";
        public String address1     = "sample string 2";
        public String address2     = "sample string 3";
        public String suburb       = "sample string 4";
        public String state        = "Vic";
        public String postCode     = "3000";
        public String title        = "sample string 9";
        public String displayName  = "sample string 11";
        public float longtitude    = 7.1f;
        public float latitude      = 8.1f;
    }

    private Pay (Builder builder) {
        cardDetails     = builder.mCardDetails;
        deliveryAddress = builder.deliveryAddress;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        final CardDetails mCardDetails;
        final DeliveryAddress deliveryAddress;

        public Builder(){
            mCardDetails = new CardDetails();
            deliveryAddress = new DeliveryAddress();

        }

        public Pay build(){
            return new Pay(this);
        }
    }
}
