package au.com.lumo.ameego.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MSiteNodeVm;
import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.model.PaymentDetails;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class PrefUtils {

    private static final String PREF_USER               = "PREF_USER";
    private static final String PREF_SITE_NODE_LIST     = "PREF_SITE_NODE_LIST";
    private static final String PREF_SHOPPING_CARD_LIST = "PREF_SHOPPING_CARD_LIST";
    private static final String PREF_CART               = "PREF_CART";
    private static final String PREF_PAYMENT_DETAIL     = "PREF_PAYMENT_DETAIL";
    private static final String PREF_IS_TC_SEEN         = "PREF_IS_TC_SEEN";

    private static final String PREF_USER_EMAIL         = "PREF_USER_EMAIL";

    public static String getUserEmail(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(PREF_USER_EMAIL, "");
        if(json.isEmpty()) {
            return "";
        }else{
            String s;
            try {
                s = new String(AESEncryption.decrypt(Base64.decode(json, Base64.NO_WRAP)));
            }catch(Exception e){
                s = "";
            }
            return s;
        }
    }

    public static void saveUserEmail(Context context, String email) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String s = Base64.encodeToString(AESEncryption.encrypt(email.getBytes()), Base64.NO_WRAP);
        sp.edit().putString(PREF_USER_EMAIL,s).commit();
    }


    public static MUser getUser(Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(PREF_USER, "");
        return gson.fromJson(json, MUser.class);
    }

    public static void saveUser(Context context, MUser mdUser) {
        Gson gson = new Gson();
        String json = gson.toJson(mdUser);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_USER,json).commit();
    }

    public static void saveSiteNodeList(Context context, ArrayList<MSiteNodeVm> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_SITE_NODE_LIST,json).commit();
    }

    public static ArrayList<MSiteNodeVm> getSavedSiteNodeList(Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(PREF_SITE_NODE_LIST, "");
        return gson.fromJson(json, new TypeToken<ArrayList<MSiteNodeVm>>(){}.getType());
    }

    public static void saveCart(Context context, MShoppingCartVM cart) {
        Gson gson = new Gson();
        String json = gson.toJson(cart);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(PREF_CART,json).commit();
    }

    public static MShoppingCartVM getCart(Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(PREF_CART, "");
        return gson.fromJson(json, MShoppingCartVM.class);
    }

    public static void savePaymentDetails(Context context, PaymentDetails paymentDetails) {
        Gson gson = new Gson();
        String json = gson.toJson(paymentDetails);
        /**
         * Karl added encryption to Credit card information
         */
        String encrypted_json = Base64.encodeToString(AESEncryption.encrypt(json.getBytes()), Base64.NO_WRAP);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        sp.edit().putString(PREF_PAYMENT_DETAIL,encrypted_json).apply();
    }

    public static PaymentDetails getPaymentDetails(Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String encrypted_json = sp.getString(PREF_PAYMENT_DETAIL, "");

        /**
         * Karl added decryption to Credit card information
         */
        if(encrypted_json.isEmpty()) {
            return gson.fromJson("", PaymentDetails.class);
        }else{
            String json;
            try {
                json = new String(AESEncryption.decrypt(Base64.decode(encrypted_json, Base64.NO_WRAP)));
            }catch(Exception e){
                json = "";
                deletePaymentDetails(context);
            }
            return gson.fromJson(json, PaymentDetails.class);
        }
    }

    public static void deletePaymentDetails(Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().remove(PREF_PAYMENT_DETAIL).commit();
    }


    public static void setTermAndCondSeen(Context context, boolean isSeen) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_IS_TC_SEEN, isSeen).commit();
    }

    public static boolean isTermAndCondSeen(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_IS_TC_SEEN, false);
    }
}
