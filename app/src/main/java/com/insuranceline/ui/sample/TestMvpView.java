package com.insuranceline.ui.sample;

import com.insuranceline.data.vo.Sample;
import com.insuranceline.ui.base.MvpView;

import java.util.List;


public interface TestMvpView extends MvpView {

    void showSamples(List<Sample> samples);

    void showSamplesEmpty();

    void showError();

}
