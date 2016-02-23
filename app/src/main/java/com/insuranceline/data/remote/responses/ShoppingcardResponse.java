package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zeki on 24/02/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShoppingCardResponse {
    public boolean success;
}
