package com.elearning.tm.android.client.app;

import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.net.TMAPI;

public class TMApplication extends Application {

	public static final String TAG = "TMApplication";
	public static TMAPI tmApi;
	public static Context tmContext;
	public static SharedPreferences tmPref;
	public static String networkType;
	public static boolean isConnected;
//	public static boolean isLogined;
	public static UserInfo currentUser;
	public static TMApplication instance;
	
	public static TMApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		tmContext = this.getApplicationContext();
		tmApi = new TMAPI();
		tmPref = PreferenceManager.getDefaultSharedPreferences(this);
		instance = this;
//		isLogined = checkIsLogedIn(); //状态应该实时获取
//		networkType = getNetworkType();//状态应该实时获取
	}

	// 由于检查网络是否连接是某一瞬间的动作,故写成一方法
	public String getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			if (activeNetInfo.getState() == NetworkInfo.State.CONNECTED) {
				isConnected = true; // 判断是否联网
			}
			return activeNetInfo.getExtraInfo(); // 接入点名称: 此名称可被用户任意更改 如: cmwap,
		} else {
			return null;
		}
	}

	// 检查是否已经登陆
	public boolean checkIsLogedIn() {
		if (tmPref.contains(Preferences.CURRENT_USER_ID)
				&& tmPref.getString(Preferences.CURRENT_USER_ID, "00000000-0000-0000-0000-000000000000") != "00000000-0000-0000-0000-000000000000") {
			return true;
		} else
			return false;
	}

	//返回当前登录用户的信息
	public UserInfo getCurrentUser(){
		if(checkIsLogedIn()){
			UserInfo user = new UserInfo();
			user.setUserID(UUID.fromString(tmPref.getString(Preferences.CURRENT_USER_ID, "00000000-0000-0000-0000-000000000000")));
			user.setPassword(tmPref.getString(Preferences.USERNAME_KEY, ""));
			user.setUserAccount(tmPref.getString(Preferences.USERNAME_KEY, ""));
			return user;
		}else
			return null;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
