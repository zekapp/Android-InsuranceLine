package com.insuranceline.utils;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insuranceline.data.remote.responses.APIError;

import java.io.IOException;
import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class JsonConverter {

    public static <T> T convertJsonToObj(String json, Class<T> t) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, t);
    }
}
