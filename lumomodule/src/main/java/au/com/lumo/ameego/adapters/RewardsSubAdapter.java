package au.com.lumo.ameego.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.viewHolders.RewardsSubViewHolder;

/**
 * Created by appscoredev2 on 13/07/15.
 */
public class RewardsSubAdapter  extends RecyclerView.Adapter<RewardsSubViewHolder>{

    private ArrayList<MCategory> mData = new ArrayList<>();
    private IRewardSubAdapter mCallback;

    public void setIRewardSubAdapterCallback(IRewardSubAdapter callback){
        this.mCallback = callback;
    }

    public interface IRewardSubAdapter{
        void onCardClicked(View view, int position, RewardsSubViewHolder viewHolder);
    }

    @Override
    public RewardsSubViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_category_item, viewGroup, false);
        return new RewardsSubViewHolder(view, new RewardsSubViewHolder.IViewHolderClick() {
            @Override
            public void onCardClick(View view, int position, RewardsSubViewHolder viewHolder) {
                if(mCallback != null) mCallback.onCardClicked(view,position,viewHolder);
            }
        });
    }

    @Override
    public void onBindViewHolder(RewardsSubViewHolder holder, int position) {
        holder.initFromData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(ArrayList<MCategory>  data) {
        this.mData.clear();
        this.mData.addAll(data);
    }

    public boolean isEmpty() {return mData.isEmpty();}
}

