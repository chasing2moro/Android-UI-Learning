package com.jushencompany.marveltools.arrayview.main;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jushen.framework.commondef.IntentAction;
import com.jushen.sdk.weibo.WBAuthActivity;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.MainActivity;
import com.jushencompany.marveltools.R;



public class MainArraySubViewAboutMe extends MainArrayViewBase{
	@Override
	protected MainArrayViewType obtainViewType() {
		// TODO Auto-generated method stub
		return MainArrayViewType.about_me;
	}

	@Override
	protected int obtainInflateLayout() {
		// TODO Auto-generated method stub
		return R.layout.main_tab_about_me;
	}
	
	@Override
	protected void onViewCreated(View vRootView)
	{
		Button loginButton = (Button)vRootView.findViewById(R.id.button__about_me__login);
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(getActivity(), WBAuthActivity.class);
				startActivity(loginIntent);
			}
			
		});
	}

}
