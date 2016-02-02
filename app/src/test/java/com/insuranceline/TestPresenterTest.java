package com.insuranceline;


import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.ui.sample.TestMvpView;
import com.insuranceline.ui.sample.TestPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@RunWith(MockitoJUnitRunner.class)
public class TestPresenterTest {
    @Mock
    DataManager mMockDataManger;
    @Mock
    TestMvpView mMockTestMvpView;

    private TestPresenter mTestPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp(){
        mTestPresenter = new TestPresenter(mMockDataManger);
        mTestPresenter.attachView(mMockTestMvpView);
    }

    @After
    public void tearDown(){
        mTestPresenter.detachView();
    }

    @Test
    public void loadSamplesReturnSamples(){
        List<Sample> samples = TestDataFactory.getSampleList(30);

        when(mMockDataManger.getSamplesFromDbThenUpdateViaApi(0,30))
                .thenReturn(Observable.just(samples));

        mTestPresenter.loadSamples();
        verify(mMockTestMvpView).showSamples(samples);
        verify(mMockTestMvpView,never()).showError();
        verify(mMockTestMvpView, never()).showSamplesEmpty();
    }

    @Test
    public void loadSamplesReturnEmptyList(){
        when(mMockDataManger.getSamplesFromDbThenUpdateViaApi(0,30))
                .thenReturn(Observable.just(Collections.<Sample>emptyList()));

        mTestPresenter.loadSamples();
        verify(mMockTestMvpView,never()).showSamples(anyListOf(Sample.class));
        verify(mMockTestMvpView,never()).showError();
        verify(mMockTestMvpView).showSamplesEmpty();
    }

    @Test
    public void loadSamplesReturnError(){
        doReturn(Observable.error(new RuntimeException()))
                .when(mMockDataManger)
                .getSamplesFromDbThenUpdateViaApi(0,30);

        mTestPresenter.loadSamples();
        verify(mMockTestMvpView,never()).showSamples(anyListOf(Sample.class));
        verify(mMockTestMvpView).showError();
        verify(mMockTestMvpView,never()).showSamplesEmpty();
    }
}
