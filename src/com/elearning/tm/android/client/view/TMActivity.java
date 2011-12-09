package com.elearning.tm.android.client.view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.manage.TaskInfo.NetWorkTaskInfoManager;
import com.elearning.tm.android.client.model.Paging;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.view.base.TwitterCursorBaseActivity;

import android.os.Bundle;

public class TMActivity extends TwitterCursorBaseActivity{
	private static final String TAG = "TwitterActivity";
	private static final int PRE_PAGE_COUNT = 20;// 官方分页为每页20
	private int currentPage = 1;
	
	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			mNavbar.setHeaderTitle("Task Manager");
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected String getActivityTitle() {
		return null;
	}

	@Override
	public Paging getCurrentPage() {
		currentPage =  currentPage - 1 <= 0 ? 1: currentPage -1;
		return new Paging(this.currentPage);
	}

	@Override
	public Paging getNextPage() {
		currentPage += 1;
		return new Paging(currentPage);
	}

	@Override
	public ArrayList<TaskInfo> getTaskList(Paging page) {
//		UUID.fromString("746be26a-b630-4213-889b-4d085beaf279"),没有添加判断是否联网
		int pageindex = page.getPage();
		UserInfo user = TMApplication.instance.getCurrentUser();
		NetWorkTaskInfoManager net = new NetWorkTaskInfoManager();
		ArrayList<TaskInfo> list = (ArrayList<TaskInfo>) net.queryUserTaskList(user.getUserID(), pageindex, PRE_PAGE_COUNT);
		return list;
	}



}
