package com.elearning.tm.android.client;

import com.elearning.tm.android.client.view.FrameActivity;
import com.elearning.tm.android.client.view.TastListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartPage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash);
    }
    
    @Override
    protected void onResume() {
		super.onResume();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
				startActivity(new Intent(StartPage.this, FrameActivity.class));
				finish();
			}
		}.start();
	}
}