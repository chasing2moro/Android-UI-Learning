package com.jushencompany.marveltools.item;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.jushen.utils.AsyncImageLoaderPlus;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.model.TimeLineUserInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeLineAdapter extends BaseAdapter{
	private List<TimeLineUserInfo> mData;
    private LayoutInflater mInflater;
    private AsyncImageLoaderPlus _AsyncImageLoader;
     
//    public TimeLineAdapter(Context context){
//        this(context, null);
//    }
    
    public TimeLineAdapter(Context context, List<TimeLineUserInfo> vData){
        this.mInflater = LayoutInflater.from(context);
        mData = vData;
        _AsyncImageLoader = new AsyncImageLoaderPlus(context);
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
            holder.mImageViewPortrait = (ImageView)convertView.findViewById(R.id.imageview__friends_timeline__portrait);
            holder.mTextViewText = (TextView)convertView.findViewById(R.id.textview__friends_timeline__text);
           holder.mTextViewname = (TextView)convertView.findViewById(R.id.textView_friends_timeline__name);
            convertView.setTag(holder);
        }else {
            holder = (TimeLineItem)convertView.getTag();
        }
         
         
       // holder.mImageViewPortrait.setBackgroundResource((Integer)mData.get(position).get("img"));
        TimeLineUserInfo aTimeLineUserInfo = mData.get(position);
        holder.mTextViewText.setText(aTimeLineUserInfo.text);
        holder.mTextViewname.setText(aTimeLineUserInfo.name);
        Bitmap profileBitmap = _AsyncImageLoader.loadImage(aTimeLineUserInfo.profile_image_url);
        if(profileBitmap == null){
        	//LoggerUtils.i(aTimeLineUserInfo.name + " ("+ position + ") image haven't load");
        	holder.mImageViewPortrait.setImageResource(R.drawable.default_profile);
        }else {
        	//LoggerUtils.i(aTimeLineUserInfo.name + " ("+ position + ") image loaded");
        	holder.mImageViewPortrait.setImageBitmap(profileBitmap);
		}
        return convertView;
    }
     
    @Override
    public void notifyDataSetChanged() {
    	LoggerUtils.i("------------------------");
		super.notifyDataSetChanged();
	} 
}