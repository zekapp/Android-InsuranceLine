package com.insuranceline;

import android.content.Context;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.ui.login.LoginMvpView;
import com.insuranceline.ui.login.LoginPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * Created by zeki on 30/01/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest{
    private static final java.lang.String FAKE_ERROR_INVALID_EMAIL      = "This email address is invalid";
    private static final java.lang.String FAKE_ERROR_INVALID_PASSWORD   = "This Password is too short";
    private static final java.lang.String FAKE_ERROR_FIELD_REQUIRED     = "This field is required";
    private static final java.lang.String FAKE_ERROR_UNAUTHORIZED_USER  = "The email/phone you entered is incorrect. Please try again.";
    @Mock DataManager mMockDataManger;
    @Mock AppConfig mMockAppConfig;
    @Mock LoginMvpView mMockLoginMvpView;
    @Mock Context mMockContext;
    private LoginPresenter mLoginPresenter;

    @Before
    public void setUp(){
        mLoginPresenter = new LoginPresenter(mMockContext, mMockDataManger, mMockAppConfig);
        mLoginPresenter.attachView(mMockLoginMvpView);
    }

    @After
    public void tearDown(){
        mLoginPresenter.detachView();
    }

    @Test
    public void attemptToLoginValidEmailValidPassword(){
        stubGetResourceAndPasswordLenght();
        String userName = "abc@abc.com";
        String password  = "123456";
        when(mMockDataManger.loginEdgeSystem(userName,password))
                .thenReturn(Observable.just(new EdgeUser()));
        mLoginPresenter.attemptToLogin(userName,password);
        verify(mMockLoginMvpView, never()).onErrorEmail(anyString());
        verify(mMockLoginMvpView, never()).onErrorPassword(anyString());
    }

    @Test
    public void attemptToLoginEmptyEmailValidPassword(){
        stubGetResourceAndPasswordLenght();
        mLoginPresenter.attemptToLogin("","123456");
        verify(mMockLoginMvpView).onErrorEmail(mMockContext.getString(R.string.error_field_required));
        verify(mMockLoginMvpView, never()).loginSuccess();
    }

    @Test
    public void attemptToLoginInvalidEmailValidPassword(){
        stubGetResourceAndPasswordLenght();
        mLoginPresenter.attemptToLogin("dsasdsd.com","123456");
        verify(mMockLoginMvpView).onErrorEmail(mMockContext.getString(R.string.error_invalid_email));
        verify(mMockLoginMvpView, never()).onErrorEmail(mMockContext.getString(R.string.error_field_required));
        verify(mMockLoginMvpView,never()).onErrorPassword(anyString());
        verify(mMockLoginMvpView, never()).loginSuccess();
    }

    @Test
    public void attemptToLoginValidEmailEmptyPassword(){
        stubGetResourceAndPasswordLenght();
        mLoginPresenter.attemptToLogin("dsasdsd@csd.com","");
        verify(mMockLoginMvpView).onErrorPassword(mMockContext.getString(R.string.error_field_required));
        verify(mMockLoginMvpView, never()).loginSuccess();
    }

    @Test
    public void attemptToLoginValidEmailInvalidPassword(){
        stubGetResourceAndPasswordLenght();
        mLoginPresenter.attemptToLogin("dsasdsd@csd.com","123");
        verify(mMockLoginMvpView, times(1)).onErrorPassword(mMockContext.getString(R.string.error_invalid_password));
        verify(mMockLoginMvpView, never()).onErrorEmail(anyString());
        verify(mMockLoginMvpView, never()).loginSuccess();
    }

    @Test
    public void attemptToLoginInValidEmailInvalidPassword(){
        stubGetResourceAndPasswordLenght();
        mLoginPresenter.attemptToLogin("dsasdcsd.com","123");
        verify(mMockLoginMvpView, times(1)).onErrorPassword(mMockContext.getString(R.string.error_invalid_password));
        verify(mMockLoginMvpView, times(1)).onErrorEmail(mMockContext.getString(R.string.error_invalid_email));
        verify(mMockLoginMvpView, never()).loginSuccess();
    }

    @Test
    public void attemptToLoginEmptyEmailEmptyPassword(){
        stubGetResourceAndPasswordLenght();
        mLoginPresenter.attemptToLogin("","");
        verify(mMockLoginMvpView, times(1)).onErrorPassword(mMockContext.getString(R.string.error_field_required));
        verify(mMockLoginMvpView, times(1)).onErrorEmail(mMockContext.getString(R.string.error_field_required));
        verify(mMockLoginMvpView, never()).loginSuccess();
    }

    @Test
    public void attemptToLoginError(){
        stubGetResourceAndPasswordLenght();
        String userName = "zeki.guler@icloud.com";
        String password = "123456";
        when(mMockDataManger.loginEdgeSystem(userName,password))
                .thenReturn(Observable.<EdgeUser>error(new RuntimeException()));
        mLoginPresenter.attemptToLogin(userName,password);
        verify(mMockLoginMvpView, never()).onErrorEmail(anyString());
        verify(mMockLoginMvpView, never()).onErrorPassword(anyString());
        verify(mMockLoginMvpView).showLoginError(anyString());
    }

    @Test
    public void attemptToLoginSuccess(){
        String userName = "zeki.guler@icloud.com";
        String password = "123456";

        stubGetResourceAndPasswordLenght();
        EdgeAuthResponse response = TestDataFactory.getEdgeResponse();
        EdgeUser edgeUser = TestDataFactory.getEdgeUser(userName,response);

        when(mMockDataManger.loginEdgeSystem(userName,password))
                .thenReturn(Observable.just(edgeUser));

        mLoginPresenter.attemptToLogin(userName,password);
        verify(mMockLoginMvpView, never()).onErrorPassword(anyString());
        verify(mMockLoginMvpView, never()).onErrorEmail(anyString());
        verify(mMockLoginMvpView, times(1)).loginSuccess();

    }

    private void stubGetResourceAndPasswordLenght(){
        when(mMockAppConfig.getPasswordLength()).thenReturn(6);
        when(mMockContext.getString(R.string.error_field_required))
                .thenReturn(FAKE_ERROR_FIELD_REQUIRED);
        when(mMockContext.getString(R.string.error_invalid_email))
                .thenReturn(FAKE_ERROR_INVALID_EMAIL);
        when(mMockContext.getString(R.string.error_invalid_password))
                .thenReturn(FAKE_ERROR_INVALID_PASSWORD);
        when(mMockContext.getString(R.string.error_unauthorized_user))
                .thenReturn(FAKE_ERROR_UNAUTHORIZED_USER);
    }
}
