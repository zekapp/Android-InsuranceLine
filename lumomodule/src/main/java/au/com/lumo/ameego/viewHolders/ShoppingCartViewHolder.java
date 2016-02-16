package au.com.lumo.ameego.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MShoppingCartItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.utils.StringUtils;

/**
 * Created by Zeki Guler on 22/07/15.
 */
public class ShoppingCartViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = ShoppingCartViewHolder.class.getSimpleName();

    /*@Bind(R.id.shop_cart_item_name)     */TextView  itemNameTextView;
    /*@Bind(R.id.shop_cart_item_detail)   */TextView  itemDetailTextView;
    /*@Bind(R.id.shop_cart_item_price)    */TextView  itemPriceTextView;
    /*@Bind(R.id.shop_cart_item_quantity) */TextView  itemQuantityTextView;
    /*@Bind(R.id.shop_cart_card_img)      */ImageView mCardImgView;

    private Context           mContext;
    private IViewHolderClick  mListener;
    private MShoppingCartItem mShoppingCart;

    public ShoppingCartViewHolder(Context context, View itemView, final IViewHolderClick listener) {
        super(itemView);
        mContext = context;

        mListener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener !=null) mListener.onItemClicked(v, getLayoutPosition(), ShoppingCartViewHolder.this);
            }
        });
    }

    public void initFromData(MShoppingCartItem shoppingCart) {
        mShoppingCart = shoppingCart;

        String thumbnailImage = Constants.Server.SERVER_URL + shoppingCart.getThumbnailImage();
        if(StringUtils.isUrlValid(thumbnailImage)){
            Picasso.with(mContext)
                    .load(thumbnailImage)
                    .into(mCardImgView);
        }

        itemNameTextView    .setText(String.valueOf(shoppingCart.getItemName()));
        itemPriceTextView   .setText("$"+String.valueOf(String.format("%.2f", shoppingCart.getPrice())));
        itemDetailTextView  .setText(String.valueOf(shoppingCart.getFulfilmentType() == Constants.FulfilmentType.DIGITAL ? "Delivered Via Email" : "Delivered To Address"));
        itemQuantityTextView.setText(String.valueOf(shoppingCart.getQuantity()));
    }

    public MShoppingCartItem getItem(){
        return mShoppingCart;
    }

    /*@OnClick(R.id.shop_cart_cancel)*/
    void onCartRemovedClicked(View view){
        if(mListener !=null) mListener.onRemoveItemClicked(view, getLayoutPosition(), ShoppingCartViewHolder.this);
    }

    /*@OnClick(R.id.shop_cart_item_increase)*/
    void onCartIncreaseClicked(View view){
        if(mListener !=null) mListener.onIncItemClicked(view, getLayoutPosition(), ShoppingCartViewHolder.this);
    }

    /*@OnClick(R.id.shop_cart_item_decrease)*/
    void onCartDecreaseClicked(View view){
        if(mListener !=null) mListener.onDecItemClicked(view, getLayoutPosition(), ShoppingCartViewHolder.this);
    }

    public interface IViewHolderClick {
        void onItemClicked       (View view, int position, ShoppingCartViewHolder viewHolder);
        void onRemoveItemClicked (View view, int position, ShoppingCartViewHolder viewHolder);
        void onIncItemClicked    (View view, int position, ShoppingCartViewHolder viewHolder);
        void onDecItemClicked    (View view, int position, ShoppingCartViewHolder viewHolder);
    }
}
