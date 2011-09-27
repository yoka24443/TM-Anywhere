package com.elearning.tm.android.client.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import com.elearning.tm.android.client.data.DatabaseHelper;
import com.elearning.tm.android.client.model.ProjectInfo;




public class ProjectInfoDao {
	private static final String TAG = "ProjectInfoDao";
	private static DatabaseHelper dbInstance = null;

	//构造器暂时没啥用途
	public ProjectInfoDao(Context context) {
		
	}
	
	public static synchronized DatabaseHelper getInstance(Context context) {
		if (null == dbInstance) {
			dbInstance = new DatabaseHelper(context);
		}
		return dbInstance;
	}
	
	public List<ProjectInfo> queryForAll() {
		List<ProjectInfo> list = null;
		try {
			list = dbInstance.getDao(ProjectInfo.class).queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public List<ProjectInfo> queryForEq(String fieldName, Object value) {
		List<ProjectInfo> list = null;
		try {
			list = dbInstance.getDao(ProjectInfo.class).queryForEq(fieldName, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ProjectInfo> queryBySql(String sqlString, String[] value) {
		List<ProjectInfo> list = null;
//		try {
//			//list = dbInstance.getDao(ProjectInfo.class).queryRaw(sqlString, value);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return list;
	}

}
