package au.com.lumo.ameego.utils;

import android.support.annotation.DrawableRes;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MAddress;
import au.com.lumo.ameego.model.MCartItemInfo;
import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.model.MError;
import au.com.lumo.ameego.model.MErrorHelper;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MResourse;
import au.com.lumo.ameego.model.MShoppingCartItem;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MSiteNodeVm;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.model.NodesHelper;

/**
 * Created by appscoredev2 on 10/07/15.
 */
public class LumoSpecificUtils {
    private static final String TAG = LumoSpecificUtils.class.getSimpleName();

    public static String getProperCategoryNameFromSiteNode(MSiteNodeVm siteNode) {
        if(siteNode == null){
//            Log.e(TAG, "MSiteNodeVm is null");
            return "";
        }

        // Display text - when present, should override any display text from child elements
        if(siteNode.getText() != null && !siteNode.getText().isEmpty())
            return siteNode.getText().trim();

        if(siteNode.getCategory() == null){
//            Log.e(TAG, "MSiteNodeVm is OK, MCategory is null");
            return "";
        }

        if(siteNode.getCategory().getCategoryName() != null && !siteNode.getCategory().getCategoryName().isEmpty())
            return siteNode.getCategory().getCategoryName().trim();
        else{
//            Log.e(TAG, "MCategory#getCategory() is " + siteNode.getCategory().getCategoryName());
            return "";
        }

    }

    public static String getProperCategoryNameFromCategory(MCategory category) {
        if(category == null){
//            Log.e(TAG, "MSiteNodeVm is null");
            return "";
        }

        if(category.getCategoryName()!= null && !category.getCategoryName().isEmpty())
            return category.getCategoryName().trim();
        else{
//            Log.e(TAG, "MCategory#categoryName is " + category.getCategoryName());
            return "";
        }
    }

    /**
     * If category is valid category find the proper icon using string compare. (bullshit >:( )
     *
     * todo: Need Clarification.
     * */
    public static int findProperIcon(String categoryName) {

        String[] eatAndDrink   = {"eat",        "drink",         "eat and drink"     };
        String[] entertainment = {"enter",      "entertainment", "tainment"          };
        String[] fashionRetail = {"fashion",    "retail",        "fashion & retail"  };
        String[] house         = {"house",      "flat"                               };
        String[] welbeing      = {"wellbeing",  "healt"                              };
        String[] automative    = {"automotive", "car",           "vehicle"           };
        String[] travel        = {"travel",     "traveller"                          };
        String[] estore        = {"estore",     "store"                              };


        if (categoryName == null || categoryName.isEmpty()) return R.drawable.ic_android;

        String in = categoryName.toLowerCase();

        if     (isContainAny(in, eatAndDrink))   return R.drawable.ic_email;
        else if(isContainAny(in, entertainment)) return R.drawable.ic_folder;
        else if(isContainAny(in, fashionRetail)) return R.drawable.ic_menu;
        else if(isContainAny(in, house))         return R.drawable.ic_location;
        else if(isContainAny(in, welbeing))      return R.drawable.ic_phone;
        else if(isContainAny(in, automative))    return R.drawable.ic_star_white;
        else if(isContainAny(in, travel))        return R.drawable.ic_location;
        else if(isContainAny(in, estore))        return R.drawable.icon_pin;
        else                                     return R.drawable.ic_android; // if none
    }

    private static boolean isContainAny(String in, String[] strArr) {
        for (String str : strArr){
            if(in.contains(str.toLowerCase())) return true;
        }
        return false;
    }

    public static ArrayList<MSiteNodeVm> getProperSiteNodeArray(NodesHelper nodes) throws Exception {
        if (nodes == null)                 throw new Exception("Nodes is null");
        if (nodes.get$values() == null )   throw new Exception("Nodes->$values is null");
        if (nodes.get$values().isEmpty() ) throw new Exception("Nodes->$values is empty");

        return oderSiteNotesAccordingOrderNumber(removeNotIncludedItems(nodes.get$values()));

    }

    /**
     * Whether this node should be displayed as part of the menu
     * */
    private static ArrayList<MSiteNodeVm> removeNotIncludedItems(ArrayList<MSiteNodeVm> list) {
        Iterator<MSiteNodeVm> itr = list.iterator();
        while (itr.hasNext()) {
            MSiteNodeVm nodeVm = itr.next();
            if (!nodeVm.isIncludeInMenu()) itr.remove();
        }
        return list;
    }

    /**
     * Order main menu items according to the incoming order number.
     * */
    private static ArrayList<MSiteNodeVm> oderSiteNotesAccordingOrderNumber(ArrayList<MSiteNodeVm> list) {
        Collections.sort(list, new Comparator<MSiteNodeVm>() {
            @Override
            public int compare(MSiteNodeVm l, MSiteNodeVm r) {
                Integer left  = l.getOrder();
                Integer right = r.getOrder();

                return left.compareTo(right);
            }
        });
        return list;
    }

    public static String getErrorMessage(MErrorHelper errors) throws Exception {
        if (errors == null)                 throw new Exception("errors is null");
        if (errors.getValues() == null)     throw new Exception("errors->$values is null");
        if (errors.getValues().isEmpty())   throw new Exception("errors->$values is empty");

        String msg = "";
        int i = 0;
        for (MError error : errors.getValues()){
            if (error.message != null) msg += "\nError " + i + ": " + error.message;
            i++;
        }
        if (msg.isEmpty()) throw new Exception("errors->$values->message all is empty");
        else               return msg;
    }


    public static ArrayList<MMerchant> getMerchandizeList(ArrayList<MSiteNodeVm> nodes) {
        ArrayList<MMerchant> list = new ArrayList<>();

        if (nodes == null) return list;

        for (MSiteNodeVm site : nodes){
            if (isMechantSizeValid(site)){
                for (MMerchant merchant : site.getCategory().getMerchantsHelper().getMerchants()){
                    if (!list.contains(merchant))
                        list.add(merchant);
                }
            }
        }
//        Log.d(TAG, " " + list.size() + " merchant address found");
        return list;
    }

    public static boolean isMechantSizeValid(MSiteNodeVm site) {
        return site.getCategory()                                       != null &&
                site.getCategory().getMerchantsHelper()                 != null &&
                site.getCategory().getMerchantsHelper().getMerchants()  != null &&
                site.getCategory().getMerchantsHelper().getMerchants().size() > 0 ;
    }

    public static boolean hasItemValidSubCategories(MSiteNodeVm item) {
        return  item                                                  != null &&
                item.getCategory()                                    != null &&
                item.getCategory().getChildren()                      != null &&
                item.getCategory().getChildren().get$values()         != null &&
                item.getCategory().getChildren().get$values().size()     >     0;
    }

    public static LatLng getLatLngOfThisMerchant(MMerchant merchant) {
        if (!hasMerchantiseValidAddress(merchant)) return null;

        MAddress address = merchant.getAddressHelper().get$values().get(0);

        if (address.getLatitude() == 0 || address.getLongtitude() == 0) return null;

        return new LatLng(address.getLatitude(), address.getLongtitude());
    }

    private static boolean hasMerchantiseValidAddress(MMerchant merchant) {
        return  merchant                                        != null &&
                merchant.getAddressHelper()                     != null &&
                merchant.getAddressHelper().get$values()        != null &&
                merchant.getAddressHelper().get$values().get(0) != null;
    }

    public static boolean hasCategoryValidStocks(MCategory category) {
        return  category                                        != null &&
                category.getStockItemsHelper()                  != null &&
                category.getStockItemsHelper().getStockItems()     != null &&
                category.getStockItemsHelper().getStockItems().size() > 0;
    }


    public static boolean hasValidThumbnailImageUrl(MStockItem stockItem) {
        return stockItem                                != null &&
                stockItem.getResources()                != null &&
                stockItem.getResources().get$values()   != null &&
                hasArrayValidUrl(stockItem.getResources().get$values());
    }

    private static boolean hasArrayValidUrl(ArrayList<MResourse> $values) {
        /**
         * if one of the $values has valid url return true
         * */
        for(MResourse resourse :$values){
            if(resourse.getUrl() != null && !resourse.getUrl().isEmpty())
                return true;
        }
        return false;
    }

    public static String getValidImageUrl(MStockItem stockItem) {
        for(MResourse resourse :stockItem.getResources().get$values()){
            if(resourse.getUrl() != null && !resourse.getUrl().isEmpty()) {
//                Log.d(TAG, "getValidImageUrl: " + Constants.Server.SERVER_URL + resourse.getUrl());
                return Constants.Server.SERVER_URL + resourse.getUrl();
            }
        }

        return Constants.Server.SERVER_URL;
    }

    public static ArrayList<MMerchant> getMerchandizeListFromStock(MStockItem stockItem) {
        ArrayList<MMerchant> list = new ArrayList<>();

        if(stockItem == null) return list;

        if(hasStochValidMerhandise(stockItem)){
            for (MMerchant merchant : stockItem.getMerchantsHelper().getMerchants()){
                list.add(merchant);
            }
        }

        return list;
    }

    private static boolean hasStochValidMerhandise(MStockItem stockItem) {
        return stockItem != null &&
                stockItem.getMerchantsHelper() != null &&
                stockItem.getMerchantsHelper().getMerchants() != null &&
                stockItem.getMerchantsHelper().getMerchants().size() > 0 ;

    }

    public static ArrayList<MCartItemInfo> getCartItemsInfo(MShoppingCartVM mShopingCart) {
        ArrayList<MCartItemInfo> infos = new ArrayList<>();

        if(mShopingCart == null || mShopingCart.getShoppingCartItemsHelper() == null)
            return infos;

        for(MShoppingCartItem item : mShopingCart.getShoppingCartItemsHelper().getItems()){
            MCartItemInfo itemInfo = new MCartItemInfo();
            itemInfo.setQuantity(item.getQuantity());
            itemInfo.setStockItemId(item.getStockItemId());
            infos.add(itemInfo);
        }

        return infos;
    }

    public static boolean isInMainCategory(int id){
        return  id == 21 || id == 25 || id == 34 ||
                id == 40 || id == 46 || id == 50 || id == 55 || id == 65

                ||

                id == 175 || id == 179 || id == 184 ||
                id == 190 || id == 283 || id == 199 || id == 203 || id == 207;
    }

    public static int getIcon(int id) {
        switch (id){
            case 21: return R.drawable.icon_eatanddrink;
            case 175: return R.drawable.icon_eatanddrink;

            case 25: return R.drawable.icon_entertainment;
            case 179: return R.drawable.icon_entertainment;

            case 34: return R.drawable.icon_fashionretail;
            case 184: return R.drawable.icon_fashionretail;

            case 40: return R.drawable.icon_house;
            case 190: return R.drawable.icon_house;

            case 46: return R.drawable.icon_wellbeing;
            case 283: return R.drawable.icon_wellbeing;

            case 50: return R.drawable.icon_automotive;
            case 199: return R.drawable.icon_automotive;

            case 55: return R.drawable.icon_travel;
            case 203: return R.drawable.icon_travel;

            case 65: return R.drawable.icon_estore;
            case 207: return R.drawable.icon_estore;
            default: return R.drawable.icon_loc;

        }
    }

    public static @DrawableRes int getSubCatIcon(int id) {
        switch (id){
            case 253: return R.drawable.icon_restaurantandcafes;
            case 218: return R.drawable.icon_groceries;
            case 191: return R.drawable.icon_liquor;

            case 273: return R.drawable.icon_tickets;
            case 176: return R.drawable.icon_amusement;
            case 287: return R.drawable.ic_2c_experiences;
            case 254: return R.drawable.icon_books;
            case 288: return R.drawable.icon_games;

            case 303: return R.drawable.icon_watch;
            case 185: return R.drawable.icon_sports;
            case 148: return R.drawable.icon_mens;
            case 149: return R.drawable.icon_womens;
            case 295: return R.drawable.icon_kids;

            case 46: return R.drawable.icon_office;
            case 291: return R.drawable.icon_homewares;
            case 209: return R.drawable.icon_services_2;
            case 29: return R.drawable.icon_tech;

            case 40: return R.drawable.icon_fitness;
            case 32: return R.drawable.icon_health;
            case 296: return R.drawable.icon_beauty;

            case 297: return R.drawable.icon_accessories;
            case 279: return R.drawable.icon_fuel;
            case 124: return R.drawable.icon_services;

            case 56: return R.drawable.icon_accomodation;
            case 134: return R.drawable.icon_airplane;
            case 261: return R.drawable.ic_7c_insurance;
            case 304: return R.drawable.icon_accessories;
            case 302: return R.drawable.icon_accomodation;

            default: return R.drawable.icon_gps;

        }
    }
}
