package com.insuranceline.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {

    private List<Sample> mSamples;

    @Inject
    public SampleAdapter() {
        mSamples = new ArrayList<>();
    }

    public void setSamples(List<Sample> samples) {
        mSamples = samples;
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sample, parent, false);
        return new SampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        Sample sample = mSamples.get(position);
        holder.idTextView.setText(String.valueOf(sample.getSampleId()));
        holder.timeTextView.setText(TimeUtils.convertReadableDate(sample.getTime(),TimeUtils.DATE_FORMAT_TYPE_1));
        holder.descriptionTextView.setText(sample.getDescription());
    }

    @Override
    public int getItemCount() {
        return mSamples.size();
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sample_id) TextView idTextView;
        @Bind(R.id.sample_description) TextView descriptionTextView;
        @Bind(R.id.sample_time) TextView timeTextView;

        public SampleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
