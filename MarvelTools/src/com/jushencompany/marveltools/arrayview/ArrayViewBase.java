package com.jushencompany.marveltools.arrayview;

import com.jushen.utils.log.LoggerUtils;

import android.R.bool;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ArrayViewBase extends com.jushen.widget.Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View aView = inflater.inflate(obtainInflateLayout(), container, false);
		onViewCreated(aView);
		return aView;
	}
	
	
	protected abstract int obtainInflateLayout();
	protected void onViewCreated(View vRootView)
	{
		//LoggerUtils.e("U should not implement this method directly");
	}
	
	boolean _visible = false;
	public boolean getIsVisible() {
		return _visible;
	}
	public void selected() {
		_visible = true;
		onSelected();
	}
	protected void onSelected() {
		LoggerUtils.i("onSelected :" + getClass().toString() + " Activity:" + getActivity());
	}
	
	public void unSelected() {
		_visible = false;
		onUnSelected();
	}
	protected void onUnSelected() {
		//LoggerUtils.i("onUnSelected :" + this.toString());
	}
	
	@Override
    public void onResume() {
        super.onResume();
        if(_visible == true)
        	onResumeWhenVisible();
    }
	protected void onResumeWhenVisible() {
		//LoggerUtils.i("onResumeWhenVisible:" + this.toString());
	}
	
	@Override
	public void onPause() {
		super.onPause();
        if(_visible == true)
        	onPauseWhenVisible();
	};
	protected void onPauseWhenVisible() {
		//LoggerUtils.i("onPauseWhenVisible:" + this.toString());
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		LoggerUtils.i("onAttach");
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		LoggerUtils.i("onDetach");
	}
	
}
