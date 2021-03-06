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
import android.widget.AdapterView;
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
import com.jushen.sdk.weibo.utils.JasonParserUtils;
import com.jushen.utils.event.CommonUtils;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.activity.DetailFriendTimelineActivity;
import com.jushencompany.marveltools.item.TimeLineAdapter;
import com.jushencompany.marveltools.model.TimeLineUserInfo;
import com.jushencompany.marveltools.model.TimeLineUserInfoManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class MainArraySubViewCommunity extends MainArrayViewBase implements RequestListener, DialogInterface.OnClickListener{
	ListView _listViewFriendsTimeline;
	TimeLineAdapter _timeLineAdapter;
	
	boolean _requested = false;
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
		registEvent(EventName.AsyncImageLoaderPlus_DownloadImageRep, "onAsyncImageLoaderPlus_DownloadProfileImageFinish");
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		super.onUnRegistEvent();
		unRegistEvent(EventName.AsyncImageLoaderPlus_DownloadImageRep, "onAsyncImageLoaderPlus_DownloadProfileImageFinish");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LoggerUtils.i("Comuunity Fragment Resume");
		if(getIsVisible() && !_requested)
			requestTimeLine();
	}
	
	@Override
	protected void onSelected() {
		// TODO Auto-generated method stub
		super.onSelected();
		if(getActivity() != null && !_requested)
			requestTimeLine();
	}
	
	public Object onAsyncImageLoaderPlus_DownloadProfileImageFinish(EventArg vEventArg){
		//LoggerUtils.i("onAsyncImageLoaderPlus_DownloadProfileImageFinish");
		_timeLineAdapter.notifyDataSetChanged();
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
		
		_listViewFriendsTimeline = (ListView)vRootView.findViewById(R.id.listview__community__friends_timeline);
		_listViewFriendsTimeline.setClickable(true);
		_listViewFriendsTimeline.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view,
					int position, 
					long id) {
				// TODO Auto-generated method stub
				LoggerUtils.i("list view clicked pos:" + position);
				Intent aIntent = new Intent();
				aIntent.setClass(getActivity(), DetailFriendTimelineActivity.class);
				aIntent.putExtra("index", position);
				startActivity(aIntent);
				sendEvent(EventName.CommonUtils_ActivitySlideIn, EventArg.Create().setUserInfo(getActivity()));
			}
			
		});

	}
	
	Object _showRotateObject;
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
			_showRotateObject = sendEvent(EventName.ViewUtils_Rotate_Hint_Show, EventArg.Create().setUserInfo(getActivity()));
			
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
			JSONObject friendTimeLineJsonObject = null;
			try {
				friendTimeLineJsonObject = statusesJsonArray.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TimeLineUserInfo aTimeLineUserInfo = (TimeLineUserInfo)sendEvent(EventName.JasonParserUtils_GetTimeLineUserInfoFromJasonObject, 
					EventArg.Create().setUserInfo(friendTimeLineJsonObject));
			if(aTimeLineUserInfo != null)
				TimeLineUserInfoManager.singleton().add(aTimeLineUserInfo);
		}

	        
		_timeLineAdapter = new TimeLineAdapter(getActivity(), TimeLineUserInfoManager.singleton().getDataList());
		_listViewFriendsTimeline.setAdapter(_timeLineAdapter);
		
		_requested = true;
		
		sendEvent(EventName.ViewUtils_Rotate_Hint_Hide, EventArg.Create().setUserInfo(_showRotateObject));
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
