package com.elearning.tm.android.client.view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.manage.TaskInfo.NetWorkTaskInfoManager;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskParams;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.view.base.TwitterListBaseActivity;
import com.elearning.tm.android.client.view.module.SimpleFeedback;
import com.elearning.tm.android.client.view.module.TMArrayAdapter;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class SearchResultActivity extends TwitterListBaseActivity {
	private static final String TAG = "SearchActivity";

	// Views.
	private PullToRefreshListView tmTaskList;
	private View tmListFooter;
	private ProgressBar loadMoreGIF;

	// State.
	private ArrayList<TaskInfo> tmTasks;
	private TMArrayAdapter tmAdapter;
	private int tmNextPage = 1;
	private boolean tmIsGetMore = false;

	// Tasks.
	private GenericTask tmSearchTask;

	private TaskListener mSearchTaskListener = new TaskAdapter() {
		@Override
		public void onPreExecute(GenericTask task) {
			if (tmIsGetMore) {
				loadMoreGIF.setVisibility(View.VISIBLE);
			}

			if (tmNextPage == 1) {
				updateProgress(getString(R.string.page_status_refreshing));
			} else {
				updateProgress(getString(R.string.page_status_refreshing));
			}
			tmTaskList.prepareForRefresh();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {
			draw();
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			loadMoreGIF.setVisibility(View.GONE);
			tmTaskList.onRefreshComplete();

			if (result == TaskResult.AUTH_ERROR) {
				logout();
			} else if (result == TaskResult.OK) {
				draw();
				if (!tmIsGetMore) {
					tmTaskList.setSelection(1);
				}
			}
			updateProgress("");
		}

		@Override
		public String getName() {
			return "SearchTask";
		}
	};

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			mNavbar.setHeaderTitle("Task Manager");
			setupState();
			doSearch(false);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.main;
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsLogedIn();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy.");

		if (tmSearchTask != null
				&& tmSearchTask.getStatus() == GenericTask.Status.RUNNING) {
			tmSearchTask.cancel(true);
		}

		super.onDestroy();
	}

	// UI helpers.

	private void updateProgress(String progress) {
		mProgressText.setText(progress);
	}

	private void doSearch(boolean isGetMore) {
		Log.d(TAG, "Attempting search.");

		if (tmSearchTask != null
				&& tmSearchTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			tmIsGetMore = isGetMore;
			tmSearchTask = new SearchTask();
			tmSearchTask.setFeedback(mFeedback);
			tmSearchTask.setListener(mSearchTaskListener);
			tmSearchTask.execute();
		}
	}

	private class SearchTask extends GenericTask {
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			List<TaskInfo> list;
			try {
				NetWorkTaskInfoManager net = new NetWorkTaskInfoManager();
				list = net.queryForAWeekList(UUID
						.fromString("746be26a-b630-4213-889b-4d085beaf279"),
						Date.valueOf("2011-09-02"), Date.valueOf("2011-09-26"));
				addTasks(list);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
				return TaskResult.IO_ERROR;
			}
			publishProgress(SimpleFeedback.calProgressBySize(40, 20, list));
			return TaskResult.OK;
		}
	}

	public void doGetMore() {
		if (!isLastPage()) {
			doSearch(true);
		}
	}

	public boolean isLastPage() {
		return tmNextPage == -1;
	}

	@Override
	protected void adapterRefresh() {
		tmAdapter.refresh(tmTasks);
	}

	private synchronized void addTasks(List<TaskInfo> tasks) {
		if (tasks.size() == 0) {
			tmNextPage = -1;
			return;
		}
		tmTasks.addAll(tasks);
		++tmNextPage;
	}

	@Override
	protected TaskInfo getContextItemTask(int position) {
		if (position > 0 && position < tmAdapter.getCount()) {
			TaskInfo item = (TaskInfo) tmAdapter.getItem(position - 1);
			if (item == null) {
				return null;
			} else {
				return item;
			}
		} else {
			return null;
		}
	}

	@Override
	protected ListView getTaskList() {
		return tmTaskList;
	}

	protected void setupState() {
		tmTasks = new ArrayList<TaskInfo>();
		tmTaskList = (PullToRefreshListView) findViewById(R.id.task_list);
		tmAdapter = new TMArrayAdapter(this);
		tmTaskList.setAdapter(tmAdapter);

		tmTaskList.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				doRetrieve();
			}
		});

		// Add Footer to ListView
		tmListFooter = View.inflate(this, R.layout.listview_footer, null);
		tmTaskList.addFooterView(tmListFooter, null, true);
		loadMoreGIF = (ProgressBar) findViewById(R.id.rectangleProgressBar);
	}

	@Override
	public void doRetrieve() {
		doSearch(false);
	}

	@Override
	protected void specialItemClicked(int position) {
		// 注意 mTweetAdapter.getCount 和 mTweetList.getCount的区别
		// 前者仅包含数据的数量（不包括foot和head），后者包含foot和head
		// 因此在同时存在foot和head的情况下，list.count = adapter.count + 2
		if (position == 0) {
			doRetrieve();
		} else if (position == tmTaskList.getCount() - 1) {
			// 最后一个Item(footer)
			doGetMore();
		}
	}

	@Override
	protected TmAdapter getTaskAdapter() {
		return tmAdapter;
	}

}
