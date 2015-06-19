package com.jushencompany.marveltools.arrayview.main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jushen.utils.log.LogController;
import com.jushencompany.marveltools.R;

public class MainArraySubViewDiscovery extends MainArrayViewBase{

	@Override
	protected MainArrayViewType obtainViewType() {
		// TODO Auto-generated method stub
		return MainArrayViewType.discovery;
	}

	@Override
	protected int obtainInflateLayout() {
		// TODO Auto-generated method stub
		return R.layout.main_tab_discovery;
	}
	
	EditText _editTextCommand;
	@Override
	protected void onViewCreated(View vRootView)
	{
		
		_editTextCommand = (EditText)vRootView.findViewById(R.id.editText__discovery__command);
		((Button)vRootView.findViewById(R.id.button__discovery__submit)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogController.singleton().OnConsoleButtonClicked(_editTextCommand.getText().toString());
			}
			
		});
	}

}
