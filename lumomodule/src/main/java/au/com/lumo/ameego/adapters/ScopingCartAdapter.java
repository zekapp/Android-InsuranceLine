package au.com.lumo.ameego.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MShoppingCartItem;
import au.com.lumo.ameego.utils.RecyclerViewAdapter;
import au.com.lumo.ameego.viewHolders.ShoppingCartViewHolder;

/**
 * Created by Zeki Guler on 22/07/15.
 */
public class ScopingCartAdapter extends RecyclerViewAdapter<ShoppingCartViewHolder>{

    private Context                                 mContext;
    private ShoppingCartViewHolder.IViewHolderClick mI;

    private ArrayList<MShoppingCartItem>  mData = new ArrayList<>();

    public ScopingCartAdapter(Context context, ArrayList<MShoppingCartItem> items, ShoppingCartViewHolder.IViewHolderClick delegate) {
        super(context);
        mContext = context;
        mData.addAll(items);
        mI = delegate;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ShoppingCartViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_item, parent, false);
        return new ShoppingCartViewHolder(mContext, view, mI);
    }

    @Override
    public void onBindView(ShoppingCartViewHolder holder, int position) {
        holder.initFromData(mData.get(position));
    }


    public void removeItem(int position) {
        if (position >= mData.size()) return;

        mData.remove(position);
        notifyItemRemoved(position);
    }

    public boolean isEmpty() {
        return mData.isEmpty();

    }

    public void updateData(ArrayList<MShoppingCartItem> res) {
        mData.clear();
        mData.addAll(res);
        notifyDataSetChanged();
    }

}
