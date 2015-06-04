package com.jushencompany.marveltools;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jushencompany.marveltools.R;
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
		case R.id.action_settings:
			
			break;
		case R.id.action_showLog:
			sendEvent(EventName.LogController_Show, null);
			break;
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
}
