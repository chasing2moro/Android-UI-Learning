package com.jushen.utils.event;

import android.app.Activity;
import android.content.Context;

import com.jushen.framework.event.Controller;
import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;

public class ViewUtils extends Controller {
	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		registEvent(EventName.ViewUtils_Rotate_Hint_Show, "onHandleRotateHintShow");
		registEvent(EventName.ViewUtils_Rotate_Hint_Hide, "onHandleRotateHintHide");
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		unRegistEvent(EventName.ViewUtils_Rotate_Hint_Show, "onHandleRotateHintShow");
		unRegistEvent(EventName.ViewUtils_Rotate_Hint_Hide, "onHandleRotateHintHide");
	}
	

	public Object onHandleRotateHintShow(EventArg vEventArg) {
		
		Context aContext = (Context)vEventArg.getUserInfo();
        //创建ProgressDialog对象
		android.app.ProgressDialog m_pDialog = new android.app.ProgressDialog(aContext);

        // 设置进度条风格，风格为圆形，旋转的
        m_pDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);

        // 设置ProgressDialog 标题
       // m_pDialog.setTitle("提示");
       
        // 设置ProgressDialog 提示信息
        String text = vEventArg.getString("text", "Loading...");
        m_pDialog.setMessage(text);

        // 设置ProgressDialog 标题图标
       // m_pDialog.setIcon(R.drawable.icoin_add);

        // 设置ProgressDialog 的进度条是否不明确
        m_pDialog.setIndeterminate(true);
       
        // 设置ProgressDialog 是否可以按退回按键取消
        m_pDialog.setCancelable(true);

        // 让ProgressDialog显示
        m_pDialog.show();

		return m_pDialog;
	}
	
	public Object onHandleRotateHintHide(EventArg vEventArg) {

		android.app.ProgressDialog m_pDialog = (android.app.ProgressDialog) vEventArg.getUserInfo();
		//m_pDialog.hide();
		if(m_pDialog != null){
			if(m_pDialog.isShowing()){
				m_pDialog.dismiss();
			}else{
				LoggerUtils.i("rotate hint aleady hide");
			}
		}
		return null;
	}
}
