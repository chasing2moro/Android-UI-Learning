package com.jushencompany.marveltools.model;

import java.util.ArrayList;
import java.util.List;

public class TimeLineUserInfoManager {
	private static TimeLineUserInfoManager _Instance;
	public static TimeLineUserInfoManager singleton() {
		if (_Instance == null) {
			_Instance = new TimeLineUserInfoManager();
		}
		return _Instance;
	}
	
	
	List<TimeLineUserInfo> _DataList;
	public List<TimeLineUserInfo> getDataList(){
		return _DataList;
	}
	
	public void clear() {
		if(_DataList != null)
			_DataList.clear();
	}
	
	public void add(TimeLineUserInfo vTimeLineUserInfo){
		if(_DataList == null)
			_DataList = new ArrayList<TimeLineUserInfo>();
		
		_DataList.add(vTimeLineUserInfo);
	}
}
