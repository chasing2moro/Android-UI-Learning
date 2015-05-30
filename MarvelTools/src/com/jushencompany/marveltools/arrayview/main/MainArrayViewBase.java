package com.jushencompany.marveltools.arrayview.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jushencompany.marveltools.arrayview.ArrayViewBase;

public abstract class MainArrayViewBase extends ArrayViewBase{
	protected MainArrayViewType m_viewType;
	public MainArrayViewType getViewType() {
		return m_viewType;
	}
	protected abstract MainArrayViewType obtainViewType();
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		m_viewType = obtainViewType();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	

}
