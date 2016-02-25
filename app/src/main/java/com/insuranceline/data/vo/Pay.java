package com.insuranceline.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zeki on 24/02/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Pay {
    public CardDetails cardDetails;

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

    private Pay (Builder builder) {
        cardDetails     = builder.mCardDetails;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        final CardDetails mCardDetails;

        public Builder(){
            mCardDetails = new CardDetails();
        }

        public Pay build(){
            return new Pay(this);
        }
    }
}
