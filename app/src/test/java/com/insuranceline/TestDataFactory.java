package com.insuranceline;


import com.insuranceline.data.remote.responses.SampleResponse;
import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.vo.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Zeki Guler on 25,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TestDataFactory {

    private static Random random = new Random();

    public static SampleResponseData getSampleResponseData(int page, int perPage) {
        SampleResponseData sampleResponseData = new SampleResponseData();
        sampleResponseData.setSampleResponse(getSampleResponse(page,perPage));
        return sampleResponseData;
    }

    public static SampleResponse getSampleResponse(int currentPage, int perPage ) {
        SampleResponse sampleResponse = new SampleResponse();
        sampleResponse.setTotal(101);
        sampleResponse.setPerPage(perPage);
        sampleResponse.setCurrentPage(currentPage);
        sampleResponse.setLastPage(11);
        sampleResponse.setFrom(1);
        sampleResponse.setTo(10);
        sampleResponse.setSampleList(getSampleList(perPage));
        return sampleResponse;
    }

    public static List<Sample> getSampleList(int count) {
        List<Sample> samples = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Sample sample = new Sample();
            sample.setDescription(getRandomText("Description"));
            sample.setTime(getRandomLong());
            sample.setSampleId(getRandomLong());

            samples.add(sample);
        }

        return samples;
    }

    public static long getRandomLong() {
        return random.nextLong();
    }

    public static String getRandomText(String preDef){
        return preDef + ": "+ UUID.randomUUID().toString();
    }
}
