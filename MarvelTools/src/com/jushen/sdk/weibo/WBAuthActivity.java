/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jushen.sdk.weibo;

import java.text.SimpleDateFormat;

import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.Facade;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
/**
 * 该类主要演示如何进行授权、SSO登陆。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBAuthActivity extends Activity {
    
    private static final String TAG = "weibosdk";

    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;
    
    private AuthInfo mAuthInfo;
    
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }else {
			LoggerUtils.e("actionBar = null");
		}

        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        
        // 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
        mTokenText = (TextView) findViewById(R.id.textview__auth__hint);

        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
        
        // SSO 授权, 仅客户端
//        findViewById(R.id.obtain_token_via_sso).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSsoHandler.authorizeClientSso(new AuthListener());
//            }
//        });
        
        // SSO 授权, 仅Web
//        findViewById(R.id.obtain_token_via_web).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSsoHandler.authorizeWeb(new AuthListener());
//            }
//        });
        
        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
        findViewById(R.id.button__auth__login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new AuthListener());
            }
        });
        
       
        
        // 用户登出
//        findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AccessTokenKeeper.clear(getApplicationContext());
//                mAccessToken = new Oauth2AccessToken();
//                updateTokenView(false);
//            }
//        });
        
//        // 通过 Code 获取 Token
//        findViewById(R.id.obtain_token_via_code).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(WBAuthActivity.this, WBAuthCodeActivity.class));
//            }
//        });

        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);
                
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
//                Toast.makeText(WBAuthActivity.this, 
//                        "auth success", Toast.LENGTH_SHORT).show();
                Facade.singleton().sendEvent(EventName.CommonUtils_ToastShow, 
                		EventArg.Create().setUserInfo(WBAuthActivity.this).putString("text", "auth success"));
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "auth failed";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Facade.singleton().sendEvent(EventName.CommonUtils_ToastShow, 
                		EventArg.Create().setUserInfo(WBAuthActivity.this).putString("text", message));
             //   Toast.makeText(WBAuthActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Facade.singleton().sendEvent(EventName.CommonUtils_ToastShow, 
            		EventArg.Create().setUserInfo(WBAuthActivity.this).putString("text", "auth canceled"));
//            Toast.makeText(WBAuthActivity.this, 
//                    "auth canceled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
//            Toast.makeText(WBAuthActivity.this, 
//                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Facade.singleton().sendEvent(EventName.CommonUtils_ToastShow, 
            		EventArg.Create().setUserInfo(WBAuthActivity.this).putString("text", "Auth exception : " + e.getMessage()));
        }
    }
    
    EditText _editText;
    /**
     * 显示当前 Token 信息。
     * 
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = "Token：%s \nExpireAt：%s";
   
        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = "token has existed" + "\n" + message;
        }
        message += "\n status:Logined";
        mTokenText.setText(message);
        
        if(_editText == null)
        	_editText = (EditText)findViewById(R.id.editText__auth__hint);
        _editText.setText("APP_KEY:" + Constants.APP_KEY +
        		" \n\n" + "token:" + mAccessToken.getToken());
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	// TODO Auto-generated method stub
    	LoggerUtils.i("featureId:" + featureId +
    			" itemId:" + item.getItemId() +
    			" R.id.home:" + R.id.home +
    			" android.R.id.home:" + android.R.id.home);
    	switch (item.getItemId()) {
		case android.R.id.home:
			_popUpActivity();
			break;

		default:
			break;
		}
    	return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {	
    	if(keyCode==KeyEvent.KEYCODE_BACK && 
    			event.getRepeatCount() == 0){// = 0 means long press
    		_popUpActivity();
    	}

    	return true;
    }
    
    private void _popUpActivity() {
    	finish();
		Facade.singleton().sendEvent(EventName.CommonUtils_ActivitySlideOut, EventArg.Create().setUserInfo(this));
	}
}