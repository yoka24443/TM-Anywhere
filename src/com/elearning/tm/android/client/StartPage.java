package com.elearning.tm.android.client;

import com.elearning.tm.android.client.view.FrameActivity;
import com.elearning.tm.android.client.view.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class StartPage extends BaseActivity {
	private ImageView splashImgView = null;
	/*	private int onShowTime = 2000;
	private Class<?> nextUI = FrameActivity.class;*/
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        splashImgView =(ImageView)findViewById(R.id.splashImageView);
    }
    
/*    @Override
	protected void onStart() {
		super.onStart();
		int sIndex = 0;
		showImg(0.2f,R.drawable.tm_logo,300);
		showImg(0.2f,R.drawable.startpage,onShowTime * ++sIndex);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				startActivity(new Intent(StartPage.this,nextUI));
				StartPage.this.finish();
			}
		}, onShowTime * ++sIndex);
    }
    
    private void showImg(final float startAlpha,final int drawableId,int delay){
    	new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				splashImgView.setImageResource(drawableId);
				AlphaAnimation animation = new AlphaAnimation(startAlpha, 1.0f);
				animation.setDuration(1000);
				splashImgView.startAnimation(animation);
			}
		},delay);
	}
    */
    
    
    @Override
    protected void onResume() {
		super.onResume();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
				startActivity(new Intent(StartPage.this, FrameActivity.class));
				finish();
			}
		}.start();
	}
}