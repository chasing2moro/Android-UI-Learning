package com.jushen.utils;

import android.view.View;

public class JushenViewUtils {
    public static boolean setVisiblityOtherwiseGone(boolean vVisible, View vView){
    	int picVisibility;
        if(vVisible == true){
        	picVisibility = View.VISIBLE;
        }else {
        	picVisibility = View.GONE;
		}
        vView.setVisibility(picVisibility);
        return vVisible;
    }
}
