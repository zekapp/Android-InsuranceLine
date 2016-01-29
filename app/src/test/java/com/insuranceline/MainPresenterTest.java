package com.insuranceline;


import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.ui.main.MainMvpView;
import com.insuranceline.ui.main.MainPresenter;

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
public class MainPresenterTest {
    @Mock
    DataManager mMockDataManger;
    @Mock
    MainMvpView mMockMainMvpView;

    private MainPresenter mMainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp(){
        mMainPresenter = new MainPresenter(mMockDataManger);
        mMainPresenter.attachView(mMockMainMvpView);
    }

    @After
    public void tearDown(){
        mMainPresenter.detachView();
    }

    @Test
    public void loadSamplesReturnSamples(){
        List<Sample> samples = TestDataFactory.getSampleList(30);

        when(mMockDataManger.getSamplesFromDbThenUpdateViaApi(0,30))
                .thenReturn(Observable.just(samples));

        mMainPresenter.loadSamples();
        verify(mMockMainMvpView).showSamples(samples);
        verify(mMockMainMvpView,never()).showError();
        verify(mMockMainMvpView, never()).showSamplesEmpty();
    }

    @Test
    public void loadSamplesReturnEmptyList(){
        when(mMockDataManger.getSamplesFromDbThenUpdateViaApi(0,30))
                .thenReturn(Observable.just(Collections.<Sample>emptyList()));

        mMainPresenter.loadSamples();
        verify(mMockMainMvpView,never()).showSamples(anyListOf(Sample.class));
        verify(mMockMainMvpView,never()).showError();
        verify(mMockMainMvpView).showSamplesEmpty();
    }

    @Test
    public void loadSamplesReturnError(){
        doReturn(Observable.error(new RuntimeException()))
                .when(mMockDataManger)
                .getSamplesFromDbThenUpdateViaApi(0,30);

        mMainPresenter.loadSamples();
        verify(mMockMainMvpView,never()).showSamples(anyListOf(Sample.class));
        verify(mMockMainMvpView).showError();
        verify(mMockMainMvpView,never()).showSamplesEmpty();
    }
}
