package com.elearning.tm.android.client.view.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.net.TMAPI;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskParams;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.view.base.BaseActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class TaskBarReportActivity extends BaseActivity {

	private static String begin;
	private static String end;
	
	private List<TaskInfo> listViewData = new ArrayList<TaskInfo>();

	private ProgressDialog dialog; // 加载进度

	private GenericTask mSendTask;

	private class SendTask extends GenericTask {
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			try {
				loadData();
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
				drawBarChart();
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
		dialog = ProgressDialog.show(TaskBarReportActivity.this, "", "正在加载数据...", true);
		if (dialog != null) {
			dialog.setCancelable(false);
		}
	}

	private void onSendSuccess() {
		if (dialog != null) {
			dialog.setMessage("加载数据成功!");
			dialog.dismiss();
		}
	}

	private void onSendFailure() {
		if (dialog != null) {
			dialog.setMessage("加载数据失败!");
			dialog.dismiss();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_bar);
		initDateRegion();
		doRetrieve();
	}

	private void drawBarChart() {
		View view = new BalanceBarChart(TaskBarReportActivity.this,
				getResources().getConfiguration().orientation, getResources()
						.getDisplayMetrics().density)
				.createBarChartView(listViewData);
		LinearLayout pieCanvas = (LinearLayout) this.findViewById(R.id.barChartLayout);
		pieCanvas.addView(view);
	}

	public void doRetrieve() {
		if (mSendTask != null
				&& mSendTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mSendTask = new SendTask();
			mSendTask.setListener(mSendTaskListener);
			mSendTask.execute();
		}
	}

	private void loadData() {
		listViewData.clear();

		TMAPI api = new TMAPI();
		listViewData = api.QueryUsersTaskReportData(begin, end);
	}

	private List<TaskInfo> sumTaskTotalTime(Map<String, List<TaskInfo>> items) {
		List<TaskInfo> groups = new ArrayList<TaskInfo>();
		if (items.size() == 0) {
			return groups;
		}
		Iterator it = items.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<TaskInfo> part = items.get(key);
			int total = 0;
			for (TaskInfo task : part) {
				total += task.getTotalTime();
			}
			TaskInfo totalTask = new TaskInfo();
			totalTask.setTotalTime(total);
			totalTask.setProjectName(key);
			groups.add(totalTask);
		}
		return groups;
	}
	
	private void initDateRegion() {
		Intent intent = getIntent();
		begin = intent.getStringExtra("begin");
		end = intent.getStringExtra("end");
	}
	
}
