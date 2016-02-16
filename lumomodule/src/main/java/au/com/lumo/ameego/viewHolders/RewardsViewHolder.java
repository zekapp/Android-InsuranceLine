package au.com.lumo.ameego.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MSiteNodeVm;
import au.com.lumo.ameego.utils.LumoSpecificUtils;

/**
 * Created by appscoredev2 on 10/07/15.
 */
public class RewardsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = RewardsViewHolder.class.getSimpleName();

    private IViewHolderClick  listener;
    private TextView          mCategoryName;
    private MSiteNodeVm       mSiteNode;
    private ImageView mIcon;

    public RewardsViewHolder(View v, IViewHolderClick listener) {
        super(v);
        this.listener = listener;
        mCategoryName = (TextView) v.findViewById(R.id.item_reward_text);
        mIcon        =  (ImageView) v.findViewById(R.id.sub_cat_icon);
        v.setOnClickListener(this);
    }

    public void initFromData(MSiteNodeVm siteNode) {
        mSiteNode = siteNode;
        String categoryName = LumoSpecificUtils.getProperCategoryNameFromSiteNode(siteNode);
        mCategoryName.setText(categoryName);
//        mCategoryName.setCompoundDrawablesWithIntrinsicBounds(LumoSpecificUtils.findProperIcon(categoryName),0,0,0);
        mIcon.setImageResource(LumoSpecificUtils.getIcon(mSiteNode.getSiteNodeId()));
//        mCategoryName.setCompoundDrawablesWithIntrinsicBounds(LumoSpecificUtils.getIcon(mSiteNode.getSiteNodeId()), 0,0,0);
    }

    @Override
    public void onClick(View view) {
        listener.onCardClick(view, getLayoutPosition(), this);
    }

    public MSiteNodeVm getItem(){
        return mSiteNode;
    }

    public interface IViewHolderClick {
        void onCardClick           (View view, int position, RewardsViewHolder viewHolder);
        void onCategoryNameClicked (View view, int position, RewardsViewHolder viewHolder);
    }
}