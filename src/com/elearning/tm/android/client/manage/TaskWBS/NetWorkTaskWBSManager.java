package com.elearning.tm.android.client.manage.TaskWBS;

import java.util.List;

import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.model.TaskWBS;
import com.elearning.tm.android.client.net.TMAPI;

public class NetWorkTaskWBSManager implements ITaskWBS {

	TMAPI api = TMApplication.tmApi;
	@Override
	public List<TaskWBS> QueryProjectWBS() {
		return api.QueryProjectWBS();
	}

	@Override
	public List<TaskWBS> QueryProjectWBSByPID(String pid) {
		return api.QueryProjectWBSByPID(pid);
	} 

}
