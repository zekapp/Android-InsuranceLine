package com.insuranceline.data.vo;

import com.insuranceline.data.local.AppDatabase;
import com.insuranceline.utils.Validation;
import com.insuranceline.utils.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by zeki on 17/01/2016.
 */

@Table(databaseName = AppDatabase.NAME)
public class Sample extends BaseModel implements Validation {
    @Column
    @PrimaryKey(autoincrement = false)
    @JsonProperty("id")
    long mSampleId;

    @Column
    @JsonProperty("description")
    String mDescription;

    @Column
    @JsonProperty("date_time")
    long mTime;


    public void validate() {
        if (mSampleId < 1) {
            throw new ValidationFailedException("invalid sample id");
        }
    }

    public long getSampleId() {
        return mSampleId;
    }

    public void setSampleId(long sampleId) {
        mSampleId = sampleId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
