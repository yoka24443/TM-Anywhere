package com.elearning.tm.android.client.view;

import java.util.ArrayList;
import java.util.List;

import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.view.base.TwitterCursorBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;



public class TMActivity extends TwitterCursorBaseActivity{
	private static final String TAG = "TwitterActivity";

	private static final String LAUNCH_ACTION = "com.ch_linghu.fanfoudroid.TWEETS";
	protected GenericTask mDeleteTask;

	private TaskListener mDeleteTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			return "DeleteTask";
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.AUTH_ERROR) {
				logout();
			} else if (result == TaskResult.OK) {
				onDeleteSuccess();
			} else if (result == TaskResult.IO_ERROR) {
				onDeleteFailure();
			}
		}
	};

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			mNavbar.setHeaderTitle("饭否fanfou.com");
			// 仅在这个页面进行schedule的处理
			manageUpdateChecks();

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			mDeleteTask.cancel(true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			outState.putBoolean(SIS_RUNNING_KEY, true);
		}
	}


	@Override
	protected Cursor fetchMessages() {
		return getDb().fetchAllTweets(getUserId(), StatusTable.TYPE_HOME);
	}

	@Override
	protected String getActivityTitle() {
		return getResources().getString(R.string.page_title_home);
	}

	@Override
	protected void markAllRead() {
		getDb().markAllTweetsRead(getUserId(), StatusTable.TYPE_HOME);
	}

	// hasRetrieveListTask interface
	@Override
	public int addMessages(ArrayList<Tweet> tweets, boolean isUnread) {
		// 获取消息的时候，将status里获取的user也存储到数据库

		// ::MARK::
		for (Tweet t : tweets) {
			getDb().createWeiboUserInfo(t.user);
		}
		return getDb().putTweets(tweets, getUserId(), StatusTable.TYPE_HOME,
				isUnread);
	}

	@Override
	public String fetchMaxId() {
		return getDb().fetchMaxTweetId(getUserId(), StatusTable.TYPE_HOME);
	}

	@Override
	public List<Status> getMessageSinceId(String maxId) throws HttpException {
		if (maxId != null) {
			return getApi().getFriendsTimeline(new Paging(maxId));
		} else {
			return getApi().getFriendsTimeline();
		}
	}

	@Override
	public String fetchMinId() {
		return getDb().fetchMinTweetId(getUserId(), StatusTable.TYPE_HOME);
	}

	@Override
	public List<Status> getMoreMessageFromId(String minId) throws HttpException {
		Paging paging = new Paging(1, 20);
		paging.setMaxId(minId);
		return getApi().getFriendsTimeline(paging);
	}

	@Override
	public String getUserId() {
		return TwitterApplication.getMyselfId();
	}

	@Override
	public int addMessages(ArrayList<TaskInfo> tweets, boolean isUnread) {
		// TODO Auto-generated method stub
		return 0;
	}


}
