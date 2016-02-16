package au.com.lumo.ameego.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.lumo.ameego.R;
import au.com.lumo.ameego.interfaces.IAdapterPresenter;
import au.com.lumo.ameego.interfaces.IQuestionnaireView;
import au.com.lumo.ameego.model.MListItem;
import au.com.lumo.ameego.model.MQuesRespWarning;
import au.com.lumo.ameego.model.MQuestion;
import au.com.lumo.ameego.utils.Constants;

/**
 * Created by Zeki Guler on 30/07/15.
 */
public class QuestionnaireAdapter extends BaseExpandableListAdapter implements IAdapterPresenter{

    private final LayoutInflater                   mInflater;
    private ArrayList<MQuestion>                   mHeaders          = new ArrayList<>();
    private HashMap<Integer,ArrayList<MListItem>>  mMListItemHashMap = new HashMap<>();

    private Context            mContext;
    private IQuestionnaireView mCallback;

    public QuestionnaireAdapter(Context contex){
        this.mContext = contex;
        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mMListItemHashMap.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mMListItemHashMap.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        final ViewHeaderHolder holder;
        MQuestion question = mHeaders.get(groupPosition);

        if(view == null){
            holder = new ViewHeaderHolder();
            view = mInflater.inflate(R.layout.item_question_header, parent, false);
            holder.questionHeader = (TextView)view.findViewById(R.id.question_header);
            view.setTag(holder);
        }else{
            holder = (ViewHeaderHolder)view.getTag();
        }

        holder.questionHeader.setText(Html.fromHtml(question.getQuestionText()));

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        final ViewChildHolder holder;
        final MListItem option = mMListItemHashMap.get(groupPosition).get(childPosition);

        if(view == null){
            holder = new ViewChildHolder();
            view = mInflater.inflate(R.layout.item_question_option, parent, false);
            holder.mQuestionOption = (TextView)view.findViewById(R.id.question_option);
            holder.mSelectView = (ImageView)view.findViewById(R.id.option_selected);
            view.setTag(holder);
        }else{
            holder = (ViewChildHolder)view.getTag();
        }

        holder.mQuestionOption.setText(Html.fromHtml(option.getListText()));
        holder.mSelectView.setVisibility(option.isItemSelected() ? View.VISIBLE : View.GONE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                option.setItemSelected(!option.isItemSelected());
//                holder.mSelectView.setVisibility(option.isItemSelected() ? View.VISIBLE : View.GONE);
                itemClicked(groupPosition, childPosition, option);
            }
        });

        return view;
    }

    private void itemClicked(int groupPosition, int childPosition, MListItem option) {
        final MQuestion question = mHeaders.get(groupPosition);

        if(question.getQuestionType() == Constants.QuestionType.SINGLEPUNCH){
            //Deselect Other Items In This Question
            deselect(groupPosition);
            //set as true for selected one
            option.setItemSelected(true);
        }else if (question.getQuestionType() == Constants.QuestionType.MULTIPUNCH){
            if (option.isForceSinglePunch()){
                deselect(groupPosition);
                option.setItemSelected(true);
            }else
                option.setItemSelected(!option.isItemSelected());

        }

        notifyDataSetChanged();
    }

    private void deselect(int groupPosition) {
        for (MListItem item : mMListItemHashMap.get(groupPosition)){
            item.setItemSelected(false);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void addAllItemsObject(ArrayList<MQuestion> headers, HashMap<Integer, ArrayList<MListItem>> children) {
        clearAllData();
        this.mHeaders.addAll(headers);
        this.mMListItemHashMap.putAll(children);
        notifyDataSetChanged();
    }

    @Override
    public HashMap<Integer, ArrayList<MListItem>> getOptions() {
        return mMListItemHashMap;
    }

    @Override
    public ArrayList<MQuestion> getQuestions() {
        return mHeaders;
    }

    @Override
    public int getQuestionnaireID() {
        return 0;
    }

    @Override
    public void setWarning(ArrayList<MQuesRespWarning> warnings) {
        // TODO: 3/08/15 if neccessary set this value as static and reset ever time
    }

    private void clearAllData() {
        this.mHeaders.clear();
        this.mMListItemHashMap.clear();
    }


    private static class ViewHeaderHolder {
        TextView questionHeader;
    }

    private static class ViewChildHolder {
        TextView mQuestionOption;
        ImageView mSelectView;
    }

}
