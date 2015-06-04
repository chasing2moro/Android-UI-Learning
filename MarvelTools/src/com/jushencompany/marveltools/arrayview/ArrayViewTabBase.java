package com.jushencompany.marveltools.arrayview;

import android.view.View;
import android.widget.ImageButton;

public class ArrayViewTabBase {
	private ImageButton _ImageButtonTab;
	public ImageButton getImageButton() {
		return _ImageButtonTab;
	}
	public void setImageButton(ImageButton vImageButton) {
		_ImageButtonTab = vImageButton;
	}
	
    private	View _rootView;
	public void setRootView(View vView){
		_rootView = vView;
	}
	public View getRootView() {
		return _rootView;
	}
}