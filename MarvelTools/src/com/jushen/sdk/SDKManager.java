package com.jushen.sdk;

import com.jushen.framework.commondef.IntentAction;
import com.jushen.utils.log.LoggerUtils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import com.jushen.framework.event.Controller;

public class SDKManager extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		LoggerUtils.i("SDKManager receive msg");
//https://api.weibo.com/2/statuses/friends_timeline.json?source=2045436852&access_token=2.00IANCxBBvfvYDfe76479cc90PNvO3		
		if(action.equals(IntentAction.SDKRequestLogin)){

		}
	}
	
}
