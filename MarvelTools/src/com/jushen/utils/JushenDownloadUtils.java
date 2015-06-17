package com.jushen.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.Facade;

public class JushenDownloadUtils {
    public static void downloadImage(ImageView vImageView, String vUrl, int vDefaultResource){
        EventArg aEventArg = new EventArg();
        aEventArg.putString("imageUrl", vUrl);
        Bitmap profileBitmap = (Bitmap)Facade.singleton().sendEvent(EventName.AsyncImageLoaderPlus_DownloadImageReq, aEventArg);
        if(profileBitmap == null){
        	vImageView.setImageResource(vDefaultResource);
        }else {
        	vImageView.setImageBitmap(profileBitmap);
		}
    }
}
