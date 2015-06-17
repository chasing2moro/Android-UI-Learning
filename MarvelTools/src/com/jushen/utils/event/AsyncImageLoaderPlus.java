package com.jushen.utils.event;

import java.util.ArrayList;
import java.util.List;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.Facade;
import com.jushen.utils.AsyncImageLoader;
import com.jushen.utils.log.LoggerUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;


public class AsyncImageLoaderPlus extends AsyncImageLoader{
	List<String> _DownloadingUrls;
	public String m_EventNameReqLoadImage;
	public String m_EventNameRepLoadImage;
	public AsyncImageLoaderPlus(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initDownloadingUrl();
	}
	
	void initDownloadingUrl(){
		_DownloadingUrls = new ArrayList<String>();
	}
	
	@Override
	protected void onDownloadWithUrl(String vImageUrl){
		if(_DownloadingUrls.contains(vImageUrl)){
			//正在下载的话，就跳过
			return;
		}
		
		_DownloadingUrls.add(vImageUrl);
		if(_DownloadingUrls.size() > 1){
			//LoggerUtils.i("download hang:" + vImageUrl);
			return;
		}
		
		LoggerUtils.i("download start:" + vImageUrl);
		super.onDownloadWithUrl(vImageUrl);
	}
	
	@Override
	protected void onDownloadFinish(String vUrl, Bitmap vResult) {
		if(!_DownloadingUrls.remove(vUrl)){
			LoggerUtils.e("failed to remove :" + vUrl);
		}
		if(vResult == null){
			_DownloadingUrls.add(vUrl);//加回队列，继续下载
		}
		LoggerUtils.i("download finish :" + vUrl + " haveResult:" + (vResult != null));
		sendEvent(EventName.AsyncImageLoaderPlus_DownloadImageRep, null);
		downloadNext();
	}

	void downloadNext(){
		
		if(_DownloadingUrls.size() > 0){
			String tmp = _DownloadingUrls.get(0);
			LoggerUtils.i("download start:" + tmp);
			super.onDownloadWithUrl(tmp);
		}
	}
	
	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		super.onRegistEvent();
		registEvent(EventName.AsyncImageLoaderPlus_DownloadImageReq, "onHandleLoadImage");
	}
	
	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		super.onUnRegistEvent();
		registEvent(EventName.AsyncImageLoaderPlus_DownloadImageReq, "onHandleLoadImage");
	}
	
	public Object onHandleLoadImage(EventArg vEventArg){
		String imageUrl = vEventArg.getString("imageUrl");
		if(TextUtils.isEmpty(imageUrl)){
			LoggerUtils.e("imageUrl is null");
			return null;
		}
		return loadImage(imageUrl);
	}
}
