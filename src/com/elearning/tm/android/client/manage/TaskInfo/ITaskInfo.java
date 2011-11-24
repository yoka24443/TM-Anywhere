package com.elearning.tm.android.client.manage.TaskInfo;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.elearning.tm.android.client.model.TaskInfo;

public interface ITaskInfo {
	public List<TaskInfo> queryUserTaskList(UUID uid, int pageIndex, int pageSize);
}
