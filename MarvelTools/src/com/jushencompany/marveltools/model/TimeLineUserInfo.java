package com.jushencompany.marveltools.model;

import android.text.TextUtils;


public class TimeLineUserInfo{
	public String text;
	public String name;
	
	public String profile_image_url;
	
	public String thumbnail_pic;
	public boolean hasPic() {
		return !TextUtils.isEmpty(thumbnail_pic);
	}
	public String bmiddle_pic;
	
	public TimeLineUserInfo retweetUserInfo = null;
	public boolean hasRetweet(){
		return retweetUserInfo != null;
	}
//	@Override
//	public void setObject(Object vObject) {
//		// TODO Auto-generated method stub
//		profile_image_url_Bitmap = (Bitmap)vObject;
//		LoggerUtils.i("setObject:" + profile_image_url_Bitmap);
//	}
}
