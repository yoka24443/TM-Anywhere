package com.elearning.tm.android.client.manage.UserInfo;

import java.util.UUID;

import android.content.SharedPreferences;

import com.elearning.tm.android.client.app.Preferences;
import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.model.UserInfo;

public class LocalUserInfoManager implements IUserInfo {
	private SharedPreferences mPreferences = TMApplication.tmPref;

	@Override
	public UserInfo getCurrentUserInfo(String account, String password) {
		UserInfo user = new UserInfo();
		if (mPreferences.contains(Preferences.CURRENT_USER_ID)
				&& mPreferences.contains(Preferences.USERNAME_KEY)
				&& mPreferences.contains(Preferences.PASSWORD_KEY)) {
			user.setUserAccount(mPreferences.getString(
					Preferences.USERNAME_KEY, ""));
			user.setPassword(mPreferences.getString(Preferences.PASSWORD_KEY,
					""));
			user.setUserID(UUID.fromString(mPreferences.getString(
					Preferences.CURRENT_USER_ID, "00000000-0000-0000-0000-000000000000")));
		}
		return user;
	}

}
