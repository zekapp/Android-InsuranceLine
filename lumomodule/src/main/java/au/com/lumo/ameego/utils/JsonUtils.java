package au.com.lumo.ameego.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by appscoredev2 on 8/07/15.
 */
public class JsonUtils {

    public static final String TAG = JsonUtils.class.getSimpleName();

    public static String convertObjToJsonString(Object obj,  Type t) {
        Gson gson = new Gson();
        String json = gson.toJson(obj, t);
//        LogUtils.longInfo(TAG,"convertObjToJsonString: " + json);
        return json;
    }

    public static <T> JsonArray convertJsonToObjArray(Object obj, Class<T> t) throws JsonSyntaxException {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(obj, new TypeToken<ArrayList<T>>(){}.getType());
        return element.getAsJsonArray();
    }

    public static <T> T convertJsonToObj(String json, Class<T> t) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, t );
    }

    public static JSONObject makeNearMeReqJson(LatLng mCurLocation, String query, int distance) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("distance",distance);
            jsonObject.put("latitude",mCurLocation.latitude);
            jsonObject.put("longitude",mCurLocation.longitude);
            jsonObject.put("searchKey",query);
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d(TAG, "makeNearMeReqJson error: " + e.getMessage());
            throw e;
        }

//        Log.d(TAG, "makeNearMeReqJson : " + jsonObject.toString());
        return jsonObject;
    }
}
