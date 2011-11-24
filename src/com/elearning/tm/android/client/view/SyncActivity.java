package com.elearning.tm.android.client.view;

import android.os.Bundle;
import android.view.Window;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;
import com.elearning.tm.android.client.view.module.NavBar;


public class SyncActivity extends BaseActivity {
	NavBar mNavbar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_syn_view);
		mNavbar = new NavBar(NavBar.HEADER_STYLE_BACK, this);
		mNavbar.setHeaderTitle("数据同步");
	}
}
