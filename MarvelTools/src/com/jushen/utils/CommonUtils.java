package com.jushen.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import com.jushen.commondef.CommonDef;
import com.jushen.framework.event.Controller;
import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushencompany.marveltools.R;

public class CommonUtils extends Controller{

	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		registEvent(EventName.CommonUtils_ActivitySlideIn, "onHandleActivityAnimSlideIn");
		registEvent(EventName.CommonUtils_ActivitySlideOut, "onHandleActivityAnimSlideOut");
		registEvent(EventName.CommonUtils_ToastShow, "onHandleShowToast");
		registEvent(EventName.CommonUtils_AlertDialogShow, "onHandleAlertDialogShow");
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		unRegistEvent(EventName.CommonUtils_ActivitySlideIn, "onHandleActivityAnimSlideIn");
		unRegistEvent(EventName.CommonUtils_ActivitySlideOut, "onHandleActivityAnimSlideOut");
		unRegistEvent(EventName.CommonUtils_ToastShow, "onHandleShowToast");
		unRegistEvent(EventName.CommonUtils_AlertDialogShow, "onHandleAlertDialogShow");
	}
	
	public Object onHandleActivityAnimSlideIn(EventArg vEventArg) {
		Activity activity = (Activity)vEventArg.getUserInfo();
		activity.overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
		return null;
	}
	
	public Object onHandleActivityAnimSlideOut(EventArg vEventArg) {
		Activity activity = (Activity)vEventArg.getUserInfo();
		activity.overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
		return null;
	}
	
	public Object onHandleShowToast(EventArg vEventArg){
		Context context = (Context)vEventArg.getUserInfo();
		String text = vEventArg.getString("text");
		int duration = vEventArg.getInt("duration", Toast.LENGTH_LONG);
		Toast.makeText(context, 
				text, 
				duration).show();
		return null;
	}
	
	private String _resourcePositiveButtonText;
	private String _resourceNegativeButtonText;
	
	public Object onHandleAlertDialogShow(EventArg vEventArg) {
		Context context = (Context)vEventArg.getUserInfo();
		String title = vEventArg.getString("title", "default_title");
		String message = vEventArg.getString("message");
		
		if(TextUtils.isEmpty(_resourcePositiveButtonText))
			_resourcePositiveButtonText = context.getResources().getString(R.string.tip_ok);
		String positiveButtonText = vEventArg.getString("positiveButtonText", _resourcePositiveButtonText);
		
		if(TextUtils.isEmpty(_resourceNegativeButtonText))
			_resourceNegativeButtonText = context.getResources().getString(R.string.tip_cancel);
		String negativeButtonText = vEventArg.getString("negativeButtonText", _resourceNegativeButtonText);
				
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);

		DialogInterface.OnClickListener listener = (DialogInterface.OnClickListener)vEventArg.getUserInfo("listener");
		builder.setPositiveButton(positiveButtonText, listener);
		builder.setNegativeButton(negativeButtonText, listener);
		builder.show(); 
		return null;
	}
}
