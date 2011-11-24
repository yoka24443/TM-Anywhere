package com.elearning.tm.android.client.manage.ProjectInfo;

import java.util.List;

import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.model.ProjectInfo;
import com.elearning.tm.android.client.net.TMAPI;

public class NetWorkProjectInfoManager implements IProjectInfo {

	TMAPI api = TMApplication.tmApi; 
	@Override
	public List<ProjectInfo> QueryProjectList() {
		List<ProjectInfo> list = api.QueryProjectList();
		return list;
	}

}
