package com.elearning.tm.android.client.data;

import java.sql.SQLException;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.elearning.tm.android.client.model.ProjectInfo;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.model.TaskWBS;
import com.elearning.tm.android.client.model.UserInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "tm.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<UserInfo, UUID> userDao = null;
	private Dao<ProjectInfo, UUID> projectDao = null;
	private Dao<TaskInfo, UUID> taskDao = null;
	private Dao<TaskWBS, UUID> wbsDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, UserInfo.class);
			TableUtils.createTable(connectionSource, ProjectInfo.class);
			TableUtils.createTable(connectionSource, TaskInfo.class);
			TableUtils.createTable(connectionSource, TaskWBS.class);
			Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " );
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, UserInfo.class, true);
			TableUtils.dropTable(connectionSource, ProjectInfo.class, true);
			TableUtils.dropTable(connectionSource, TaskInfo.class, true);
			TableUtils.dropTable(connectionSource, TaskWBS.class, true);
			
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<UserInfo, UUID> getUserDataDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(UserInfo.class);
		}
		return userDao;
	}
	public Dao<ProjectInfo, UUID> getProjectDataDao() throws SQLException {
		if (projectDao == null) {
			projectDao = getDao(ProjectInfo.class);
		}
		return projectDao;
	}
	public Dao<TaskInfo, UUID> getTaskDataDao() throws SQLException {
		if (taskDao == null) {
			taskDao = getDao(TaskInfo.class);
		}
		return taskDao;
	}
	public Dao<TaskWBS, UUID> getTaskWBSDataDao() throws SQLException {
		if (wbsDao == null) {
			wbsDao = getDao(TaskWBS.class);
		}
		return wbsDao;
	}

	@Override
	public void close() {
		super.close();
		userDao = null;
	}
}
