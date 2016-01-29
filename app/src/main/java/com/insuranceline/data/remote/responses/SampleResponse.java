package com.insuranceline.data.remote.responses;

import com.insuranceline.data.vo.Sample;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Zeki Guler on 19,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class SampleResponse {
    private int total;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("last_page")
    private int lastPage;
    private int from;
    private int to;
    @JsonProperty("items")
    List<Sample> sampleList;

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public List<Sample> getSampleList() {
        return sampleList;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setSampleList(List<Sample> sampleList) {
        this.sampleList = sampleList;
    }
}
