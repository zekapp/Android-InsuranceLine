package au.com.lumo.ameego.utils;

/**
 * Created by appscoredev2 on 7/07/15.
 */
public class Constants {

    public static final int    PASSWORD_LENGHT                = 6;
    public static final String CLIENT_ID                      = "aae7cbf8-99e0-4577-931d-785aa371e3e2";//"31159E2C-1DA9-4F94-B279-A3259A74F127";
    public static final String LOGIN_USER_NAME                = /*"t1member"*/ /*"lumoappuser2"*/ /*"lumoappuser"*/ /*"bennys"*/ "";
    public static final String LOGIN_PASSWORD                 = /*"3L3phant!"*/ /*"AF34Rt"*/ "";
    public static final String SUPPORT_PHONE_NUMBER           = "1300887787";
    public static final String SUPPORT_EMAIL                  = "info@lifestylerewards.com.au";

    public static class Url{
        public static final String NEW_HERE_USER_URL          = "https://myaccount.lumoenergy.com.au";
        public static final String FORGOT_PASSWORD            = "https://myaccount.lumoenergy.com.au/login.aspx?ReturnUrl=%2f";
        public static final String TWITTER_URL                = "https://twitter.com/lumo_energy";
        public static final String PRIVACY_POLICY             = Server.SERVER_URL + "Info/Privacy";
        public static final String TERMS_CONDITION            = Server.SERVER_URL + "Redirect/Go?target=TERMS&userKey=%s&appID=%s&args=m";
        public static final String MY_ACCOUNT                 = "https://myaccount.lumoenergy.com.au";
        public static final String INSURANCE_LINE_WEB         = "https://www.insuranceline.com.au";
        public static final String EMPTY_CASE_URL             = "https://lumorewards.com.au/";
        public static final String IMAGE_BASE                 = "https://lumorewards.com.au/Default/Resources/Image/";
    }

    public static class Config{
        public static final boolean DEVELOPER_MODE            = false;
    }

    public static class Extra {
        public static final String IS_LAUNCHER                = "au.com.lumo.is_launcher";
    }

    public static class Server {
        public static final String SERVER_URL                 = "http://stagingapi.lifestylerewards.com.au/";
//        public static final String SERVER_URL                 = "https://api.lifestylerewards.com.au/";
        public static final String LOGIN                      = SERVER_URL + "auth";
        public static final String SITE_NODE_VM               = SERVER_URL + "api/v1/site";
        public static final String GET_STOCK                  = SERVER_URL + "api/v1/stock?stockItemId=%d";
        public static final String GET_ASSOCIATED_STOCK       = SERVER_URL + "api/v1/StockByMerchant?merchantId=%d&stockItemId=%d";
        public static final String GET_SUBCATEGORIES          = SERVER_URL + "api/v1/category?id=%d";
        public static final String GET_SHOPPING_CART          = SERVER_URL + "api/v1/ShoppingCart";
        public static final String UPDATE_CART                = SERVER_URL + "api/v1/shoppingCartItems";
        public static final String GET_NEAR_ME                = SERVER_URL + "api/v1/merchant";
        public static final String PAY                        = SERVER_URL + "api/v1/Pay";
        public static final String GET_QUESTIONNAIRE          = SERVER_URL + "api/v1/Questions?questionnaireId=%d";
        public static final String POST_QUESTIONNAIRE         = SERVER_URL + "api/v1/Questions";
        public static final String LOGOUT                     = SERVER_URL + "api/v1/Logout";
    }

    public static class RequestTags {
        public static final String LOG_IN_TAG                 = "LOG_IN_TAG";
        public static final String SITE_NODE_VM_TAG           = "SITE_NODE_VM_TAG";
        public static final String GET_STOCK_TAG              = "GET_STOCK_TAG";
        public static final String GET_ASSOCIATED_STOCK_TAG   = "GET_ASSOCIATED_STOCK_TAG";
        public static final String GET_SUBCATEGORIES_TAG      = "GET_SUBCATEGORIES_TAG";
        public static final String GET_SHOPPING_CART_TAG      = "GET_SHOPPING_CART_TAG";
        public static final String GET_NEAR_ME_TAG            = "GET_NEAR_ME_TAG";
        public static final String UPDATE_CART_TAG            = "UPDATE_CART_TAG";
        public static final String PAY_TAG                    = "PAY_TAG";
        public static final String GET_QUESTIONNAIRE_TAG      = "GET_QUESTIONNAIRE_TAG";
        public static final String POST_QUESTIONNAIRE_TAG     = "POST_QUESTIONNAIRE_TAG";
        public static final String LOGOUT_TAG                 = "LOGOUT_TAG";
    }

    public static class LinkBehaviour{
        public static final int NONE                          = 0;
        public static final int SAME_TAB                      = 1;
        public static final int NEW_WINDOW                    = 2;
        public static final int IFRAME                        = 3;
        public static final int NEW_WINDOW_API_CALL           = 4;
        public static final int IFRAME_API_CALL               = 5;
    }

    public static class ProductType {
        public static final int PHYSICAL                      = 0;
        public static final int DIGITAL                       = 1;
        public static final int MERCHANT_DEAL                 = 2;
        public static final int PRODUCT_FEED                  = 3;
        public static final int ONLINE_PRODUCT                = 4;
        public static final int VOUCHER                       = 5;
        public static final int GROUP                         = 6;
    }

    public static class FulfilmentType {
        public static final int DIGITAL                       = 0; // Sent via email
        public static final int PHYSICAL                      = 1; // Sent via the physical fulfilment process
        public static final int EXTERNAL_API                  = 2; // External API Call
        public static final int CUSTOM_PAGE                   = 3; // The system displays an external page of information on how to access the detal
        public static final int DISPLAY                       = 4; // The system displays an internal page of information on how to access the detal
        public static final int REDEMPTION_CODE_LIST          = 5; // The system displays a unique value that once viewed is assigned to that user
        public static final int NOT_SALEABLE                  = 6; // Not a saleable item
    }

    public static class DiscountType{
        /**
         * type of discount applied to the denomintation (percentage of denomination v absolute amount deducted from denomintation)
         * */
        public static final int PERCENTAGE                    = 0; // The amount of the discount is a percentage of the Denomination
        public static final int TOTAL                         = 1; // The amount of the discount is in dollars and cents and should be deducted from the Denomination
        public static final int TEXT                          = 2; // The amount of discount is expressed as a statement and should not be calculated
    }

    public static class ShoppingCartStatus{
        public static final int NEW                           = 0;
        public static final int BUILDING                      = 1;
        public static final int PAID                          = 2;
        public static final int FAILED_PAYMENT                = 3;
        public static final int DESPATCHED                    = 4;
        public static final int CANCELLED                     = 5;
        public static final int QUEUED                        = 6;
        public static final int CLOSED                        = 7;
    }

    public static class CardType{
        /** Required Range: inclusive between 1 and 4 */
        public static final int VISA                          = 1;
        public static final int MASTER_CARD                   = 2;
        public static final int AMERICAN_EXPRESS              = 4;
    }

    public static class PaymentType{
        public static final int CREDIT_CARD                   = 0;
        public static final int PAY_PAL                       = 1;
    }
    public static class AddressType{
        public static final int HOME                          = 0;
        public static final int BUSSINESS                     = 1;
        public static final int BRANCH                        = 2;
        public static final int DELIVERY                      = 3;
        public static final int OTHER                         = 4;
    }

    public static class QuestionType{
        /** The type of the question */
        public static final int SIMPLE                        = 0;
        public static final int SINGLEPUNCH                   = 1;
        public static final int MULTIPUNCH                    = 2;
        public static final int GRID                          = 3;
        public static final int INSTRUCTION                   = 4;
    }

    public static class QuestionDataType{
        /** The data type expected as an answer to the question */
        public static final int TEXT                          = 0;
        public static final int INTEGER                       = 1;
        public static final int DOUBLE                        = 2;
        public static final int DATETIME                      = 3;
        public static final int BOOLEAN                       = 4;
        public static final int LIST                          = 5;
    }


    public static String getRandTestPicture() {
        return IMAGES[(int)(Math.random()*IMAGES.length) ];
    }

    public static final String[] IMAGES = new String[] {
            // Light images
//            "http://simpozia.com/pages/images/stories/windows-icon.png",
//            "http://radiotray.sourceforge.net/radio.png",
//            "http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
//            "http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
//            "http://weloveicons.s3.amazonaws.com/icons/100929_applications.png",
//            "http://www.idyllicmusic.com/index_files/get_apple-iphone.png",
//            "http://www.frenchrevolutionfood.com/wp-content/uploads/2009/04/Twitter-Bird.png",
    };

    public static final class DrawerItems{
        public static final int DRAWER_REWARD      = 0;
        public static final int DRAWER_NEWS        = 1;
        public static final int DRAWER_NEAR_ME     = 2;
        public static final int DRAWER_CART        = 3;
        public static final int DRAWER_SETTINGS    = 4;
    }
}
