package com.elearning.tm.android.client.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import com.elearning.tm.android.client.net.TMAPI;

public class TMApplication extends Application {

	public static final String TAG = "TMApplication";
	public static TMAPI tmApi;
	public static Context tmContext;
	public static SharedPreferences tmPref;
	public static String networkType ;
	public static boolean isConnected;
	
	@Override
	public void onCreate() {
		super.onCreate();
		tmContext = this.getApplicationContext();
		tmApi = new TMAPI();
		tmPref = PreferenceManager.getDefaultSharedPreferences(this);
		networkType = getNetworkType();
	}

	public String getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			if (activeNetInfo.getState() == NetworkInfo.State.CONNECTED) {
				isConnected = true; //判断是否联网
			}
			return activeNetInfo.getExtraInfo(); // 接入点名称: 此名称可被用户任意更改 如: cmwap,
		} else {
			return null;
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
