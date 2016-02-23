package com.insuranceline.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zeki on 24/02/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCart {
    public BillingAddress billingAddress;
    public ContatctNumber contactPhoneNumber;
    public String         emailAddress; // this field required in project perspective

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class BillingAddress{
        public int $id = 7;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class ContatctNumber{
         public int number = 3;
         public String dateCreated = "2016-02-23T10:29:35.57697+11:00";
         public String lastUpdated = "2016-02-23T10:29:35.57697+11:00";

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class ShoppingCartItemVM{
        public int quantity = 1; // don't change
        public int StockItemId; // this field required in project perspective
    }
}
