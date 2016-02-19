package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.MCartItemInfo;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.presenters.CardSelector;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by Zeki Guler on 27/07/15.
 */
public class StockDetailType3Fragment extends BaseFragment {

    public  static final String TAG            = StockDetailType3Fragment.class.getSimpleName();
    public  static final String STOCK_ITEM_KEY = "STOCK_ITEM_KEY_TYPE_3";
    public  static final String MERCHANDISE_KEY = "MERHANDISE_KEY_TYPE_3";


    private  TextView  mMerchantName;
    private  TextView  mMerchantDiscount;
    private  ImageView mMerchantImg;
    private  TextView  mMerchantDef;
    private  TextView  mStockName;
    private  TextView  mStockDiscount;
    private  ImageView mStockImage;
    private  TextView  mStockDef;
    private  TextView  mDeliveryType;
    private  TextView  mCardPrice;
    private  TextView  mQuantity;
    private  TextView  mStockClaim;
    private  LinearLayout mClaimLiLa;
    private  TextView      mTerms;
    private  LinearLayout  mTermsLayout;
    /*private  TextView  mCardValueName;*/

/*//    @Bind(R.id.digital_option)      */    TableRow  mPhysicalOptTr; // hide if digital selected.
/**/
    /***/
     /** Karl added*/
     /**/
    private TextView mCardOptionTitle;
    private TextView mBulletText1;
    private TextView mBulletText2;
    private TextView mBulletText3;
    private TextView mBulletText4;

    private MStockItem mStockItem;
    private MMerchant  mMerchant;
    private CardSelector mCardSelector;

    public static StockDetailType3Fragment newInstance(MMerchant merchant, MStockItem stockItem) {
        StockDetailType3Fragment fragment = new StockDetailType3Fragment();
        Bundle bundle   = new Bundle();

        bundle.putSerializable(STOCK_ITEM_KEY, stockItem);
        bundle.putSerializable(MERCHANDISE_KEY, merchant);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mStockItem = (MStockItem)getArguments().getSerializable(STOCK_ITEM_KEY);
            mMerchant  = (MMerchant )getArguments().getSerializable(MERCHANDISE_KEY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMerchantName       = (TextView) view.findViewById(R.id.merchant_name);
        mMerchantDiscount   = (TextView) view.findViewById(R.id.merchant_discount);
        mMerchantImg        = (ImageView) view.findViewById(R.id.merchant_img);
        mMerchantDef        = (TextView) view.findViewById(R.id.merchant_def);
        mStockName          = (TextView) view.findViewById(R.id.stock_detail_name);
        mStockDiscount      = (TextView) view.findViewById(R.id.stock_detail_discount);
        mStockImage         = (ImageView) view.findViewById(R.id.stock_detail_img);
        mStockDef           = (TextView) view.findViewById(R.id.stock_detail_def);
        mDeliveryType       = (TextView) view.findViewById(R.id.delivery_type);
        mCardPrice          = (TextView) view.findViewById(R.id.card_price);
        mQuantity           = (TextView) view.findViewById(R.id.iten_quantity);
        mStockClaim         = (TextView) view.findViewById(R.id.stock_claim);
        mClaimLiLa          = (LinearLayout) view.findViewById(R.id.claim_layout);
        mTerms              = (TextView) view.findViewById(R.id.stock_terms);
        mTermsLayout        = (LinearLayout) view.findViewById(R.id.terms_container);
        /*mCardValueName      = (TextView) view.findViewById(R.id.card_value_title);*/

        mCardOptionTitle    = (TextView) view.findViewById(R.id.card_option_title);
        mBulletText1        = (TextView) view.findViewById(R.id.bullet1);
        mBulletText2        = (TextView) view.findViewById(R.id.bullet2);
        mBulletText3        = (TextView) view.findViewById(R.id.bullet3);
        mBulletText4        = (TextView) view.findViewById(R.id.bullet4);

        view.findViewById(R.id.delivery_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryTypeClicked();
            }
        });
        view.findViewById(R.id.card_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardPriceClicked();
            }
        });
        view.findViewById(R.id.stock_detail_increase_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemIncreased();
            }
        });
        view.findViewById(R.id.stock_detail_decrease_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDecreased();
            }
        });
        view.findViewById(R.id.stock_detail_add_to_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        if(mCardSelector == null)
            mCardSelector = new CardSelector(getActivity(), mStockItem, new CustomCrdSelecterInterface());
        updateViews();
    }

    @Override
    protected String getTitle() {
        return mMerchant.getName();
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_stock_detail_type3;
    }

    private void updateViews() {

        //Stock Item Related updates
        mStockName    .setText(mStockItem.getName());
        mStockDef     .setText(Html.fromHtml(mStockItem.getDescriptionLong() == null ? " " : mStockItem.getDescriptionLong()));

        if(mStockItem.getRedemptionButtonText() == null){
            mClaimLiLa.setVisibility(View.GONE);
        }else{
            mClaimLiLa.setVisibility(View.VISIBLE);
            mStockClaim.setText(Html.fromHtml(mStockItem.getRedemptionButtonText()));
        }

        mStockDiscount.setVisibility(View.VISIBLE);

        if(mStockItem.getDiscountType() == Constants.DiscountType.TEXT){
            mStockDiscount.setText(mStockItem.getDiscountText());
        } else if (mStockItem.getDiscountType() == Constants.DiscountType.TOTAL){
            mStockDiscount.setText("$" + mStockItem.getDiscount());
        } else if (mStockItem.getDiscountType() == Constants.DiscountType.PERCENTAGE){
            mStockDiscount.setText((int)mStockItem.getDiscount() + "% OFF");
        } else {
            mStockDiscount.setVisibility(mStockItem.getDiscount() > 0 ? View.VISIBLE : View.GONE);
        }


        if (mStockItem.getCategoryPageImage() != null && !mStockItem.getCategoryPageImage().isEmpty()){
            Picasso.with(getActivity())
                    .load(Constants.Url.IMAGE_BASE + mStockItem.getCategoryPageImage())// get valid one
                    .into(mStockImage);
        }


        //Merchandise Related Updates
        mMerchantName    .setText(mMerchant.getName());

        mMerchantDiscount.setVisibility(View.VISIBLE);

        if(mMerchant.getDiscountType() == Constants.DiscountType.TEXT){
            mMerchantDiscount.setText(mMerchant.getDiscountText());
        } else if (mMerchant.getDiscountType() == Constants.DiscountType.TOTAL){
            mMerchantDiscount.setText("$" + mMerchant.getDiscount());
        } else if (mMerchant.getDiscountType() == Constants.DiscountType.PERCENTAGE){
            mMerchantDiscount.setText(StringUtils.formatDoubleValue(mMerchant.getDiscount()) + "% OFF");
        } else {
            mMerchantDiscount.setVisibility(mMerchant.getDiscount() > 0 ? View.VISIBLE : View.GONE);
        }

        mMerchantDef.setText(Html.fromHtml(mMerchant.getLongDescription() == null ? "" : mMerchant.getLongDescription()));

        if (mMerchant.getCategoryPageImage() != null && !mMerchant.getCategoryPageImage().isEmpty()){
            Picasso.with(getActivity())
                    .load(Constants.Url.IMAGE_BASE + mMerchant.getCategoryPageImage())// get valid one
                    .into(mMerchantImg);
        }

        boolean isTermShown = mStockItem.getTerms() != null && !mStockItem.getTerms().isEmpty();
        mTermsLayout.setVisibility(isTermShown ? View.VISIBLE : View.GONE);
        if(isTermShown) {
            /**
             * Karl added, text view does not support <li><ul> tag in html
             * delete <ul></ul>, replace <li> with  &#8226; </li> with <br/>
             */
            String s = mStockItem.getTerms().replace("<ul>","");
            s = s.replace("</ul>","");
            s = s.replace("<li>","&#8226; ");
            s = s.replace("</li>","<br/><br/>");

            mTerms.setText(Html.fromHtml(s));
        }

        /*mCardValueName.setText(mStockItem.isDisplayNameAsCardType() ? "CARD TYPE:" : "CARD VALUE:");*/
        mCardPrice.setText(mStockItem.isDisplayNameAsCardType() ? "Select Card Type:" : "Select Card Value");
    }

    /*@OnClick(R.id.delivery_type)*/
    void deliveryTypeClicked(){
        mCardSelector.deliveryTypeClicked();
    }

    /*@OnClick(R.id.card_price)*/
    void cardPriceClicked(){
        mCardSelector.priceClicked();
    }

    /*@OnClick(R.id.stock_detail_increase_item)*/
    void itemIncreased(){
        mCardSelector.increaseQuantityByOne();
    }

    /*@OnClick(R.id.stock_detail_decrease_item)*/
    void itemDecreased(){
        mCardSelector.decreaseQuantityByOne();
    }

    /*@OnClick(R.id.stock_detail_add_to_card)*/
    void addToCart(){
        mCardSelector.addToCart();
    }

    private class CustomCrdSelecterInterface implements CardSelector.ICardSelectorResponse {
        @Override
        public void itemSelected(MCartItemInfo selectedCard, int deliveryType, String price) {
            mDeliveryType.setText(getResources().getStringArray(R.array.card_delivery_type)[deliveryType]);
            mCardPrice.setText(String.valueOf(price));

//            if(deliveryType == Constants.FulfilmentType.DIGITAL)
//                mPhysicalOptTr.setVisibility(View.GONE);
//            else
//                mPhysicalOptTr.setVisibility(View.VISIBLE);

            setDeliveryTypeInstruction(deliveryType);
        }

        @Override
        public void cardAddedToStore(MShoppingCartVM shoppingCartOverall) {
            mIActivity.saveCartToDb(shoppingCartOverall);
            Toast.makeText(getActivity(), "Item added to your cart.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void quantityChanged(int quantity) {
            mQuantity.setText("QUANTITY: " + String.valueOf(quantity));
        }
    }

    /**
     * Karl added
     */
    private void setDeliveryTypeInstruction(int deliveryType){
        if(deliveryType == Constants.FulfilmentType.DIGITAL){
            mCardOptionTitle.setText(getResources().getString(R.string.card_options_digital));
            mBulletText1.setText(getResources().getString(R.string.options_bullet_digital_1));
            mBulletText2.setText(getResources().getString(R.string.options_bullet_digital_2));
            mBulletText3.setText(getResources().getString(R.string.options_bullet_digital_3));
            mBulletText4.setText(getResources().getString(R.string.options_bullet_digital_4));
        } else {
            mCardOptionTitle.setText(getResources().getString(R.string.card_options_physical));
            mBulletText1.setText(getResources().getString(R.string.options_bullet_physical_1));
            mBulletText2.setText(getResources().getString(R.string.options_bullet_physical_2));
            mBulletText3.setText(getResources().getString(R.string.options_bullet_physical_3));
            mBulletText4.setText(getResources().getString(R.string.options_bullet_physical_4));
        }
    }
}
