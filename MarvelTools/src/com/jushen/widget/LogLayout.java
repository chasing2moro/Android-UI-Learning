package com.jushen.widget;

import com.jushencompany.marveltools.R;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogLayout extends LinearLayout {
	public TextView m_TextView;
	public Button m_SlideButton;
	
	public EditText m_EditTextConsole;
	public Button m_ButtonConsole;

    public LogLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);// ˮƽ����
        

        //���ÿ��
        this.setLayoutParams( 
        		new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        		);
        
        View view = LayoutInflater.from(context).inflate(R.layout.log_layout, null); 
        m_TextView = (TextView)view.findViewById(R.id.textView_log);
        
        m_TextView.setMovementMethod(ScrollingMovementMethod.getInstance()); //Android��������ʾ��صĿ��Բ鿴SDK��android.text.method��֧�˽����
        m_TextView.setEllipsize(TruncateAt.END);
        
        m_SlideButton = (Button)view.findViewById(R.id.button_log_slide);

        m_EditTextConsole = (EditText)view.findViewById(R.id.editText_log_console);
        m_ButtonConsole = (Button)view.findViewById(R.id.button_log_console);
        this.addView(view);
    }
}