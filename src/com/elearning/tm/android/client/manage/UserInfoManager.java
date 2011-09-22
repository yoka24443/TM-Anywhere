package com.elearning.tm.android.client.manage;

import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.manage.UserInfo.IUserInfo;
import com.elearning.tm.android.client.manage.UserInfo.LocalUserInfoManager;
import com.elearning.tm.android.client.manage.UserInfo.NetWorkUserInfoManager;
import com.elearning.tm.android.client.model.UserInfo;

public class UserInfoManager {
	private static final boolean isConnected = TMApplication.isConnected;
	private static IUserInfo user = null;
	static {
		if (isConnected) {
			user = new NetWorkUserInfoManager();
		} else
			user = new LocalUserInfoManager();

	}
	public static UserInfo getCurrentUserInfo(String account, String password) {
		return user.getCurrentUserInfo(account, password);
	}

}
