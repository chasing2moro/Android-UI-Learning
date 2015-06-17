package com.jushen.sdk.weibo.utils;

import java.sql.Ref;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;

import com.jushen.framework.event.Controller;
import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.model.TimeLineUserInfo;

public class JasonParserUtils extends Controller{
	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		registEvent(EventName.JasonParserUtils_GetTimeLineUserInfoFromJasonObject, "onHandleGetTimeLineUserInfoFromJasonObject");
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		unRegistEvent(EventName.JasonParserUtils_GetTimeLineUserInfoFromJasonObject, "onHandleGetTimeLineUserInfoFromJasonObject");
	}
	
	TimeLineUserInfo GetTimeLineUserInfoFromJasonObject(JSONObject vJSONObject) {
		TimeLineUserInfo retTimeLineUserInfo = new TimeLineUserInfo();
		JSONObject friendTimeLineJsonObject = vJSONObject;
		try {
			retTimeLineUserInfo.text = friendTimeLineJsonObject.getString("text");
			retTimeLineUserInfo.weiboId = friendTimeLineJsonObject.getString("id");
			if(friendTimeLineJsonObject.has("thumbnail_pic"))
				retTimeLineUserInfo.thumbnail_pic = friendTimeLineJsonObject.getString("thumbnail_pic");
			if(friendTimeLineJsonObject.has("bmiddle_pic"))
				retTimeLineUserInfo.bmiddle_pic = friendTimeLineJsonObject.getString("bmiddle_pic");
			JSONObject userJSONObject = friendTimeLineJsonObject.getJSONObject("user");
			retTimeLineUserInfo.profile_image_url = userJSONObject.getString("profile_image_url");
			retTimeLineUserInfo.name = userJSONObject.getString("name");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggerUtils.e("Parse Time Line User Info encounter error:" + e.getMessage());
		}
		return retTimeLineUserInfo;
	}
	
	TimeLineUserInfo GetCompleteTimeLineUserInfoFromJasonObject(JSONObject vJSONObject) {
		TimeLineUserInfo retTimeLineUserInfo = GetTimeLineUserInfoFromJasonObject(vJSONObject);
		if(vJSONObject.has("retweeted_status")){
			try {
				retTimeLineUserInfo.retweetUserInfo = GetTimeLineUserInfoFromJasonObject(vJSONObject.getJSONObject("retweeted_status"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retTimeLineUserInfo;
	}
	
	public Object onHandleGetTimeLineUserInfoFromJasonObject(EventArg vEventArg) {
		JSONObject friendTimeLineJsonObject = (JSONObject)vEventArg.getUserInfo();
		return GetCompleteTimeLineUserInfoFromJasonObject(friendTimeLineJsonObject);
	}
}
