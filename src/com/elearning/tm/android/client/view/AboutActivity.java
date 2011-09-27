package com.elearning.tm.android.client.view;

import android.os.Bundle;
import android.view.Window;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class AboutActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_view);
		

	}
}
