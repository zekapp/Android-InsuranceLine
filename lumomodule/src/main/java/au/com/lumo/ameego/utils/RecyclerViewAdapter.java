package au.com.lumo.ameego.utils;

/**
 * Created by Zeki Guler on 22/07/15.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static class ExtraItem
    {
        public final int type;
        public final RecyclerView.ViewHolder view;

        public ExtraItem(int type, RecyclerView.ViewHolder view)
        {
            this.type = type;
            this.view = view;
        }

        public ExtraItem(int type, View view)
        {
            this.type = type;
            this.view = new RecyclerView.ViewHolder(view) {};
        }
    }

    private View emptyView;

    private final Context context;
    private final List<ExtraItem> headers;
    private final List<ExtraItem> footers;

    public RecyclerViewAdapter(Context context)
    {
        this.context = context;
        this.headers = new ArrayList<>();
        this.footers = new ArrayList<>();
    }

    public Context getContext()
    {
        return context;
    }

    public void setEmptyView(View emptyView)
    {
        this.emptyView = emptyView;
        emptyView.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public abstract int getCount();

    @Override
    public final int getItemCount()
    {
        int count = headers.size();
        count += getCount();
        count += footers.size();

        if (emptyView != null)
            emptyView.setVisibility(getCount() == 0 ? View.VISIBLE : View.GONE);

        return count;
    }

    public ExtraItem addHeaderView(int type, RecyclerView.ViewHolder headerView)
    {
        ExtraItem item = new ExtraItem(type, headerView);
        addHeaderView(item);
        return item;
    }

    public void addHeaderView(ExtraItem headerView)
    {
        headers.add(headerView);
        notifyItemInserted(headers.size());
    }

    public void removeHeaderView(int type)
    {
        List<ExtraItem> indexesToRemove = new ArrayList<>();
        for (int i=0; i<headers.size(); i++)
        {
            ExtraItem item = headers.get(i);
            if (item.type == type)
                indexesToRemove.add(item);
        }

        for (ExtraItem item : indexesToRemove)
        {
            int index = headers.indexOf(item);
            headers.remove(item);
            notifyItemRemoved(index);
        }
    }

    public void removeHeaderView(ExtraItem headerView)
    {
        int indexToRemove = headers.indexOf(headerView);
        if (indexToRemove >= 0)
        {
            headers.remove(indexToRemove);
            notifyItemRemoved(indexToRemove);
        }
    }

    public ExtraItem addFooterView(int type, RecyclerView.ViewHolder footerView)
    {
        ExtraItem item = new ExtraItem(type, footerView);
        addFooterView(item);
        return item;
    }

    public void addFooterView(ExtraItem footerView)
    {
        footers.add(footerView);
        notifyItemInserted(getItemCount());
    }

    public void removeFooterView(int type)
    {
        List<ExtraItem> indexesToRemove = new ArrayList<>();
        for (int i=0; i<footers.size(); i++)
        {
            ExtraItem item = footers.get(i);
            if (item.type == type)
                indexesToRemove.add(item);
        }

        for (ExtraItem item : indexesToRemove)
        {
            int index = footers.indexOf(item);
            footers.remove(item);
            notifyItemRemoved(headers.size() + getCount() + index);
        }
    }

    public void removeFooterView(ExtraItem footerView)
    {
        int indexToRemove = footers.indexOf(footerView);
        if (indexToRemove >= 0)
        {
            footers.remove(indexToRemove);
            notifyItemRemoved(headers.size() + getCount() + indexToRemove);
        }
    }

    public int getViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @Override
    public final int getItemViewType(int position)
    {
        if (position < headers.size())
            return headers.get(position).type;
        if (position >= headers.size() + getCount())
            return footers.get(position - (headers.size() + getCount())).type;
        return getViewType(position - headers.size());
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        for (ExtraItem item : headers)
            if (viewType == item.type)
                return item.view;

        for (ExtraItem item : footers)
            if (viewType == item.type)
                return item.view;

        return onCreateView(parent, viewType);
    }

    public abstract V onCreateView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (position >= headers.size() && (position - headers.size()) < getCount())
            //noinspection unchecked
            onBindView((V) holder, position - headers.size());
    }

    public abstract void onBindView(V view, int position);

}