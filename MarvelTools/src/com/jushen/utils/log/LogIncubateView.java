package com.jushen.utils.log;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

import com.jushen.framework.event.Controller;
import com.jushen.widget.LogLayout;

public class LogIncubateView extends Controller implements View.OnClickListener{
	  private WindowManager mWindowManager;
	    private WindowManager.LayoutParams mLayoutParams;
	    protected LogLayout mLogLayout;
	    private long startTime;
	    protected boolean _isVisible = false;
	    protected Handler _Handler;

	    float x, y;
	    int top;
	    public int getTop() {
			return top;
		}
	    public void setTop(int vTop) {
			top = vTop;
		}
	    
	    
	    Context _Context;
	    
	    public void Init(Context vContext){//getApplicationContext()
	    	if(_Context == null){
	    		_Context = vContext;
	    		createWindowManager();
	    		createDesktopLayout();
	    		createHandler();
	    	}
	    }
	    
	    /**
	     * ������������
	     */
	    void createDesktopLayout() {
	        mLogLayout = new LogLayout(_Context);
	        mLogLayout.m_SlideButton.setText("Clear");
	        mLogLayout.m_SlideButton.setOnClickListener(this);
	        mLogLayout.setOnTouchListener(new OnTouchListener() {
	            float mTouchStartX;
	            float mTouchStartY;
	            
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                x = event.getRawX();
	                y = event.getRawY() ;
	                switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                    mTouchStartX = event.getRawX();
	                    mTouchStartY = event.getRawY();

	                    if (System.currentTimeMillis() - startTime < 200) {
	                        closeLog();
	                    }
	                    startTime = System.currentTimeMillis();
	                    break;
	                case MotionEvent.ACTION_MOVE:
	                    // ���¸�������λ�ò���
	                    mLayoutParams.x += (int) (x - mTouchStartX);
	                    mLayoutParams.y += (int) (y - mTouchStartY);
	                    mWindowManager.updateViewLayout(mLogLayout, mLayoutParams);
	                    mTouchStartX = x;
	                    mTouchStartY = y;
	                    break;
	                case MotionEvent.ACTION_UP:

	                    // ���¸�������λ�ò���
	                    mLayoutParams.x += (int) (x - mTouchStartX);
	                    mLayoutParams.y += (int) (y - mTouchStartY);
	                    mWindowManager.updateViewLayout(mLogLayout, mLayoutParams);

	                    mTouchStartX = mTouchStartY = 0;
	                    break;
	                }
	                return false;
	            }
	        });
	    }

//	    @Override
//	    public void onWindowFocusChanged(boolean hasFocus) {
//	        super.onWindowFocusChanged(hasFocus);
//	        Rect rect = new Rect();
//	        // /ȡ��������ͼ����,ע�⣬�����Ҫ���ñ�����ʽ�������������ڱ�����ʽ֮�󣬷�������
//	        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//	        top = rect.top;//״̬���ĸ߶ȣ�����rect.height,rect.width�ֱ���ϵͳ�ĸ߶ȵĿ��
	//
//	        Log.i("top",""+top);
//	    }

	    /**
	     * ��ʾDesktopLayout
	     */
	    boolean _createdTag = false;
	    public void showLog() {
	    	_isVisible = true;
	    	if(_createdTag == false){
	    		mWindowManager.addView(mLogLayout, mLayoutParams);
	    		_createdTag = true;
	    	}else {
				mLogLayout.setVisibility(View.VISIBLE);
			}
	    }

	    /**
	     * �ر�DesktopLayout
	     */
	    public void closeLog() {
	    	_isVisible = false;
	    	mLogLayout.setVisibility(View.INVISIBLE);
	    	LoggerUtils.e("close");
	    }
	    

		public void setText(String vMsg){
			if(_isVisible == true){
				Message msg = new Message();
				msg.obj = vMsg;
				_Handler.sendMessage(msg);
			}
		}
		
		public void setTextInHandler(String vMsg){
			mLogLayout.m_TextView.setText(Html.fromHtml( vMsg ));
		}


	    /**
	     * ����WindowManager
	     */
	    void createWindowManager() {
	        // ȡ��ϵͳ����
	        mWindowManager = (WindowManager)_Context.getSystemService("window");

	        // ����Ĳ�����ʽ
	        mLayoutParams = new WindowManager.LayoutParams();

	        // ���ô�����ʾ���͡���TYPE_SYSTEM_ALERT(ϵͳ��ʾ)
	        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

	        // ���ô��役�㼰������
	        // FLAG_NOT_FOCUSABLE(���ܻ�ð������뽹��)
	        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//FLAG_ALT_FOCUSABLE_IM;//

	        // ������ʾ��ģʽ
	        mLayoutParams.format = PixelFormat.RGBA_8888;

	        // ���ö���ķ���
	        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

	        // ���ô����Ⱥ͸߶�
	        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
	        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    }
	    
	    void createHandler(){
	    	_Handler = new Handler(Looper.getMainLooper()){
	    		public void handleMessage(Message msg)
	    		{
	    			super.handleMessage(msg);
	    			setTextInHandler((String)msg.obj);
	    		}
	    	};
	    }
	    
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == mLogLayout.m_SlideButton){
				ClearButtnClicked();
			}
		}
		
		protected void ClearButtnClicked(){
			
		}
}
