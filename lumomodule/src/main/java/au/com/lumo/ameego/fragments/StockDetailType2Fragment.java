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
import au.com.lumo.ameego.model.MShoppingCartVM;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.presenters.CardSelector;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by Zeki Guler on 28/07/15.
 */
public class StockDetailType2Fragment extends BaseFragment {

    public  static final String TAG            = StockDetailType1Fragment.class.getSimpleName();
    public  static final String STOCK_ITEM_KEY = "STOCK_ITEM_KEY_TYPE_2";


    private TextView  mStockName;
    private TextView  mStockDiscount;
    private ImageView mStockImage;
    private TextView  mStockDef;
    private TableRow  mPhysicalOptTr;    // hide if digital selected.
    private TextView  mDeliveryType;
    private TextView  mCardPrice;
    private TextView  mQuantity;
    private TextView  mTerms;
    private LinearLayout mTermsLayout;
    /*private TextView  mCardValueName;*/
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
    private CardSelector mCardSelector;

    public static StockDetailType2Fragment newInstance(MStockItem stockItem) {
        StockDetailType2Fragment fragment = new StockDetailType2Fragment();
        Bundle bundle   = new Bundle();

        bundle.putSerializable(STOCK_ITEM_KEY, stockItem);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mStockItem = (MStockItem)getArguments().getSerializable(STOCK_ITEM_KEY);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStockName       = (TextView) view.findViewById(R.id.stock_detail_name);
        mStockDiscount   = (TextView) view.findViewById(R.id.stock_detail_discount);
        mStockImage      = (ImageView) view.findViewById(R.id.stock_detail_img);
        mStockDef        = (TextView) view.findViewById(R.id.stock_detail_def);
        mDeliveryType    = (TextView) view.findViewById(R.id.delivery_type);
        mCardPrice       = (TextView) view.findViewById(R.id.card_price);
        mQuantity        = (TextView) view.findViewById(R.id.item_quantity);
        mTerms           = (TextView) view.findViewById(R.id.stock_terms);
        mTermsLayout     = (LinearLayout) view.findViewById(R.id.terms_container);
        /*mCardValueName   = (TextView) view.findViewById(R.id.card_value_title);*/

        mCardOptionTitle = (TextView) view.findViewById(R.id.card_option_title);
        mBulletText1     = (TextView) view.findViewById(R.id.bullet1);
        mBulletText2     = (TextView) view.findViewById(R.id.bullet2);
        mBulletText3     = (TextView) view.findViewById(R.id.bullet3);
        mBulletText4     = (TextView) view.findViewById(R.id.bullet4);

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

        view.findViewById(R.id.item_quantity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardSelector.cardCountClicked();
            }
        });

        if(mCardSelector == null)
            mCardSelector = new CardSelector(getActivity(), mStockItem, new CustomCrdSelecterInterface());

        updateViews();
    }

    private void updateViews() {
        mStockName    .setText(mStockItem.getName());
        mStockDef     .setText(Html.fromHtml(mStockItem.getDescriptionLong()));

        mStockDiscount.setVisibility(View.VISIBLE);

        if(mStockItem.getDiscountType() == Constants.DiscountType.TEXT){
            mStockDiscount.setText(mStockItem.getDiscountText());
        } else if (mStockItem.getDiscountType() == Constants.DiscountType.TOTAL){
            mStockDiscount.setText("$" + mStockItem.getDiscount());
        } else if (mStockItem.getDiscountType() == Constants.DiscountType.PERCENTAGE){
            mStockDiscount.setText(StringUtils.formatDoubleValue(mStockItem.getDiscount()) + "% OFF");
        } else {
            mStockDiscount.setVisibility(mStockItem.getDiscount() > 0 ? View.VISIBLE : View.GONE);
        }

        if (mStockItem.getCategoryPageImage() != null && !mStockItem.getCategoryPageImage().isEmpty()){
            Picasso.with(getActivity())
                    .load(Constants.Url.IMAGE_BASE + mStockItem.getCategoryPageImage())// get valid one
                    .into(mStockImage);
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
//        mCardValueName.setText(mStockItem.isDisplayNameAsCardType() ? "CARD TYPE:" : "CARD VALUE:");
        mCardPrice.setText(mStockItem.isDisplayNameAsCardType() ? "Select Card Type:" : "Select Card Value");

    }

    @Override
    protected String getTitle() {
        return mStockItem.getName();
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
        return R.layout.fragment_stock_detail_type2;
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
            mQuantity.setText(quantity == 0 ? getString(R.string.select_quantity):"Quantity: " + String.valueOf(quantity));
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
