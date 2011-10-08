package com.elearning.tm.android.client.manage.UserInfo;

import java.util.List;

import android.content.SharedPreferences;

import com.elearning.tm.android.client.app.Preferences;
import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.net.TMAPI;
import com.elearning.tm.android.client.util.EncryptHelper;

public class NetWorkUserInfoManager implements IUserInfo {

	private SharedPreferences mPreferences;
	TMAPI api = TMApplication.tmApi; //是否可以单例模式在application里?
	@Override
	public UserInfo getCurrentUserInfo(String account, String password) {
		UserInfo user = api.Login(account, password);
		//初始登录将登陆信息保留在Preferences中,以后可以通过配置中的开关进行调节
		if(user != null){
			mPreferences = TMApplication.tmPref;
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putString(Preferences.USERNAME_KEY, account);

			editor.putString(Preferences.PASSWORD_KEY,EncryptHelper.encryptPassword(password));
			// add 存储当前用户的id
			editor.putString(Preferences.CURRENT_USER_ID, user.getUserID().toString());
			editor.commit();
		}
		return user;
	}
	
	public List<UserInfo> getUserList(int pageIndex, int pageSize){
		List<UserInfo> list = api.QueryUserList("", pageIndex, pageSize);
		return list;
	}

}
