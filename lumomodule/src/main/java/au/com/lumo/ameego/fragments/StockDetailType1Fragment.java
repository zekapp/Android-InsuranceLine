package au.com.lumo.ameego.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.fragments.basefragments.BaseFragment;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by Zeki Guler on 27/07/15.
 */
public class StockDetailType1Fragment extends BaseFragment{

    public  static final String TAG            = StockDetailType1Fragment.class.getSimpleName();
    public  static final String STOCK_ITEM_KEY = "STOCK_ITEM_KEY_TYPE_1";


    private TextView  mStockName;
    private TextView  mStockDiscount;
    private ImageView mStockImage;
    private TextView  mStockDef;
    private TextView  mStockClaim;
    private TextView  mStockWeb;
    private TextView  mTerms;
    private LinearLayout mTermsLayout;


    private MStockItem   mStockItem;

    public static StockDetailType1Fragment newInstance(MStockItem stockItem) {
        StockDetailType1Fragment fragment = new StockDetailType1Fragment();
        Bundle                   bundle   = new Bundle();

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
        mStockName     = (TextView) view.findViewById(R.id.stock_detail_name);
        mStockDiscount = (TextView) view.findViewById(R.id.stock_detail_discount);
        mStockImage    = (ImageView) view.findViewById(R.id.stock_detail_img);
        mStockDef      = (TextView) view.findViewById(R.id.stock_detail_def);
        mStockClaim    = (TextView) view.findViewById(R.id.stock_detail_claim);
        mStockWeb      = (TextView) view.findViewById(R.id.stock_detail_visit_web);
        mTerms         = (TextView) view.findViewById(R.id.stock_terms);
        mTermsLayout   = (LinearLayout) view.findViewById(R.id.terms_container);

        view.findViewById(R.id.stock_detail_visit_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitWebPage();
            }
        });
        view.findViewById(R.id.stock_detail_view_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocation();
            }
        });

        updateViews();
    }

    private void updateViews() {
        mStockName    .setText(mStockItem.getName());
        mStockWeb     .setText("Visit");
        mStockDef     .setText(Html.fromHtml(mStockItem.getDescriptionLong()));
        mStockClaim   .setText(Html.fromHtml(mStockItem.getRedemptionInstructions()));


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
        return R.layout.fragment_stock_detail_type1;
    }
    /*@OnClick(R.id.stock_detail_visit_web)*/
    void visitWebPage(){
        mIActivity.replaceWith(WebViewFragment.newInstance(mStockItem.getRedemptionURL(), ""));
    }

    /*@OnClick(R.id.stock_detail_view_location)*/
    void viewLocation(){
        mIActivity.replaceWith(MerchandisePlaceFragment.newInstance(mStockItem));
    }

}
