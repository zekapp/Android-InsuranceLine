package au.com.lumo.ameego.manager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.com.lumo.ameego.LumoController;
import au.com.lumo.ameego.callbacks.GenericArrayCallBack;
import au.com.lumo.ameego.callbacks.GenericCallback;
import au.com.lumo.ameego.callbacks.GenericTwoReturnCallback;
import au.com.lumo.ameego.model.MAssociatedStockItemHelper;
import au.com.lumo.ameego.model.MCartItemInfo;
import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MMerchantPositionVM;
import au.com.lumo.ameego.model.MMerchantPositionVMHelper;
import au.com.lumo.ameego.model.MNetworkError;
import au.com.lumo.ameego.model.MQuestionnaire;
import au.com.lumo.ameego.model.MQuestionnaireAnswer;
import au.com.lumo.ameego.model.MQuestionnaireAnswerResponse;
import au.com.lumo.ameego.model.MQuestionnaireHelper;
import au.com.lumo.ameego.model.MShoppingCartHelper;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MSiteHelper;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.model.MStockItemHelper;
import au.com.lumo.ameego.model.MUser;
import au.com.lumo.ameego.model.PaymentDetails;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.JsonUtils;
import au.com.lumo.ameego.utils.LumoSpecificUtils;

/**
 * Created by appscoredev2 on 8/07/15.
 */
public class NetworkManager {

    public static final String TAG = NetworkManager.class.getSimpleName();

    private static String handleError(VolleyError error) {
        if (error != null) error.printStackTrace();

        if (error instanceof TimeoutError){
            return "Unable to connect to server. Please try again shortly.";
        } else if (error instanceof NoConnectionError) {
            return "No Internet connection available. Please check your data connection and try again shortly..";
        } else if (error instanceof AuthFailureError) {
            return "Authentication Problem.";
        } else if (error instanceof ServerError) {
            if (error.networkResponse != null && error.networkResponse.data != null){
                MNetworkError erMessage = JsonUtils.convertJsonToObj(new String(error.networkResponse.data), MNetworkError.class);
                return erMessage != null ? erMessage.getError_description() : "Unable to connect to server. Please try again shortly.";
            } else
                return "Unable to connect to server. Please try again shortly.";
        } else if (error instanceof NetworkError) {
            return "Unable to connect to server. Please try again shortly.";
        } else if (error instanceof ParseError) {
            return "Unable to connect to server. Please try again shortly.";
        }else
            return "Unable to connect to server. Please try again shortly.";
    }

    public static void login(final String email, final String password, final GenericCallback<MUser> callback) {
        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.POST,
                Constants.Server.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (callback == null) return;

//                        LogUtils.i(TAG, response);

                        MUser user = JsonUtils.convertJsonToObj(response, MUser.class);

                        if(user != null && (user.getError() == null || user.getError().isEmpty())){
                            callback.done(user, null);
                        }else if(user != null){
                            callback.done(null, new Exception(user.getError_description()));
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                })
                {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username",   email);
                        params.put("password",   password);
                        params.put("grant_type", "password");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Accept",       "application/json");
//                        params.put("Content-Type", "application/x-www.form-urlencoded");
                        params.put("clientId",     Constants.CLIENT_ID);
                        return params;
                    }

                };

        LumoController.getInstance().addToRequestQueue(jsonObjRequest, Constants.RequestTags.LOG_IN_TAG);
    }

    public static void logout(final GenericCallback<MUser> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;

        StringRequest jsonObjRequest = new StringRequest(
                Request.Method.POST,
                Constants.Server.LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (callback == null) return;
                        callback.done(null, null);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }

        };

        LumoController.getInstance().addToRequestQueue(jsonObjRequest, Constants.RequestTags.LOGOUT_TAG);
    }


    public static void fetchSiteNodeVM(final GenericCallback<MSiteHelper> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Constants.Server.SITE_NODE_VM,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "fetchSiteNodeVM incoming : " + json);
                            MSiteHelper siteHelper = JsonUtils.convertJsonToObj(json, MSiteHelper.class);
                            if(siteHelper.isSuccess()){
                                callback.done(siteHelper, null);
                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(siteHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(request, Constants.RequestTags.SITE_NODE_VM_TAG);
    }

    public static void fetchAssociatedStockDetail(int merchantId, int associatedStockItemId, final GenericTwoReturnCallback<MMerchant, MStockItem> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;

//        Log.d(TAG, "fetchAssociatedStockDetail req: " + String.format(Constants.Server.GET_ASSOCIATED_STOCK, merchantId, associatedStockItemId));

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                String.format(Constants.Server.GET_ASSOCIATED_STOCK, merchantId, associatedStockItemId),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "fetchStockDetail incoming : " + json);
                            MAssociatedStockItemHelper stockHelper = JsonUtils.convertJsonToObj(json, MAssociatedStockItemHelper.class);
                            if(true/*stockHelper.isSuccess()*/){ //todo: ask to backend developer to fix it
                                callback.done(stockHelper.getMerchant(), stockHelper.getAssociatedStockItem(),null);
                            }else{
                                callback.done(null, null, new Exception(LumoSpecificUtils.getErrorMessage(stockHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null,null,  e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null,null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(request, Constants.RequestTags.GET_ASSOCIATED_STOCK_TAG);
    }

    public static void fetchStockDetail(int stockId, final GenericCallback<MStockItem> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                String.format(Constants.Server.GET_STOCK, stockId),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "fetchStockDetail incoming : " + json);
                            MStockItemHelper stockHelper = JsonUtils.convertJsonToObj(json, MStockItemHelper.class);
                            if(stockHelper.isSuccess()){
                                callback.done(stockHelper.getStockItem(), null);
                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(stockHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(request, Constants.RequestTags.GET_STOCK_TAG);

    }

    public static void fetchSubCategory(int subCategoryId, final GenericCallback<MCategory> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                String.format(Constants.Server.GET_SUBCATEGORIES, subCategoryId),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "fetchSubCategory incoming : " + json);
                            MCategory mCategory = JsonUtils.convertJsonToObj(json, MCategory.class);
                            //todo: there is no way to check response is correct or not. Ask to backend developer to implement error message
                            callback.done(mCategory, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(request, Constants.RequestTags.GET_SUBCATEGORIES_TAG);

    }
    public static void updateCart(ArrayList<MCartItemInfo> mCurrentCartItemInfo, final GenericCallback<MShoppingCartVM> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) {
            callback.done(null, new Exception("User is null"));
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constants.Server.UPDATE_CART,
                String.valueOf(JsonUtils.convertJsonToObjArray(mCurrentCartItemInfo, MCartItemInfo[].class)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "updateCart incoming : " + json);
                            MShoppingCartHelper cartHelper = JsonUtils.convertJsonToObj(json, MShoppingCartHelper.class);

                            if(cartHelper.isSuccess()){
                                if(cartHelper.getShoppingCartOverall() != null){
                                    callback.done(cartHelper.getShoppingCartOverall(), null);
                                }else{
                                    callback.done(null, new Exception(Constants.Server.UPDATE_CART + " response is Null. "));
                                }

                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(cartHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(jsonRequest, Constants.RequestTags.UPDATE_CART_TAG);
    }

    public static void getLatestCart(final GenericCallback<MShoppingCartVM> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Constants.Server.GET_SHOPPING_CART,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "updateCart incoming : " + json);
                            MShoppingCartHelper cartHelper = JsonUtils.convertJsonToObj(json, MShoppingCartHelper.class);

                            if(cartHelper.isSuccess()){
                                if(cartHelper.getShoppingCartOverall() != null){
                                    callback.done(cartHelper.getShoppingCartOverall(), null);
                                }else{
                                    callback.done(null, new Exception(Constants.Server.GET_SHOPPING_CART + " response is Null. "));
                                }

                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(cartHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(request, Constants.RequestTags.GET_SHOPPING_CART_TAG);
    }

    public static void fetchNearMerchandise(LatLng mCurLocation, String searchQuerry, int distance, final GenericArrayCallBack<MMerchantPositionVM> callback){
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) {
            callback.done(null, new Exception("User is null"));
            return;
        }

        JsonObjectRequest jsonRequest = null;
        try {
            jsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.Server.GET_NEAR_ME,
                    JsonUtils.makeNearMeReqJson(mCurLocation,searchQuerry, distance),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String json = response.toString();
//                                LogUtils.longInfo(TAG, "fetchNearMerchandise incoming : " + json);
                                MMerchantPositionVMHelper helper = JsonUtils.convertJsonToObj(response.toString(), MMerchantPositionVMHelper.class);
                                if(helper.isSuccess()){
                                    if(helper.getPlacesHelper() != null && helper.getPlacesHelper().getMercHPosList() != null){
                                        callback.done(helper.getPlacesHelper().getMercHPosList(), null);
                                    }else{
                                        callback.done(null, new Exception(Constants.Server.GET_NEAR_ME + " response is Null. "));
                                    }

                                }else{
                                    callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(helper.getErrors())));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                callback.done(null, new VolleyError(e.getMessage()));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (callback == null) return;
                            String error = handleError(volleyError);
                            callback.done(null, new Exception(error));
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                    return params;
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
            callback.done(null,e);
        }

        LumoController.getInstance().addToRequestQueue(jsonRequest, Constants.RequestTags.GET_NEAR_ME_TAG);
    }


    /*
    *                         try {
                            String json = response.toString();
                            LogUtils.longInfo(TAG, "updateCart incoming : " + json);
                            MShoppingCartHelper cartHelper = JsonUtils.convertJsonToObj(json, MShoppingCartHelper.class);

                            if(cartHelper.isSuccess()){
                                if(cartHelper.getShoppingCartOverall() != null){
                                    callback.done(cartHelper.getShoppingCartOverall(), null);
                                }else{
                                    callback.done(null, new Exception(Constants.Server.GET_SHOPPING_CART + " response is Null. "));
                                }

                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(cartHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
    * */

    public static void pay(PaymentDetails paymentDetails, final  GenericCallback<MShoppingCartVM> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) {
            callback.done(null, new Exception("User is null"));
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constants.Server.PAY,
                JsonUtils.convertObjToJsonString(paymentDetails, PaymentDetails.class),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "updateCart incoming : " + json);
                            MShoppingCartHelper cartHelper = JsonUtils.convertJsonToObj(json, MShoppingCartHelper.class);

                            if(cartHelper.isSuccess()){
                                callback.done(cartHelper.getShoppingCartOverall(), null);
                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(cartHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LumoController.getInstance().addToRequestQueue(jsonRequest, Constants.RequestTags.PAY_TAG);
    }

    public static void fetchQuestionnaire(final int questionId, final  GenericCallback<MQuestionnaire> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) return;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                String.format(Constants.Server.GET_QUESTIONNAIRE, questionId),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "fetchQuestionnaire incoming : " + json);
                            MQuestionnaireHelper quHelper = JsonUtils.convertJsonToObj(json, MQuestionnaireHelper.class);
//                            if(true /*quHelper.isSuccess()*/){ //TODO: 3/08/15 success always return false ask to backend developer to fix it
                            if(quHelper.isSuccess()){
                                if(quHelper.getQuestionnaire() != null){
                                    callback.done(quHelper.getQuestionnaire(), null);
                                }else{
                                    callback.done(null, new Exception(String.format(Constants.Server.GET_QUESTIONNAIRE, questionId) + " response is Null. "));
                                }
                            }else{
                                callback.done(null, new Exception(LumoSpecificUtils.getErrorMessage(quHelper.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(request, Constants.RequestTags.GET_QUESTIONNAIRE_TAG);
    }

    public static void postQuestionnaire(MQuestionnaireAnswer answer,final  GenericCallback<MQuestionnaireAnswerResponse> callback) {
        final MUser user = LumoController.getInstance().getUser();
        if(user == null) {
            callback.done(null, new Exception("User is null"));
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constants.Server.POST_QUESTIONNAIRE,
                JsonUtils.convertObjToJsonString(answer, MQuestionnaireAnswer.class),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
//                            LogUtils.longInfo(TAG, "postQuestionnaire posting : " + json);
                            MQuestionnaireAnswerResponse resp = JsonUtils.convertJsonToObj(json, MQuestionnaireAnswerResponse.class);

                            if(resp.isSuccess()){
                                callback.done(resp, null);
                            }else{
                                callback.done(resp, new Exception(LumoSpecificUtils.getErrorMessage(resp.getErrors())));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.done(null, e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (callback == null) return;
                        String error = handleError(volleyError);
                        callback.done(null, new Exception(error));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", user.getToken_type() + " " +user.getAccess_token());
                return params;
            }
        };

        LumoController.getInstance().addToRequestQueue(jsonRequest, Constants.RequestTags.POST_QUESTIONNAIRE_TAG);
    }
}
/*

*/