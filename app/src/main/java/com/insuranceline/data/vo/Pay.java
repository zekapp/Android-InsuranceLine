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
}
