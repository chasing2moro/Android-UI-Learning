package com.jushen.sdk.weibo.openapi;


import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import com.sina.weibo.sdk.openapi.AbsOpenAPI;

public class OpenAPIBase extends AbsOpenAPI{

	public OpenAPIBase(Context context, String appKey,
			Oauth2AccessToken accessToken) {
		super(context, appKey, accessToken);
		// TODO Auto-generated constructor stub
	}
	

}
