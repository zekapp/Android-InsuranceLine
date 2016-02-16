package au.com.lumo.ameego.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MMerchant;
import au.com.lumo.ameego.model.MStockItem;
import au.com.lumo.ameego.utils.Constants;
import au.com.lumo.ameego.viewHolders.StockItemViewHolder;

/**
 * Created by Zeki Guler on 16/07/15.
 */
public class StockAdapter extends RecyclerView.Adapter<StockItemViewHolder> {

    private final Context mContext;
    private ArrayList<Object> mData = new ArrayList<>();
    private ArrayList<Object> mDataOrg = new ArrayList<>();
    private IStockListMessage     mI;

    public void filterHighToLow() {

        freshTheData();

        Collections.sort(mData, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {

                Double l = 0.0, r = 0.0;

                if (lhs instanceof MStockItem)     l = ((MStockItem)lhs).getDiscount();
                else if (lhs instanceof MMerchant) l = ((MMerchant)lhs).getDiscount();

                if (rhs instanceof MStockItem)     r = ((MStockItem)rhs).getDiscount();
                else if (rhs instanceof MMerchant) r = ((MMerchant)rhs).getDiscount();

                return r.compareTo(l);
            }
        });

        notifyDataSetChanged();
    }

    public void filterLowToHigh() {

        freshTheData();

        Collections.sort(mData, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {

                Double l = 0.0, r = 0.0;

                if (lhs instanceof MStockItem)     l = ((MStockItem)lhs).getDiscount();
                else if (lhs instanceof MMerchant) l = ((MMerchant)lhs).getDiscount();

                if (rhs instanceof MStockItem)     r = ((MStockItem)rhs).getDiscount();
                else if (rhs instanceof MMerchant) r = ((MMerchant)rhs).getDiscount();

                return l.compareTo(r);
            }
        });

        notifyDataSetChanged();
    }

    public void filterAtoZ() {

        freshTheData();

        Collections.sort(mData, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String r = "", l = "";

                if (lhs instanceof MStockItem) l = ((MStockItem) lhs).getName().toLowerCase();
                else if (lhs instanceof MMerchant) l = ((MMerchant) lhs).getName().toLowerCase();

                if (rhs instanceof MStockItem) r = ((MStockItem) rhs).getName().toLowerCase();
                else if (rhs instanceof MMerchant) r = ((MMerchant) rhs).getName().toLowerCase();

                return l.compareTo(r);
            }
        });

        notifyDataSetChanged();
    }

    private void freshTheData() {
        //clear data and get original
        mData.clear();
        mData.addAll(mDataOrg);
    }

    public void filterZtoA() {

        freshTheData();

        Collections.sort(mData, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String r = "", l = "";

                if (lhs instanceof MStockItem) l = ((MStockItem) lhs).getName().toLowerCase();
                else if (lhs instanceof MMerchant) l = ((MMerchant) lhs).getName().toLowerCase();

                if (rhs instanceof MStockItem) r = ((MStockItem) rhs).getName().toLowerCase();
                else if (rhs instanceof MMerchant) r = ((MMerchant) rhs).getName().toLowerCase();

                return r.compareTo(l);
            }
        });

        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mData.isEmpty();
    }

    public void showGiftCardsOnly() {
        ArrayList<Object> temp = new ArrayList<>();

        freshTheData();

        for (Object obj : mData){
            if(obj instanceof MStockItem){
                MStockItem item = (MStockItem)obj;

                if(item.getFulfilmentType() == Constants.FulfilmentType.DISPLAY)
                    continue;

                temp.add(obj);
            }
        }

        mData.clear();
        mData.addAll(temp);

        notifyDataSetChanged();

    }

    public void showDiscCodesOnly() {
        ArrayList<Object> temp = new ArrayList<>();

        freshTheData();

        for (Object obj : mData){
            if(obj instanceof MStockItem){
                MStockItem item = (MStockItem)obj;

                if(item.getFulfilmentType() == Constants.FulfilmentType.DISPLAY)
                    temp.add(obj);
            }

        }

        mData.clear();
        mData.addAll(temp);

        notifyDataSetChanged();
    }

    public void showNearMe(final Location currentLocation, float howFar) {

        ArrayList<Object> tempMerch = new ArrayList<>();
        ArrayList<Object> tempStock = new ArrayList<>();

        freshTheData();

        Location merch = new Location("");
        float distance;
        for (Object obj : mData){
            if(obj instanceof MMerchant){
                merch  = ((MMerchant) obj).getLocation();
                distance = currentLocation.distanceTo(merch);

                if(distance < howFar)
                    tempMerch.add(obj);
            }
            else
                tempStock.add(obj);
        }

        // sort them according to the current location.
        Collections.sort(tempMerch, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                Location r = new Location(""), l = new Location("");

                if (lhs instanceof MMerchant) l = ((MMerchant) lhs).getLocation();

                if (rhs instanceof MMerchant) r = ((MMerchant) rhs).getLocation();

                Float dis1 = currentLocation.distanceTo(l);
                Float dis2 = currentLocation.distanceTo(r);

                return dis1.compareTo(dis2);
            }
        });

        mData.clear();

        mData.addAll(tempMerch);
        mData.addAll(tempStock);

        notifyDataSetChanged();

    }

    public void showAll() {
        freshTheData();
        notifyDataSetChanged();
    }

    /**
     * Communicate with activity or fragment.
     * */
    public interface IStockListMessage{
        /**
         * To get which item clicked use view.getItemId() and improve the logic
         * @param view       clicked item view.
         * @param position   layout position
         * @param viewHolder view holder
         * */
        void onItemClicked(View view, int position, StockItemViewHolder viewHolder);
    }

    public StockAdapter(Context context, ArrayList<Object> items, IStockListMessage callback) {
        mContext = context;
        mI = callback;
        mData.clear();mDataOrg.clear();
        mData.addAll(items);
        mDataOrg.addAll(items);
    }

    @Override
    public StockItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        return new StockItemViewHolder(mContext, view, new CustomOnClickListener());
    }

    @Override
    public void onBindViewHolder(StockItemViewHolder holder, int position) {
        holder.initFromData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class CustomOnClickListener implements StockItemViewHolder.IViewHolderClick {
        @Override
        public void onItemClicked(View view, int position, StockItemViewHolder viewHolder) {
            if(mI != null) mI.onItemClicked(view, position, viewHolder);
        }
    }
}
