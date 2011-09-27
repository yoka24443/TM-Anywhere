package com.elearning.tm.android.client.manage.TaskInfo;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.net.TMAPI;

public class NetWorkTaskInfoManager implements ITaskInfo {

	@Override
	public List<TaskInfo> queryForAWeekList(UUID uid, Date beginDate, Date endDate) {
		TMAPI api = TMApplication.tmApi; 
		List<TaskInfo>  taskList = api.QueryUserTaskList(uid.toString(), beginDate, endDate);
		return taskList;
	}

}
