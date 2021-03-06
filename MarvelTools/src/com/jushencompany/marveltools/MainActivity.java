package com.jushencompany.marveltools;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.activity.DetailFriendTimelineActivity;
import com.jushencompany.marveltools.arrayview.main.*;
import com.jushencompany.marveltools.arrayview.maintab.MainArrayViewTabBase;
import com.jushencompany.marveltools.arrayview.maintab.MainArrayViewTabManager;
import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.Facade;
import com.jushen.utils.Incubate;
import com.jushen.utils.log.LogController;
import com.jushen.utils.log.LoggerUtils;
import com.jushen.web.socket.NetClient;
import com.jushen.web.socket.NetServer;
import com.jushen.web.socket.SocketClient;
import com.jushen.widget.Activity;
import com.jushen.widget.LogLayout;

import android.R.integer;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.usage.UsageEvents.Event;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity implements OnClickListener , Serializable{

	private static MainActivity _instance;
	public static MainActivity singleton() {
		return _instance;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1035792375506237537L;

	final public static String TAG = "com.jushencompany.marveltools";

	private FragmentManager _fragmentManager;
	private MainArrayViewTabManager _mainArrayViewTabManager;
	private MainArrayViewManager _mainArrayViewManager; 

	private MainArrayViewType _oldViewType = MainArrayViewType.maxCount;
//	private MainArraySubViewCommunity _MainArraySubViewCommunity;
//	private MainArraySubViewMarvelTools _MainArraySubViewMarvelTools;
//	private MainArraySubViewDiscovery _MainArraySubViewDiscovery;
//	private MainArraySubViewAboutMe _MainArraySubViewAboutMe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_instance = MainActivity.this;
		
		MainEntrance.singleton().Main(this);
	
		_mainArrayViewTabManager = MainArrayViewTabManager.singleton();
		_mainArrayViewManager = MainArrayViewManager.singleton();
		_fragmentManager = getFragmentManager();
		
		initTabView();
		
		setTabSelection(0);
	}

	private void setTabSelection(int index)
	{
		MainArrayViewType viewType = MainArrayViewType.values()[index];
		if(_oldViewType == viewType){
			return;
		}
		resetTabViewBtn();
		FragmentTransaction transaction = _fragmentManager.beginTransaction();
		hideFragments(transaction);
		
		
		
		MainArrayViewTabBase mainArrayViewTabBase = _mainArrayViewTabManager.getMainArrayViewTab(viewType);
		ImageButton tabImButton = mainArrayViewTabBase.getImageButton();
		switch (viewType)
		{
		case community:
			tabImButton.setImageResource(R.drawable.tab_community_pressed);
			break;
		case marveltools:
			tabImButton.setImageResource(R.drawable.tab_marveltools_pressed);
			break;
		case discovery:
			tabImButton.setImageResource(R.drawable.tab_discovery_pressed);
			break;
		case about_me:
			tabImButton.setImageResource(R.drawable.tab_about_me_pressed);
			break;
		default:
			LoggerUtils.e("unhandle type:" + index);
			//mainArrayViewType = MainArrayViewType.community;
			break;
		}
		
		
		MainArrayViewBase mainArrayView = _mainArrayViewManager.getRawMainArrayView(viewType);
		if (mainArrayView == null)
		{
			mainArrayView = _mainArrayViewManager.getMainArrayView(viewType);
			transaction.add(R.id.id_content, mainArrayView);
		} else
		{
			LoggerUtils.i(
			" isAdd:" + mainArrayView.isAdded()
			+ " isDetached:" + mainArrayView.isDetached()
			+ " isHidden:" + mainArrayView.isHidden()
			+ " isRemoving:" + mainArrayView.isRemoving()
			+ " isVisible:" + mainArrayView.isVisible()
			);
			if(!mainArrayView.isAdded()){
				LoggerUtils.w("" + mainArrayView + " has been removed, try to reAdd");
				transaction.add(R.id.id_content, mainArrayView);
			}
			transaction.show(mainArrayView);
		}
		
		transaction.commit();
		
		mainArrayView.selected();
		
		_oldViewType = viewType;
	}

	MainArrayViewTabBase _viewTabCommunity;
	MainArrayViewTabBase _viewTabMarvelToos;
	MainArrayViewTabBase _viewTabDiscovery;
	MainArrayViewTabBase _viewTabAboutMe;
	private void initTabView() {
		_viewTabCommunity = _mainArrayViewTabManager.getMainArrayViewTab(MainArrayViewType.community);
		findViewById(R.id.id_tab_bottom_community).setOnClickListener(this);
		_viewTabCommunity.setImageButton((ImageButton)findViewById(R.id.imagebutton_tab_bottom_community));
		//_viewTabCommunity.getImageButton().setOnClickListener(this);
		
		_viewTabMarvelToos = _mainArrayViewTabManager.getMainArrayViewTab(MainArrayViewType.marveltools);
		findViewById(R.id.id_tab_bottom_marvel_tools).setOnClickListener(this);
		_viewTabMarvelToos.setImageButton((ImageButton)findViewById(R.id.imagebutton_tab_bottom_marvel_tools));
		//_viewTabMarvelToos.getImageButton().setOnClickListener(this);
		
		_viewTabDiscovery = _mainArrayViewTabManager.getMainArrayViewTab(MainArrayViewType.discovery);
		findViewById(R.id.id_tab_bottom_discovery).setOnClickListener(this);
		_viewTabDiscovery.setImageButton((ImageButton)findViewById(R.id.imagebutton_tab_bottom_discovery));
		//_viewTabDiscovery.getImageButton().setOnClickListener(this);
		
		_viewTabAboutMe = _mainArrayViewTabManager.getMainArrayViewTab(MainArrayViewType.about_me);
		findViewById(R.id.id_tab_bottom_about_me).setOnClickListener(this);
		_viewTabAboutMe.setImageButton((ImageButton)findViewById(R.id.imagebutton_tab_bottom_about_me));
		//_viewTabAboutMe.getImageButton().setOnClickListener(this);
	}

	
	private void resetTabViewBtn()
	{
		_viewTabCommunity.getImageButton().setImageResource(R.drawable.tab_community_normal);
		_viewTabMarvelToos.getImageButton().setImageResource(R.drawable.tab_marveltools_normal);
		_viewTabDiscovery.getImageButton().setImageResource(R.drawable.tab_discovery_normal);
		_viewTabAboutMe.getImageButton().setImageResource(R.drawable.tab_about_me_normal);
	}

	private static int s_maxCount = 4;
	private void hideFragments(FragmentTransaction transaction)
	{
//		MainArrayViewBase mainArrayView;
//		for (int i = 0; i < s_maxCount; i++) {
//			mainArrayView = _mainArrayViewManager.getRawMainArrayView(MainArrayViewType.values()[i]);
//			if(mainArrayView != null){
//				transaction.hide(mainArrayView);
//			}
//		}
		if(oldViewTypeIsValidate()){
			MainArrayViewBase mainArrayView = _mainArrayViewManager.getRawMainArrayView(_oldViewType);
			if(mainArrayView != null){
				transaction.hide(mainArrayView);
				mainArrayView.unSelected();
			}else{
				LoggerUtils.e("Can not find fragment by " + _oldViewType);
			}
		}
	}
	
	boolean oldViewTypeIsValidate(){
		return _oldViewType != MainArrayViewType.maxCount;
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LoggerUtils.i("onActivityResult : MainActivity");
		//WBAuthor.singleton().onActivityResult(requestCode, resultCode, data);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		
		int id = item.getItemId();
		switch (id) {
		case R.id.textView__detail_friend_timeline__name:
			
			break;
		case R.id.action_showLog:
			sendEvent(EventName.LogController_Show, null);
			break;
		case R.id.action_test:
			testRotate();
			break;
//			Intent aIntent = new Intent();
//			aIntent.setClass(this, DetailFriendTimelineActivity.class);
//			aIntent.putExtra("index", 0);
//			startActivity(aIntent);
//			sendEvent(EventName.CommonUtils_ActivitySlideIn, EventArg.Create().setUserInfo(this));
//			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        
        EventArg aEventArg = new EventArg();
        aEventArg.putInt("top", rect.top);
        sendEvent(EventName.LogController_ChangeTop, aEventArg);
        //LogController.singleton().setTop(rect.top);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_tab_bottom_community:
			setTabSelection(0);
			break;
		case R.id.id_tab_bottom_marvel_tools:
			setTabSelection(1);
			break;
		case R.id.id_tab_bottom_discovery:
			setTabSelection(2);
			break;
		case R.id.id_tab_bottom_about_me:
			setTabSelection(3);
			break;
		default:
			LoggerUtils.e("unhandle error:" + v.getId());
			break;
		}
//		switch (v.getId()) {
//		case R.id.imagebutton_tab_bottom_community:
//			setTabSelection(0);
//			break;
//		case R.id.imagebutton_tab_bottom_marvel_tools:
//			setTabSelection(1);
//			break;
//		case R.id.imagebutton_tab_bottom_discovery:
//			setTabSelection(2);
//			break;
//		case R.id.imagebutton_tab_bottom_about_me:
//			setTabSelection(3);
//			break;
//		default:
//			LoggerUtils.e("unhandle error:" + v.getId());
//			break;
//		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LoggerUtils.i("Activity Resume");
	}
	
	long preTime;  
	public static final long TWO_SECOND = 2 * 1000; 
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // 截获后退键  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            long currentTime = new Date().getTime();  
  
            // 如果时间间隔大于2秒, 不处理  
            if ((currentTime - preTime) > TWO_SECOND) { 
            	sendEvent(EventName.CommonUtils_ToastShow,
            			EventArg.Create().setUserInfo(this).putString("text", getResources().getString(R.string.tip_press_back_again_to_quit)));

                // 更新时间  
                preTime = currentTime;  
  
                // 截获事件,不再处理  
                return true;  
            }else {
                finish();  
                System.exit(0); 
			}
        }  
  
        return super.onKeyDown(keyCode, event);  
    }  
    
    void testRotate(){
             // TODO Auto-generated method stub
            
             //创建ProgressDialog对象
    	android.app.ProgressDialog m_pDialog = new android.app.ProgressDialog(getApplicationContext());

             // 设置进度条风格，风格为圆形，旋转的
             m_pDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);

             // 设置ProgressDialog 标题
            // m_pDialog.setTitle("提示");
            
             // 设置ProgressDialog 提示信息
             m_pDialog.setMessage("Loading...");

             // 设置ProgressDialog 标题图标
            // m_pDialog.setIcon(R.drawable.icoin_add);

             // 设置ProgressDialog 的进度条是否不明确
             m_pDialog.setIndeterminate(true);
            
             // 设置ProgressDialog 是否可以按退回按键取消
             m_pDialog.setCancelable(true);

             // 让ProgressDialog显示
             m_pDialog.show();
             
             m_pDialog = new android.app.ProgressDialog(this);

             // 设置进度条风格，风格为圆形，旋转的
             m_pDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);

             // 设置ProgressDialog 标题
            // m_pDialog.setTitle("提示");
            
             // 设置ProgressDialog 提示信息
             m_pDialog.setMessage("Loading...");

             // 设置ProgressDialog 标题图标
            // m_pDialog.setIcon(R.drawable.icoin_add);

             // 设置ProgressDialog 的进度条是否不明确
             m_pDialog.setIndeterminate(true);
            
             // 设置ProgressDialog 是否可以按退回按键取消
             m_pDialog.setCancelable(true);

             // 让ProgressDialog显示
             m_pDialog.show();
         }
    
}
