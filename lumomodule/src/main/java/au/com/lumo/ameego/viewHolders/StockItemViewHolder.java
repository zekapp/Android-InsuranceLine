package au.com.lumo.ameego.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.LumoSpecificUtils;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by Zeki Guler on 16/07/15.
 */
public class StockItemViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = StockItemViewHolder.class.getSimpleName();
    private final Context mContext;

    /*@Bind(R.id.stock_item_tumbnail)   */ImageView thumbnail;
    /*@Bind(R.id.stock_item_percentage) */TextView  percentage;
    /*@Bind(R.id.stock_item_merchname)  */TextView  thumbName;

    private IViewHolderClick  listener;
    private Object                 mT;

    public StockItemViewHolder(Context context, View itemView, final IViewHolderClick listener) {
        super(itemView);
        mContext = context;
        this.listener = listener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener !=null) listener.onItemClicked(v, getLayoutPosition(), StockItemViewHolder.this);
            }
        });
    }

    public void initFromData(Object t)  {
        mT = t;

        if      (t instanceof MStockItem) initUsingStockItem((MStockItem) t);
        else if (t instanceof MMerchant)  initUsingMerchant ((MMerchant ) t);
//        else    Log.e(TAG, "Error: This object can not found: " + (t != null ? t.toString() : "Object is null"));

    }

    private void initUsingMerchant(MMerchant merchant) {
        if (merchant.getCategoryPageImage() != null && !merchant.getCategoryPageImage().isEmpty()){
            String url = (Constants.Url.IMAGE_BASE + merchant.getCategoryPageImage()).trim();
//            Log.d(TAG, "Merchant img url: " + url);
            Picasso.with(mContext)
                    .load(url)// get valid one
                    .into(thumbnail);
        }else
//            Log.e(TAG, "Merchant get getCategoryPageImage null or empty");


        percentage.setVisibility(View.VISIBLE);

        if(merchant.getDiscountType() == Constants.DiscountType.TEXT){
            percentage.setText(merchant.getDiscountText());
        } else if (merchant.getDiscountType() == Constants.DiscountType.TOTAL){
            percentage.setText("$" + merchant.getDiscount());
        } else if (merchant.getDiscountType() == Constants.DiscountType.PERCENTAGE){
            percentage.setText(StringUtils.formatDoubleValue(merchant.getDiscount()) + "% OFF");
        } else {
            percentage.setVisibility(merchant.getDiscount() > 0 ? View.VISIBLE : View.GONE);
        }

        thumbName.setText(merchant.getName());
    }

    private void initUsingStockItem(MStockItem stockItem) {
        if(LumoSpecificUtils.hasValidThumbnailImageUrl(stockItem)){
//            Log.d(TAG, "StockItem img url: " + LumoSpecificUtils.getValidImageUrl(stockItem));
            Picasso.with(mContext)
                    .load(LumoSpecificUtils.getValidImageUrl(stockItem))// get valid one
                    .into(thumbnail);
        }else
//            Log.e(TAG, "StockItem get hasValidThumbnailImageUrl not valid Image Url");

        percentage.setVisibility(View.VISIBLE);

        if(stockItem.getDiscountType() == Constants.DiscountType.TEXT){
            percentage.setText(stockItem.getDiscountText());
        } else if (stockItem.getDiscountType() == Constants.DiscountType.TOTAL){
            percentage.setText("$" + stockItem.getDiscount());
        } else if (stockItem.getDiscountType() == Constants.DiscountType.PERCENTAGE){
            percentage.setText(StringUtils.formatDoubleValue(stockItem.getDiscount()) + "% OFF");
        } else {
            percentage.setVisibility(stockItem.getDiscount() > 0 ? View.VISIBLE : View.GONE);
        }

        thumbName.setText(stockItem.getName());

    }

    public Object getItem(){
        return mT;
    }

    public interface IViewHolderClick {
        void onItemClicked(View view, int position, StockItemViewHolder viewHolder);
    }
}
