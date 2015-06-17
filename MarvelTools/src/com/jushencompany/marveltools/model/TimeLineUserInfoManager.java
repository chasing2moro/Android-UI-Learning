package com.jushencompany.marveltools.model;

import java.util.ArrayList;
import java.util.List;

import com.jushen.utils.log.LoggerUtils;

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
	
	public TimeLineUserInfo getTimeLineUserInfoByIndex(int vIndex) {
		TimeLineUserInfo aTimeLineUserInfo=  _DataList.get(vIndex);
		if(aTimeLineUserInfo == null)
			LoggerUtils.e("can not find TimeLineUserInfo, by index:" + vIndex);
		return aTimeLineUserInfo;
	}
	
	public TimeLineUserInfo getTimeLineUserInfoById(String vWeboId) {
		for (int i = 0; i < _DataList.size(); i++) {
			TimeLineUserInfo aTimeLineUserInfo=  _DataList.get(i);
			if(aTimeLineUserInfo.weiboId.equals(vWeboId)){
				return aTimeLineUserInfo;
			}
		}
		LoggerUtils.e("can not find TimeLineUserInfo, by id:" + vWeboId);
		return null;
	}
}
