package com.insuranceline.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insuranceline.data.remote.responses.APIError;

import java.io.IOException;

/**
 * Created by zeki on 31/01/2016.
 */
public class ErrorUtils {

    public static APIError parseError(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, APIError.class);
    }
}
