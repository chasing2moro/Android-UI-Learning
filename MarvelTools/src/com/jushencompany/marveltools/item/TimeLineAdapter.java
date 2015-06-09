package com.jushencompany.marveltools.item;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.Facade;
import com.jushen.utils.AsyncImageLoaderPlus;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.model.TimeLineUserInfo;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeLineAdapter extends BaseAdapter{
	private List<TimeLineUserInfo> mData;
    private LayoutInflater mInflater;
     
//    public TimeLineAdapter(Context context){
//        this(context, null);
//    }
    
    public TimeLineAdapter(Context context, List<TimeLineUserInfo> vData){
        this.mInflater = LayoutInflater.from(context);
        mData = vData;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         
    	TimeLineItem holder = null;
        if (convertView == null) {
            holder=new TimeLineItem();  
             
            convertView = mInflater.inflate(R.layout.item_friends_timeline, null);
            holder.m_ImageViewPortrait = (ImageView)convertView.findViewById(R.id.imageview__friends_timeline__portrait);
            holder.m_TextViewText = (TextView)convertView.findViewById(R.id.textview__friends_timeline__text);
            holder.m_TextViewName = (TextView)convertView.findViewById(R.id.textView_friends_timeline__name);
            holder.m_ImageViewContent = (ImageView)convertView.findViewById(R.id.imageView__friends_timeline__textcontent);
           
            holder.m_RetweetRoot = convertView.findViewById(R.id.view__friends_timeline__retweet_root);
            holder.m_TextViewRetweetText = (TextView)convertView.findViewById(R.id.textView__friends_timeline__retweet_text);
            holder.m_TextViewRetweetText.setFocusable(false);
            holder.m_ImageViewRetweetContent = (ImageView)convertView.findViewById(R.id.imageView__friends_timeline__retweet_textcontent);
            convertView.setTag(holder);
        }else {
            holder = (TimeLineItem)convertView.getTag();
        }
         
         
       // holder.mImageViewPortrait.setBackgroundResource((Integer)mData.get(position).get("img"));
        TimeLineUserInfo aTimeLineUserInfo = mData.get(position);
        holder.m_TextViewName.setText(aTimeLineUserInfo.name);
        holder.m_TextViewText.setText(aTimeLineUserInfo.text);
    
        if(_setVisiblity(aTimeLineUserInfo.hasPic(), holder.m_ImageViewContent)){
        	_downloadImage(holder.m_ImageViewContent, 
            		aTimeLineUserInfo.thumbnail_pic, 
            		R.color.light_gray);
        }
        

        if(_setVisiblity(aTimeLineUserInfo.hasRetweet(),  holder.m_RetweetRoot)){
        	holder.m_TextViewRetweetText.setText("@" + aTimeLineUserInfo.retweetUserInfo.name + ":" + aTimeLineUserInfo.retweetUserInfo.text);
        	if(_setVisiblity(aTimeLineUserInfo.retweetUserInfo.hasPic(), holder.m_ImageViewRetweetContent)){
        		_downloadImage(holder.m_ImageViewRetweetContent, 
                		aTimeLineUserInfo.retweetUserInfo.thumbnail_pic, 
                		R.color.light_gray);
        	}
        }
        

//        
//        EventArg aEventArg = new EventArg();
//        aEventArg.putString("imageUrl", aTimeLineUserInfo.profile_image_url);
//        Bitmap profileBitmap = (Bitmap)Facade.singleton().sendEvent(EventName.AsyncImageLoaderPlus_DownloadProfileImageReq, aEventArg);
//        if(profileBitmap == null){
//        	holder.m_ImageViewPortrait.setImageResource(R.drawable.default_profile);
//        }else {
//        	holder.m_ImageViewPortrait.setImageBitmap(profileBitmap);
//		}
        
        _downloadImage(holder.m_ImageViewPortrait, aTimeLineUserInfo.profile_image_url, R.drawable.default_profile);
        return convertView;
    }
    
    static void _downloadImage(ImageView vImageView, String vUrl, int vDefaultResource){
        EventArg aEventArg = new EventArg();
        aEventArg.putString("imageUrl", vUrl);
        Bitmap profileBitmap = (Bitmap)Facade.singleton().sendEvent(EventName.AsyncImageLoaderPlus_DownloadProfileImageReq, aEventArg);
        if(profileBitmap == null){
        	vImageView.setImageResource(vDefaultResource);
        }else {
        	vImageView.setImageBitmap(profileBitmap);
		}
    }
    
    static boolean _setVisiblity(boolean vVisible, View vView){
    	int picVisibility;
        if(vVisible == true){
        	picVisibility = View.VISIBLE;
        }else {
        	picVisibility = View.GONE;
		}
        vView.setVisibility(picVisibility);
        return vVisible;
    }
     
    @Override
    public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	} 
}