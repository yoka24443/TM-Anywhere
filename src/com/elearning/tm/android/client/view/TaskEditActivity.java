package com.elearning.tm.android.client.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.manage.ProjectInfo.NetWorkProjectInfoManager;
import com.elearning.tm.android.client.manage.TaskWBS.NetWorkTaskWBSManager;
import com.elearning.tm.android.client.model.ProjectInfo;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.model.TaskWBS;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.net.TMAPI;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskManager;
import com.elearning.tm.android.client.task.TaskParams;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.util.DateTimeHelper;
import com.elearning.tm.android.client.view.base.BaseActivity;
import com.elearning.tm.android.client.view.module.Feedback;
import com.elearning.tm.android.client.view.module.FeedbackFactory;
import com.elearning.tm.android.client.view.module.FeedbackFactory.FeedbackType;

public class TaskEditActivity extends BaseActivity {
	// Tasks.
	private ProgressDialog dialog; // 保存进度
	protected TaskManager taskManager = new TaskManager();
	protected Feedback mFeedback;
	private GenericTask mRetrieveTask;
	private GenericTask mSendTask;
	private GenericTask mBindTask;
	private static String tid;

	private TaskListener mRetrieveTaskListener = new TaskAdapter() {
		@Override
		public String getName() {
			return "RetrieveTask";
		}

		@Override
		public void onPostExecute(GenericTask mtask, TaskResult result) {
			if (result == TaskResult.OK) {
				int pcount = projectList.size();
				for (int i = 0; i < pcount; i++) {
					if (projectList.get(i).getPID().toString().equals(
							task.getPID().toString())) {
						project.setSelection(i);
						break;
					}
				}
				projectAdapter.notifyDataSetChanged();

			} else if (result == TaskResult.IO_ERROR) {
				if (mtask == mRetrieveTask) {
					mFeedback.failed(((RetrieveTask) mtask).getErrorMsg());
				}
			}
		}
	};

	private class SendTask extends GenericTask {
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				TMAPI api = new TMAPI();
				api.CreateOrModifyTaskInfo(task, "edit");
			} catch (Exception e) {
				return TaskResult.IO_ERROR;
			}
			return TaskResult.OK;
		}
	}

	private TaskListener mSendTaskListener = new TaskAdapter() {
		@Override
		public void onPreExecute(GenericTask task) {
			onSendBegin();
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				onSendSuccess();
//				Intent intent = new Intent(TaskEditActivity.this, TMActivity.class);
//				startActivity(intent);
				finish();
			} else if (result == TaskResult.IO_ERROR) {
				onSendFailure();
			}
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "SendTask";
		}
	};

	private class BindTask extends GenericTask {
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				TMAPI api = new TMAPI();
				task = api.QueryTaskInfo(tid);
			} catch (Exception e) {
				return TaskResult.IO_ERROR;
			}
			return TaskResult.OK;
		}
	}

	private TaskListener mBindTaskListener = new TaskAdapter() {
		@Override
		public void onPreExecute(GenericTask task) {

		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.OK) {
				initEditor();
				initialSpinner();
				doRetrieve();
			} else if (result == TaskResult.IO_ERROR) {
				onSendFailure();
			}
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "SendTask";
		}
	};

	private void onSendBegin() {
		dialog = ProgressDialog
				.show(TaskEditActivity.this, "", "正在发送...", true);
		if (dialog != null) {
			dialog.setCancelable(false);
		}
	}

	private void onSendSuccess() {
		if (dialog != null) {
			dialog.setMessage("发送成功!");
			dialog.dismiss();
		}
	}

	private void onSendFailure() {
		dialog.setMessage("发送失败!");
		dialog.dismiss();
	}

	public void doRetrieve() {
		if (mRetrieveTask != null
				&& mRetrieveTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mRetrieveTask = new RetrieveTask();
			mRetrieveTask.setFeedback(mFeedback);
			mRetrieveTask.setListener(mRetrieveTaskListener);
			mRetrieveTask.execute();
			// Add Task to manager
			taskManager.addTask(mRetrieveTask);
		}
	}

	private class RetrieveTask extends GenericTask {
		private String _errorMsg;

		public String getErrorMsg() {
			return _errorMsg;
		}

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				reloadSpinnerData();
			} catch (Exception e) {
				_errorMsg = e.getMessage();
				return TaskResult.IO_ERROR;
			}
			if (isCancelled()) {
				return TaskResult.CANCELLED;
			}
			return TaskResult.OK;
		}
	}

	Spinner fromEditor;
	Spinner project;
	Spinner projectWbs;

	Button back;
	Button save;
	Button begindate;
	Button enddate;
	EditText title;
	EditText content;
	EditText expectTime;
	EditText costTime;

	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	TaskInfo task;

	private static String[] spfrom = new String[] { "project", "projectid" };
	private static int[] spto = new int[] { R.id.simple_spitem_display,
			R.id.simple_spdditem_display };

	List<ProjectInfo> projectList;
	List<TaskWBS> projectWbsList;

	List<Map<String, String>> projectMapList;
	List<Map<String, String>> projectWbsMapList;

	private SimpleAdapter projectAdapter;
	private SimpleAdapter projectWbsAdapter;
	int selpos; // 当前选中的下拉选项的位置

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.create_task);
		mFeedback = FeedbackFactory.create(this, FeedbackType.DIALOG);
		initTaskInfo();
		// 保存按钮事件
		save = (Button) this.findViewById(R.id.top_send_btn);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				doSave();
			}
		});

	}

	private void initEditor() {
		// 初始化顶部按钮
		back = (Button) this.findViewById(R.id.top_back);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		content = (EditText) this
				.findViewById(R.id.createtask_taskcontent_edittext);
		content.setText(task.getRemark());
		title = (EditText) this
				.findViewById(R.id.createtask_subjectcontent_edittext);
		title.setText(task.getTaskName());
		expectTime = (EditText) this
				.findViewById(R.id.createtask_expectcost_edittext);
		expectTime.setText(String.valueOf(task.getPlanTime()));
		costTime = (EditText) this
				.findViewById(R.id.createtask_realcost_edittext);
		costTime.setText(String.valueOf(task.getTotalTime()));
		// 初始化日期按钮
		Calendar today = Calendar.getInstance();
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");

		if (!TextUtils.isEmpty(date)) {
			java.util.Date dt = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dt = df.parse(date);
				today.setTime(dt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		begindate = (Button) this
				.findViewById(R.id.createtask_begindate_button);
		begindate.setText(DateTimeHelper.fmtDate(task.getBeginTime()));
		begindate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openDatePicker(TaskEditActivity.this, begindate);
			}
		});

		enddate = (Button) this.findViewById(R.id.createtask_enddate_button);
		enddate.setText(DateTimeHelper.fmtDate(task.getEndTime()));
		enddate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openDatePicker(TaskEditActivity.this, enddate);
			}
		});

	}

	private void initialSpinner() {
		fromEditor = (Spinner) findViewById(R.id.createtask_completionrate_spinner);
		String[] ls = getResources().getStringArray(R.array.task_status);
		// 获取XML中定义的数组
		for (int i = 0; i < ls.length; i++) {
			list.add(ls[i]);
		}
		// 把数组导入到ArrayList中
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		// 设置下拉菜单的风格
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromEditor.setAdapter(adapter);
		fromEditor.setSelection(task.getStatus());

		// project and wbs
		project = (Spinner) findViewById(R.id.createtask_project_spinner);
		projectList = new ArrayList<ProjectInfo>();
		projectMapList = new ArrayList<Map<String, String>>();

		projectAdapter = new SimpleAdapter(this, projectMapList,
				R.layout.simple_spitem, spfrom, spto);
		projectAdapter.setDropDownViewResource(R.layout.simple_spdd);
		project.setAdapter(projectAdapter);

		projectWbs = (Spinner) findViewById(R.id.createtask_subtasktype_spinner);
		projectWbsList = new ArrayList<TaskWBS>();
		projectWbsMapList = new ArrayList<Map<String, String>>();
		projectWbsAdapter = new SimpleAdapter(this, projectWbsMapList,
				R.layout.simple_spitem, spfrom, spto);
		projectWbsAdapter.setDropDownViewResource(R.layout.simple_spdd);
		projectWbs.setAdapter(projectWbsAdapter);

		project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						selpos = project.getSelectedItemPosition();
						TextView tv = (TextView) view
								.findViewById(R.id.simple_spitem_display);
						String pid = tv.getText().toString();
						onProjectSelected(pid);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
	}

	private void reloadSpinnerData() {
		projectList.clear();
		projectMapList.clear();

		NetWorkProjectInfoManager proManager = new NetWorkProjectInfoManager();
		List<ProjectInfo> list = proManager.QueryProjectList();

		if (list != null && list.size() != 0) {
			projectList.addAll(list);
			for (ProjectInfo pi : projectList) {
				Map<String, String> row = new HashMap<String, String>();

				row.put(spfrom[0], pi.getPID().toString());
				row.put(spfrom[1], pi.getPTitle());
				projectMapList.add(row);
			}

		}
	}

	private void onProjectSelected(String pid) {
		projectWbsList.clear();
		projectWbsMapList.clear();
		projectWbsAdapter.notifyDataSetChanged();

		NetWorkTaskWBSManager proWBSManager = new NetWorkTaskWBSManager();
		List<TaskWBS> list = proWBSManager.QueryProjectWBSByPID(pid);

		if (list != null && list.size() != 0) {
			projectWbsList.addAll(list);
			for (TaskWBS tw : projectWbsList) {
				Map<String, String> row = new HashMap<String, String>();

				row.put(spfrom[0], tw.getTaskID().toString());
				row.put(spfrom[1], tw.getName());
				projectWbsMapList.add(row);
			}
		}
		if (task != null) {
			int wbscount = projectWbsList.size();
			for (int i = 0; i < wbscount; i++) {
				if (projectWbsList.get(i).getTaskID().toString().equals(
						task.getTaskWBS().toString())) {
					projectWbs.setSelection(i);
					break;
				}
			}
		}

		projectWbsAdapter.notifyDataSetChanged();

	}

	private void doSave() {
		HashMap<String, String> item = (HashMap<String, String>) project
				.getSelectedItem();
		String pid = item.get(spfrom[0]);

		HashMap<String, String> item1 = (HashMap<String, String>) projectWbs
				.getSelectedItem();
		String wbsid = item1.get(spfrom[0]);

		String titleValue = title.getText().toString();
		if (TextUtils.isEmpty(titleValue)) {
			title.requestFocus();
			return;
		}
		String beginDate = begindate.getText().toString();
		String endDate = enddate.getText().toString();

		SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = null;
		Date end = null;
		try {
			begin = parseTime.parse(beginDate);
			end = parseTime.parse(endDate);
			if (begin.compareTo(end) > 0) {
				Toast.makeText(this, "开始时间不能大于结束时间.", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String contentValue = content.getText().toString();

		String expectTimeValue = expectTime.getText().toString();
		if (TextUtils.isEmpty(expectTimeValue)) {
			expectTime.requestFocus();
			return;
		}
		int planTime = Integer.parseInt(expectTimeValue);

		String realTimeValue = costTime.getText().toString();
		if (TextUtils.isEmpty(realTimeValue)) {
			costTime.requestFocus();
			return;
		}
		int realTime = Integer.parseInt(realTimeValue);
		int status = fromEditor.getSelectedItemPosition();

		task.setTaskID(UUID.fromString(tid));
		task.setPID(UUID.fromString(pid));
		task.setPType(UUID.fromString(wbsid));
		task.setTaskName(titleValue);
		task.setRemark(contentValue);
		task.setBeginTime(begin);
		task.setEndTime(end);
		task.setTotalTime(realTime);
		task.setPlanTime(planTime);
		task.setStatus(status);
		UserInfo user = TMApplication.instance.getCurrentUser();
		task.setAssignUser(user.getUserID());
		if (mSendTask != null
				&& mSendTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mSendTask = new SendTask();
			mSendTask.setListener(mSendTaskListener);
			mSendTask.execute();
		}
	}

	public void openDatePicker(Context context, final TextView datatime) {
		final Calendar c = Calendar.getInstance();
		DatePickerDialog picker = new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						c.set(Calendar.YEAR, year);
						c.set(Calendar.MONTH, monthOfYear);
						c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						datatime.setText(DateTimeHelper.fmtDate(c.getTime()));
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DAY_OF_MONTH));
		picker.show();
	}

	private void initTaskInfo() {
		Intent intent = getIntent();
		tid = intent.getStringExtra("tid");

		if (mBindTask != null
				&& mBindTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mBindTask = new BindTask();
			mBindTask.setListener(mBindTaskListener);
			mBindTask.execute();
		}
	}

	private void initData() {
		int pcount = projectList.size();
		for (int i = 0; i < pcount; i++) {
			if (projectList.get(i).getPID().toString().equals(
					task.getPID().toString())) {
				project.setSelection(i);
				projectAdapter.notifyDataSetChanged();
				break;
			}

		}
		project.setSelection(4);
	}

}
