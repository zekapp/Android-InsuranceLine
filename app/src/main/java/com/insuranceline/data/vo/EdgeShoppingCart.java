package com.insuranceline.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zeki Guler on 24,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdgeShoppingCart {
    public BillingAddress       billingAddress;
    public ContatctNumber       contactPhoneNumber;
    public ShoppingCartItemVM   shoppingCartItems;
    public String               emailAddress; // this field required in project perspective

    public EdgeShoppingCart(){
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class BillingAddress{
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class ContatctNumber{
         public int number = 3;
         public String dateCreated = "2016-02-23T10:29:35.57697+11:00";
         public String lastUpdated = "2016-02-23T10:29:35.57697+11:00";

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class ShoppingCartItemVM{
        public List<ItemValue> $values;

        @JsonIgnoreProperties(ignoreUnknown = true)
        static public class ItemValue{
            public int quantity = 1; // don't change
            public int stockItemId; // this field required in project perspective*/
        /*public String SKU;*/
        }
    }

    private EdgeShoppingCart (Builder builder) {

        billingAddress     = builder.mBillingAddress;
        contactPhoneNumber = builder.mContatctNumber;
        shoppingCartItems  = builder.mShoppingCartItemVM;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        final BillingAddress mBillingAddress;
        final ContatctNumber mContatctNumber;
        final ShoppingCartItemVM mShoppingCartItemVM;

        public Builder(){
            mBillingAddress = new BillingAddress();
            mContatctNumber = new ContatctNumber();
            mShoppingCartItemVM = new ShoppingCartItemVM();
            mShoppingCartItemVM.$values = new ArrayList<>();
            mShoppingCartItemVM.$values.add(new ShoppingCartItemVM.ItemValue());
        }

        public EdgeShoppingCart build(){
            return new EdgeShoppingCart(this);
        }
    }
}
