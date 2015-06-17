package com.jushencompany.marveltools.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jushencompany.marveltools.R;

public class DetailFriendTimelineView {
	public ImageView m_ImageViewPortrait;
	public TextView m_TextViewText;
	public TextView m_TextViewName;
	public ImageView m_ImageViewContent;
	
	public View m_RetweetRoot;
	public TextView m_TextViewRetweetText;
	public ImageView m_ImageViewRetweetContent;
	
	public void init(Activity vActivity){
		m_ImageViewPortrait = (ImageView)vActivity.findViewById(R.id.imageview__detail_friend_timeline__portrait);
        m_TextViewText = (TextView)vActivity.findViewById(R.id.textview__detail_friend_timeline__text);
        m_TextViewName = (TextView)vActivity.findViewById(R.id.textView__detail_friend_timeline__name);
        m_ImageViewContent = (ImageView)vActivity.findViewById(R.id.imageView__detail_friend_timeline__textcontent);
       
        m_RetweetRoot = vActivity.findViewById(R.id.view__detail_friend_timeline__retweet_root);
        m_TextViewRetweetText = (TextView)vActivity.findViewById(R.id.textView__detail_friend_timeline__retweet_text);
        m_ImageViewRetweetContent = (ImageView)vActivity.findViewById(R.id.imageView__detail_friend_timeline__retweet_textcontent);
	}
}
