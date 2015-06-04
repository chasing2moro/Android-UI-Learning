package com.jushen.sdk.weibo.openapi;

import android.content.Context;
import android.util.SparseArray;

import com.jushen.utils.log.LoggerUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

public class TimeLineAPI extends OpenAPIBase {

	// https://api.weibo.com/2/statuses/friends_timeline.json

	private static final int READ_PUBLIC = 0;
	private static final int READ_FRIENDS = 1;

	private static final String API_BASE_URL = API_SERVER + "/statuses";

	private static final SparseArray<String> sAPIList = new SparseArray<String>();
	static {
		sAPIList.put(READ_PUBLIC, API_BASE_URL + "/public_timeline.json");
		sAPIList.put(READ_FRIENDS, API_BASE_URL + "/friends_timeline.json");
	}

	public TimeLineAPI(Context context, String appKey,
			Oauth2AccessToken accessToken) {
		super(context, appKey, accessToken);
	}

	/**
	 * @param listener
	 *            异步请求回调接口
	 */
	public void requestFriendsTimeLine(RequestListener listener) {
		WeiboParameters params = new WeiboParameters(mAppKey);
		requestAsync(sAPIList.get(READ_FRIENDS), params, HTTPMETHOD_GET, listener);
	}
	
	/**
	 * @param listener
	 *            异步请求回调接口
	 */
	public void requestPublicTimeLine(RequestListener listener) {
		WeiboParameters params = new WeiboParameters(mAppKey);
		LoggerUtils.i("req appKey:" + mAppKey);
		requestAsync(sAPIList.get(READ_PUBLIC), params, HTTPMETHOD_GET, listener);
	}
}
