package com.insuranceline;

/**
 * Created by Zeki Guler on 25,January,2016
 * ©2015 Appscore. All Rights Reserved
 */

import com.insuranceline.data.DataManager;
import com.insuranceline.data.local.DatabaseHelper;
import com.insuranceline.data.remote.ApiService;
import com.insuranceline.data.remote.EdgeApiService;
import com.insuranceline.data.remote.FitBitApiService;
import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.data.vo.Sample;
import com.path.android.jobqueue.JobManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. ApiServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock JobManager mJobHelper;
    @Mock ApiService mMockApiService;
    @Mock EdgeApiService mEdgeApiService;
    @Mock FitBitApiService mFitBitApiService;

    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockApiService,mEdgeApiService,mFitBitApiService, mMockDatabaseHelper, mJobHelper);
    }

    @Test
    public void syncSamplesEmitValues(){
        int page = 1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(page,perPage);
        stubSyncSamplesHelperCalls(sampleResponseData,page, perPage);

        TestSubscriber<Sample> result = new TestSubscriber<>();
        mDataManager.syncSample(page, perPage).subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(sampleResponseData.getSampleResponse().getSampleList());
    }

    @Test
    public void syncSamplesCallsApiAndDatabase(){
        int page = 1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(page,perPage);
        stubSyncSamplesHelperCalls(sampleResponseData,page, perPage);

        mDataManager.syncSample(page, perPage).subscribe();
        // Verify right calls to helper methods
        verify(mMockApiService).getSamples(page, perPage);
        verify(mMockDatabaseHelper).setSamples(sampleResponseData.getSampleResponse().getSampleList());

    }

    @Test
    public void syncSamplesDoesNotCallDatabaseWhenApiFails(){
        int page =1;
        int perPage = 30;
        when(mMockApiService.getSamples(page,perPage)).thenReturn(Observable.<SampleResponseData>error(new RuntimeException()));

        mDataManager.syncSample(page, perPage).subscribe(new TestSubscriber<Sample>());

        verify(mMockApiService).getSamples(page,perPage);
        verify(mMockDatabaseHelper, never()).setSamples(anyListOf(Sample.class));
    }

    private void stubSyncSamplesHelperCalls(SampleResponseData sampleResponseData, int page, int perPage) {
        List<Sample> samples = sampleResponseData.getSampleResponse().getSampleList();

        when(mMockApiService.getSamples(page,perPage))
                .thenReturn(Observable.just(sampleResponseData));
        when(mMockDatabaseHelper.setSamples(samples))
                .thenReturn(Observable.from(samples));
    }


    @Test
    public void getAllSamplesEmitValues(){
        List<Sample> samples = TestDataFactory.getSampleList(300);

        when(mMockDatabaseHelper.sampleListQuery())
                .thenReturn(samples);

        TestSubscriber<List<Sample>> testObserver = new TestSubscriber<>();
        mDataManager.getAllSamples().subscribe(testObserver);
        testObserver.assertNoErrors();
        testObserver.assertValue(samples);
    }

    @Test
    public void getSamplesFromDbThenUpdateViaApiEmitValues(){
        int page =1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(0,30);

        List<Sample> samplesFromDb          = TestDataFactory.getSampleList(30);
        List<Sample> samplesFromUpdatedDb   = sampleResponseData.getSampleResponse().getSampleList();

        stubGetSamplesFromDbThenUpdateViaApi(samplesFromDb,sampleResponseData);

        TestSubscriber<List<Sample>> testSubscriber = new TestSubscriber<>();
        mDataManager.getSamplesFromDbThenUpdateViaApi(page, perPage).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(samplesFromDb,samplesFromUpdatedDb);
    }

    @Test
    public void getSamplesFromDbThenUpdateViaCallApiAndDb(){
        int page =1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(0,30);

        List<Sample> samplesFromDb          = TestDataFactory.getSampleList(30);
        List<Sample> samplesFromUpdatedDb   = sampleResponseData.getSampleResponse().getSampleList();

        stubGetSamplesFromDbThenUpdateViaApi(samplesFromDb,sampleResponseData);

        TestSubscriber<List<Sample>> testSubscriber = new TestSubscriber<>();
        mDataManager.getSamplesFromDbThenUpdateViaApi(page, perPage).subscribe(testSubscriber);

        verify(mMockApiService).getSamples(page,perPage);
        verify(mMockDatabaseHelper,atMost(1)).setSamples(samplesFromUpdatedDb);
        verify(mMockDatabaseHelper, times(2)).sampleListQuery();
    }


    @Test
    public void getSamplesFromDbThenDontUpdateIfApiFails(){
        int page =1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(0,30);

        List<Sample> samplesFromDb          = TestDataFactory.getSampleList(30);
        List<Sample> samplesFromUpdatedDb   = sampleResponseData.getSampleResponse().getSampleList();
        List<Sample> samplesFromApi         = sampleResponseData.getSampleResponse().getSampleList();

        stubGetSamplesFromDbThenUpdateViaFailedApi(samplesFromDb,sampleResponseData);

        TestSubscriber<List<Sample>> testSubscriber = new TestSubscriber<>();
        mDataManager.getSamplesFromDbThenUpdateViaApi(page, perPage).subscribe(testSubscriber);
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(samplesFromDb);

        verify(mMockDatabaseHelper, never()).setSamples(samplesFromApi);

    }

    private void stubGetSamplesFromDbThenUpdateViaApi(List<Sample> samplesFromDb, SampleResponseData sampleDataFromApi) {
        List<Sample> sampleFromApi      = sampleDataFromApi.getSampleResponse().getSampleList();
        List<Sample> sampleFromUpdateDb = sampleDataFromApi.getSampleResponse().getSampleList();

        when(mMockDatabaseHelper.sampleListQuery())
                .thenReturn(samplesFromDb)
                .thenReturn(sampleFromUpdateDb);

        when(mMockApiService.getSamples(anyInt(),anyInt())).thenReturn(Observable.just(sampleDataFromApi));

        when(mMockDatabaseHelper.setSamples(sampleFromApi)).thenReturn(Observable.from(sampleFromApi));
    }

    private void stubGetSamplesFromDbThenUpdateViaFailedApi(List<Sample> samplesFromDb, SampleResponseData sampleDataFromApi) {
        List<Sample> sampleFromApi      = sampleDataFromApi.getSampleResponse().getSampleList();
        List<Sample> sampleFromUpdateDb = sampleDataFromApi.getSampleResponse().getSampleList();

        when(mMockDatabaseHelper.sampleListQuery())
                .thenReturn(samplesFromDb)
                .thenReturn(sampleFromUpdateDb);

        when(mMockApiService.getSamples(anyInt(),anyInt())).thenReturn(Observable.<SampleResponseData>error(new RuntimeException()));

        when(mMockDatabaseHelper.setSamples(sampleFromApi)).thenReturn(Observable.from(sampleFromApi));
    }

    @Test
    public void  loginEdgeApiFails(){
        String userName = "abc@abc.com";
        String password = "123456";
        when(mEdgeApiService.getAuthToken(userName,password,"password"))
                .thenReturn(Observable.<EdgeAuthResponse>error(new RuntimeException()));

        TestSubscriber<EdgeUser> testSubscriber = new TestSubscriber<>();
        mDataManager.loginEdgeSystem(userName, password).subscribe(testSubscriber);
        testSubscriber.assertNoValues();
        testSubscriber.assertError(Exception.class);

        verify(mMockDatabaseHelper, never()).createEdgeUser(anyString(), any(EdgeAuthResponse.class));
    }

    @Test
    public void loginEdgeSuccess(){
        String userName = "abc@abc.com";
        String password = "123456";
        EdgeAuthResponse edgeAuthResponse = TestDataFactory.getEdgeResponse();
        EdgeUser edgeUser = TestDataFactory.getEdgeUser(userName, edgeAuthResponse);

        when(mEdgeApiService.getAuthToken(userName,password,"password"))
                .thenReturn(Observable.just(edgeAuthResponse));
        when(mMockDatabaseHelper.createEdgeUser(userName, edgeAuthResponse))
                .thenReturn(Observable.just(edgeUser));

        TestSubscriber<EdgeUser> testSubscriber = new TestSubscriber<>();
        mDataManager.loginEdgeSystem(userName, password).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(edgeUser);

        verify(mMockDatabaseHelper).createEdgeUser(userName, edgeAuthResponse);

    }
}
