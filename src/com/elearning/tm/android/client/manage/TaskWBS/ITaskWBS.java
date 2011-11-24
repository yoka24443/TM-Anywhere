package com.elearning.tm.android.client.manage.TaskWBS;

import java.util.List;
import com.elearning.tm.android.client.model.TaskWBS;

public interface ITaskWBS {
	public List<TaskWBS> QueryProjectWBS();
	public List<TaskWBS> QueryProjectWBSByPID(String pid);
}
