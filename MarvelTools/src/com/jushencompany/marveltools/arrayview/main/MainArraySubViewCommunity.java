package com.jushencompany.marveltools.arrayview.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.EventRegister;
import com.jushen.framework.event.Facade;
import com.jushen.sdk.weibo.AccessTokenKeeper;
import com.jushen.sdk.weibo.Constants;
import com.jushen.sdk.weibo.WBAuthActivity;
import com.jushen.sdk.weibo.openapi.TimeLineAPI;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.item.TimeLineAdapter;
import com.jushencompany.marveltools.model.TimeLineUserInfo;
import com.jushencompany.marveltools.model.TimeLineUserInfoManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class MainArraySubViewCommunity extends MainArrayViewBase implements RequestListener, DialogInterface.OnClickListener{
	ListView _ListViewFriendsTimeline;
	TimeLineAdapter _TimeLineAdapter;
	@Override
	protected MainArrayViewType obtainViewType() {
		// TODO Auto-generated method stub
		return MainArrayViewType.community;
	}

	@Override
	protected int obtainInflateLayout() {
		// TODO Auto-generated method stub
		return R.layout.main_tab_community;
	}
	TimeLineAPI _TimeLineAPI;
	
	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		super.onRegistEvent();
		registEvent(EventName.AsyncImageLoaderPlus_DownloadProfileImageRep, "onAsyncImageLoaderPlus_DownloadProfileImageFinish");
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		super.onUnRegistEvent();
		unRegistEvent(EventName.AsyncImageLoaderPlus_DownloadProfileImageRep, "onAsyncImageLoaderPlus_DownloadProfileImageFinish");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(getIsVisible())
			requestTimeLine();
	}
	
	@Override
	protected void onSelected() {
		// TODO Auto-generated method stub
		super.onSelected();
		if(getActivity() != null)
			requestTimeLine();
	}
	
	public Object onAsyncImageLoaderPlus_DownloadProfileImageFinish(EventArg vEventArg){
		//LoggerUtils.i("onAsyncImageLoaderPlus_DownloadProfileImageFinish");
		_TimeLineAdapter.notifyDataSetChanged();
		return null;
	}
	
	@Override
	protected void onViewCreated(View vRootView)
	{
//		Button requestFriendsTimeline =(Button) vRootView.findViewById(R.id.button__community__request_friends_timelines);
//		requestFriendsTimeline.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {

//				
//		     
//			}
//			
//		});
		
//		Button refreshListViewButton = (Button)vRootView.findViewById(R.id.button__community__refresh_listview);
//		refreshListViewButton.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				_TimeLineAdapter.notifyDataSetChanged();
//				
//			}
//			
//		});
		
		_ListViewFriendsTimeline = (ListView)vRootView.findViewById(R.id.listview__community__friends_timeline);
	}
	
	void requestTimeLine() {
		LoggerUtils.i("requestTimeLine");
		
		boolean isLogin = false;
		 // 获取当前已保存过的 Token
		 Oauth2AccessToken AccessToken = AccessTokenKeeper.readAccessToken(getActivity());
        // 第一次启动本应用，AccessToken 不可用
        if (AccessToken != null && AccessToken.isSessionValid()) {
        	isLogin = true;
        }
		if (!isLogin) {
			
			/*
			String[] images = new String[] {
					"http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383264_8243.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383243_5120.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383242_3127.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383242_9576.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383242_1721.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383219_5806.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383214_7794.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383213_4418.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383213_3557.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383210_8779.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383172_4577.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383166_3407.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383166_2224.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383166_7301.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383165_7197.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383150_8410.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383131_3736.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383130_5094.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383130_7393.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383129_8813.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383100_3554.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383093_7894.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383092_2432.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383092_3071.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383091_3119.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383059_6589.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383059_8814.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383059_2237.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383058_4330.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406383038_3602.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382942_3079.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382942_8125.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382942_4881.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382941_4559.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382941_3845.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382924_8955.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382923_2141.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382923_8437.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382922_6166.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382922_4843.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382905_5804.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382904_3362.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382904_2312.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382904_4960.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382900_2418.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382881_4490.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382881_5935.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382880_3865.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382880_4662.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382879_2553.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382862_5375.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382862_1748.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382861_7618.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382861_8606.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382861_8949.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382841_9821.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382840_6603.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382840_2405.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382840_6354.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382839_5779.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382810_7578.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382810_2436.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382809_3883.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382809_6269.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382808_4179.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382790_8326.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382789_7174.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382789_5170.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382789_4118.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382788_9532.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382767_3184.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382767_4772.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382766_4924.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382766_5762.jpg",
					"http://img.my.csdn.net/uploads/201407/26/1406382765_7341.jpg" };

			for (int i = 0; i < 30; i++) {
				TimeLineUserInfo aTimeLineUserInfo = new TimeLineUserInfo();
				aTimeLineUserInfo.text = "text:" + i;
				aTimeLineUserInfo.name = "name:" + i;
				aTimeLineUserInfo.profile_image_url = images[i];
				TimeLineUserInfoManager.singleton().add(aTimeLineUserInfo);
			}
			_TimeLineAdapter = new TimeLineAdapter(getActivity(),
					TimeLineUserInfoManager.singleton().getDataList());
			_ListViewFriendsTimeline.setAdapter(_TimeLineAdapter);
			*/

			 // 获取用户信息接口
//			 _TimeLineAPI = new TimeLineAPI(getActivity(),
//					 Constants.APP_KEY,
//                     AccessToken);
//			 _TimeLineAPI.requestPublicTimeLine(MainArraySubViewCommunity.this);
			
			Resources resource = getActivity().getResources();
			sendEvent(
					EventName.CommonUtils_AlertDialogShow,
					EventArg.Create()
							.putString("title",
									resource.getString(R.string.tip_warning))
							.putString(
									"message",
									resource.getString(R.string.tip_havenot_login_comfirm_login))
							.setUserInfo(getActivity())
							.putUserInfo("listener", this));
		}else{
		 // 获取用户信息接口
		 _TimeLineAPI = new TimeLineAPI(getActivity(), 
				 Constants.APP_KEY,
				 AccessToken);
		 _TimeLineAPI.requestFriendsTimeLine(MainArraySubViewCommunity.this);
		}
	}

	@Override
	public void onComplete(String arg0) {
		// TODO Auto-generated method stub
		LoggerUtils.i("TimeLineAPI Request result:" + arg0);
		 JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggerUtils.e("parse json error:" + e.getMessage());
		}
		
		JSONArray statusesJsonArray = jsonObject.optJSONArray("statuses");
		LoggerUtils.i("statusesJsonArray count:" + statusesJsonArray.length());
		

		TimeLineUserInfoManager.singleton().clear();
		for (int i = 0; i < statusesJsonArray.length(); i++) {
			TimeLineUserInfo aTimeLineUserInfo = new TimeLineUserInfo();
			JSONObject friendTimeLineJsonObject = null;
			String temp = null;
			try {
				friendTimeLineJsonObject = statusesJsonArray.getJSONObject(i);
				temp = friendTimeLineJsonObject.getString("text");
				aTimeLineUserInfo.text = temp;
				
				JSONObject userJSONObject = friendTimeLineJsonObject.getJSONObject("user");
				if(userJSONObject == null)
					LoggerUtils.e("user can not find");
				
				temp = userJSONObject.getString("profile_image_url");
				if(TextUtils.isEmpty(temp)){
					LoggerUtils.e("profile_image_url can not find");
					temp = "http://tp1.sinaimg.cn/1668679740/50/5718695216/1";
				}
				aTimeLineUserInfo.profile_image_url = temp;
				
				
				temp = userJSONObject.getString("name");
				aTimeLineUserInfo.name = temp;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			TimeLineUserInfoManager.singleton().add(aTimeLineUserInfo);
		}

	        
		_TimeLineAdapter = new TimeLineAdapter(getActivity(), TimeLineUserInfoManager.singleton().getDataList());
		_ListViewFriendsTimeline.setAdapter(_TimeLineAdapter);;
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub
		LoggerUtils.e("TimeLineAPI Request error:" + arg0);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		//-1 -2
		switch (which) {
		case -1:
			Intent loginIntent = new Intent(getActivity(), WBAuthActivity.class);
			startActivity(loginIntent);
	         //设置切换动画，从右边进入，左边退出
           sendEvent(EventName.CommonUtils_ActivitySlideIn, EventArg.Create().setUserInfo(getActivity()));
			break;
		case -2:
			
			break;
		default:
			break;
		}
	}
}
