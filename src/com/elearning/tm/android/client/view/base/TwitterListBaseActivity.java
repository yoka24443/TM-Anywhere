/**
 * AbstractTwitterListBaseLine用于抽象tweets List的展现
 * UI基本元素要求：一个ListView用于tweet列表
 *               一个ProgressText用于提示信息
 */
package com.elearning.tm.android.client.view.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskParams;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.view.TaskEditActivity;
import com.elearning.tm.android.client.view.module.Feedback;
import com.elearning.tm.android.client.view.module.FeedbackFactory;
import com.elearning.tm.android.client.view.module.ListItemAdapter;
import com.elearning.tm.android.client.view.module.NavBar;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.elearning.tm.android.client.view.module.FeedbackFactory.FeedbackType;

public abstract class TwitterListBaseActivity extends BaseActivity implements
		Refreshable {

	static final String TAG = "TMListBaseActivity";
	protected TextView mProgressText;
	protected Feedback mFeedback;
	protected NavBar mNavbar;
	protected Dialog dialog = null;
	protected TaskInfo task = null;
	protected GenericTask mDeleteTask;
	
	protected static final String SIS_RUNNING_KEY = "running";

	abstract protected int getLayoutId();

	abstract protected ListView getTMList();

	abstract protected TmAdapter getTMAdapter();

	abstract protected void setupState();

	abstract protected String getActivityTitle();

	abstract protected TaskInfo getContextItemTask(int position);

	abstract protected void updateTask(TaskInfo task);

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			setContentView(getLayoutId());
			mNavbar = new NavBar(NavBar.HEADER_STYLE_HOME, this);
			mFeedback = FeedbackFactory.create(this, FeedbackType.PROGRESS);
			// 提示栏
			mProgressText = (TextView) findViewById(R.id.progress_text);
			setupState();
			CreateDialog();
			registerOnClickListener(getTMList());
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsLogedIn();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void draw() {
		getTMAdapter().refresh();
	}

	protected void goTop() {
		getTMList().setSelection(1);
	}

	protected void adapterRefresh() {
		getTMAdapter().refresh();
	}

	protected void specialItemClicked(int position) {

	}

	protected void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				task = getContextItemTask(position);
				if (task == null) {
					specialItemClicked(position);
				} else {
					// launchActivity(StatusActivity.createIntent(tweet));
					dialog.show();
				}
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	// 创建dialog
	protected void CreateDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_sethead_1);
		builder.setTitle("任务管理");
		BaseAdapter adapter = new ListItemAdapter(TwitterListBaseActivity.this);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int which) {
				switch (which) {
				case 0:
					createIntent(task.getTaskID().toString());
					task = null;
					break;
				case 1:
					ShareTask(task);
					task = null;
					break;
				case 2:
					doDelete(task.getTaskID().toString());
					task = null;
					break;
				}
			}
		};
		builder.setAdapter(adapter, listener);
		dialog = builder.create();
	}
	
//	分享
	protected void ShareTask(TaskInfo task){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, task.getRemark());
		startActivity(Intent.createChooser(intent,"分享"));
	}
//	删除
	private void doDelete(String id) {

		if (mDeleteTask != null
				&& mDeleteTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mDeleteTask = new DeleteTask(this);
			mDeleteTask.setListener(mDeleteTaskListener);

			TaskParams params = new TaskParams();
			params.put("tid", id);
			mDeleteTask.execute(params);
		}
	}

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
	
	public  class DeleteTask extends GenericTask {
		public static final String TAG = "DeleteTask";

		private BaseActivity activity;

		public DeleteTask(BaseActivity activity) {
			this.activity = activity;
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			TaskParams param = params[0];
			try {
				String id = param.getString("id");
//				com.ch_linghu.fanfoudroid.fanfou.Status status = null;
//
//				status = activity.getApi().destroyStatus(id);

			} catch (Exception e) {
				return TaskResult.IO_ERROR;
			}
			return TaskResult.OK;
		}

	}
	
	
	public void onDeleteFailure() {
		Log.e(TAG, "Delete failed");
	}

	public void onDeleteSuccess() {
//		remove 相应list中的item
		Toast.makeText(TwitterListBaseActivity.this, "删除成功!" ,Toast.LENGTH_SHORT).show();
	}
	
	public void createIntent(String tid) {
		Intent intent = new Intent();
		intent.putExtra("tid", tid);
		intent.setClass(this, TaskEditActivity.class);
		startActivity(intent);
	}
	
}