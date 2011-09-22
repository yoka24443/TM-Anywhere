package com.elearning.tm.android.client.manage.UserInfo;

import com.elearning.tm.android.client.model.UserInfo;

public interface IUserInfo {
	UserInfo getCurrentUserInfo(String account, String password);
}
