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
        public int paymentType = 99; // don't change
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
