package com.jushencompany.marveltools.activity;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.utils.JushenDownloadUtils;
import com.jushen.utils.JushenViewUtils;
import com.jushen.utils.log.LoggerUtils;
import com.jushen.widget.ActivitySlideOut;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.R.id;
import com.jushencompany.marveltools.R.layout;
import com.jushencompany.marveltools.R.menu;
import com.jushencompany.marveltools.model.TimeLineUserInfo;
import com.jushencompany.marveltools.model.TimeLineUserInfoManager;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class DetailFriendTimelineActivity extends ActivitySlideOut {

	private DetailFriendTimelineView _detailFriendTimelineView;
	private int _index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_friend_timeline);
		
		_detailFriendTimelineView = new DetailFriendTimelineView();
		_detailFriendTimelineView.init(this);
		
		_index = getIntent().getIntExtra("index", 0);
		show();
	}
	
	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		super.onRegistEvent();
		registEvent(EventName.AsyncImageLoaderPlus_DownloadImageRep, "OnDownloadImageFinish");
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		super.onUnRegistEvent();
		unRegistEvent(EventName.AsyncImageLoaderPlus_DownloadImageRep, "OnDownloadImageFinish");
	}
	
	void show(){
	       // _detailFriendTimelineView.mImageViewPortrait.setBackgroundResource((Integer)mData.get(position).get("img"));
        TimeLineUserInfo aTimeLineUserInfo = TimeLineUserInfoManager.singleton().getTimeLineUserInfoByIndex(_index);
        if(aTimeLineUserInfo == null) return;
        _detailFriendTimelineView.m_TextViewName.setText(aTimeLineUserInfo.name);
        _detailFriendTimelineView.m_TextViewText.setText(aTimeLineUserInfo.text);
    
        if(JushenViewUtils.setVisiblityOtherwiseGone(aTimeLineUserInfo.hasPic(), _detailFriendTimelineView.m_ImageViewContent)){
        	JushenDownloadUtils.downloadImage(_detailFriendTimelineView.m_ImageViewContent, 
            		aTimeLineUserInfo.bmiddle_pic, 
            		R.color.light_gray);
        }
        

        if(JushenViewUtils.setVisiblityOtherwiseGone(aTimeLineUserInfo.hasRetweet(),  _detailFriendTimelineView.m_RetweetRoot)){
        	String styledText = "@" + aTimeLineUserInfo.retweetUserInfo.name + ":" + aTimeLineUserInfo.retweetUserInfo.text;
        	

				_detailFriendTimelineView.m_TextViewRetweetText.setText(styledText);

			
			
        	
        	if(JushenViewUtils.setVisiblityOtherwiseGone(aTimeLineUserInfo.retweetUserInfo.hasPic(), _detailFriendTimelineView.m_ImageViewRetweetContent)){
        		JushenDownloadUtils.downloadImage(_detailFriendTimelineView.m_ImageViewRetweetContent, 
                		aTimeLineUserInfo.retweetUserInfo.thumbnail_pic, 
                		R.color.light_gray);
        	}
        }
        

      JushenDownloadUtils.downloadImage(_detailFriendTimelineView.m_ImageViewPortrait, aTimeLineUserInfo.profile_image_url, R.drawable.default_profile);
 
	}
	
	public Object OnDownloadImageFinish(EventArg vEventArg){
		show();
		return null;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_friend_timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.textView__detail_friend_timeline__name) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
