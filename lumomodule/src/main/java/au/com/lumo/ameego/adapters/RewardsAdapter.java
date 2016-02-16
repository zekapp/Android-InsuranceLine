package au.com.lumo.ameego.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MSiteNodeVm;
import au.com.lumo.ameego.viewHolders.RewardsViewHolder;

/**
 * Created by appscoredev2 on 10/07/15.
 */
public class RewardsAdapter extends RecyclerView.Adapter<RewardsViewHolder>{

    private ArrayList<MSiteNodeVm> mData = new ArrayList<>();
    private IRewardAdapter mCallback;

    public void setIRewardAdapterCallback(IRewardAdapter callback){
        this.mCallback = callback;
    }

    public interface IRewardAdapter{
        void onCardClicked(View view, int position, RewardsViewHolder viewHolder);
        void onCategoryNameClicked(View view, int position, RewardsViewHolder viewHolder);
    }

    @Override
    public RewardsViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_category_item, viewGroup, false);
        return new RewardsViewHolder(view, new CustomOnClickListener());
    }

    @Override
    public void onBindViewHolder(RewardsViewHolder holder, int position) {
        holder.initFromData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItems(ArrayList<MSiteNodeVm>  data) {
        this.mData.clear();
        this.mData.addAll(data);
    }

    public boolean isEmpty() {return mData.isEmpty();}

    private class CustomOnClickListener implements RewardsViewHolder.IViewHolderClick {
        @Override
        public void onCardClick(View view, int position, RewardsViewHolder viewHolder) {
            if(mCallback != null) mCallback.onCardClicked(view,position,viewHolder);
        }

        @Override
        public void onCategoryNameClicked(View view, int position, RewardsViewHolder viewHolder) {
            if(mCallback != null) mCallback.onCategoryNameClicked(view, position, viewHolder);
        }
    }
}
