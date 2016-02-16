package au.com.lumo.ameego.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.model.MCategory;
import au.com.lumo.ameego.utils.LumoSpecificUtils;

/**
 * Created by appscoredev2 on 13/07/15.
 */
public class RewardsSubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String TAG = RewardsSubViewHolder.class.getSimpleName();

        private IViewHolderClick  listener;
        private TextView          mCategoryName;
        private MCategory         mCategory;
        private ImageView         mIcon;

        public RewardsSubViewHolder(View v, IViewHolderClick listener) {
            super(v);
            this.listener = listener;
            mCategoryName = (TextView) v.findViewById(R.id.item_reward_text);
            mIcon        =  (ImageView) v.findViewById(R.id.sub_cat_icon);
            v.setOnClickListener(this);
        }

        public void initFromData(MCategory category) {
            mCategory = category;
            String categoryName = LumoSpecificUtils.getProperCategoryNameFromCategory(category);
            mCategoryName.setText(categoryName);
//            mCategoryName.setCompoundDrawablesWithIntrinsicBounds(LumoSpecificUtils.findProperIcon(categoryName), 0, 0, 0);
            mIcon.setImageResource(LumoSpecificUtils.getSubCatIcon(mCategory.getCategoryId()));
//            mCategoryName.setCompoundDrawablesWithIntrinsicBounds(LumoSpecificUtils.getSubCatIcon(mCategory.getCategoryId()), 0,0,0);
        }

        @Override
        public void onClick(View view) {
            listener.onCardClick(view, getLayoutPosition(), this);
        }

        private class CustomItemClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {

                if (v.getId() ==  R.id.item_reward_text){
                    listener.onCardClick(v, getLayoutPosition(), RewardsSubViewHolder.this);
                }
                /*int id = v.getId();
                switch (id){
                    case R.id.item_reward_text : listener.onCardClick(v, getLayoutPosition(), RewardsSubViewHolder.this); break;
                }*/
            }
        }

        public MCategory getItem(){
            return mCategory;
        }

        public interface IViewHolderClick {
            void onCardClick          (View view, int position, RewardsSubViewHolder viewHolder);
        }
}
