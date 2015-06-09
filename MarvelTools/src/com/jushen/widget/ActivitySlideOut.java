package com.jushen.widget;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;
import com.jushen.framework.event.Facade;
import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;


public class ActivitySlideOut extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
        	actionBar.setDisplayHomeAsUpEnabled(true);
        }else {
			LoggerUtils.e("actionBar = null");
		}
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	// TODO Auto-generated method stub
    	LoggerUtils.i("featureId:" + featureId +
    			" itemId:" + item.getItemId() +
    			" R.id.home:" + R.id.home +
    			" android.R.id.home:" + android.R.id.home);
    	switch (item.getItemId()) {
		case android.R.id.home:
			_popUpActivity();
			break;

		default:
			break;
		}
    	return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {	
    	if(keyCode==KeyEvent.KEYCODE_BACK && 
    			event.getRepeatCount() == 0){// = 0 means long press
    		_popUpActivity();
    	}

    	return true;
    }
    
    private void _popUpActivity() {
    	finish();
		Facade.singleton().sendEvent(EventName.CommonUtils_ActivitySlideOut, EventArg.Create().setUserInfo(this));
	}
}
