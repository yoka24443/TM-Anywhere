package com.elearning.tm.android.client.view;

import android.os.Bundle;
import android.webkit.WebView;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class SaySomeWordsActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		final WebView wv = (WebView) findViewById(R.id.help_webview);
		wv.loadUrl("file:///android_asset/tm_about.html");

	}
}
