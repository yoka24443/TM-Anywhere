package com.elearning.tm.android.client.view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

import com.elearning.tm.android.client.manage.TaskInfo.NetWorkTaskInfoManager;
import com.elearning.tm.android.client.model.Paging;
import com.elearning.tm.android.client.model.TaskInfo;
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
		int pageindex = page.getPage();
		NetWorkTaskInfoManager net = new NetWorkTaskInfoManager();
		ArrayList<TaskInfo> list = (ArrayList<TaskInfo>) net.queryUserTaskList(UUID.fromString("746be26a-b630-4213-889b-4d085beaf279"), pageindex, PRE_PAGE_COUNT);
		return list;
	}



}
