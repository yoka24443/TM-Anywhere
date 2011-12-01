package com.elearning.tm.android.client.view.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.NamedItem;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BalanceActivity extends BaseActivity {

	private static String begin;
	private static String end;
	private static String[] bindingFrom = new String[] { "layout", "name",
			"totaltime" };

	private static int[] bindingTo = new int[] { R.id.report_balance_layout,
			R.id.report_balance_item_name, R.id.report_balance_item_totaltime };

	private List<TaskInfo> listViewData = new ArrayList<TaskInfo>();

	private List<Map<String, Object>> listViewMapList = new ArrayList<Map<String, Object>>();

	private Map<String, List<TaskInfo>> list = new HashMap<String, List<TaskInfo>>();

	private ListView listView;

	private SimpleAdapter listViewAdapter;

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
				listViewAdapter.notifyDataSetChanged();
				// initEditor(); //初始化组件
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
		dialog = ProgressDialog.show(BalanceActivity.this, "", "正在加载数据...",
				true);
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
		setContentView(R.layout.report_balance);
		initDateRegion();
		initialContent();
		doRetrieve();
	}

	private void initialContent() {
		listViewAdapter = new SimpleAdapter(this, listViewMapList,
				R.layout.report_balance_item, bindingFrom, bindingTo);
		listViewAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data, String text) {
				TaskInfo task = (TaskInfo) data;
				switch (view.getId()) {
				case R.id.report_balance_item_name:
					String name = task.getTaskName();
					final TextView item_name = (TextView) view;
					item_name.setText(name);
					adjustItem(item_name, task, getResources().getDisplayMetrics().density);
					break;
				case R.id.report_balance_item_totaltime:
					Integer totaltime = task.getTotalTime();
					final TextView item_totaltime = (TextView) view;
					item_totaltime.setText(totaltime.toString());
					adjustItem(item_totaltime, task, getResources().getDisplayMetrics().density);
					break;
				case R.id.report_balance_layout:
					LinearLayout layout = (LinearLayout) view;
					adjustLayout(layout, task);
					break;
				}
				return true;
			}
		});

		listView = (ListView) findViewById(R.id.report_balance_list);
		listView.setAdapter(listViewAdapter);
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
		listViewMapList.clear();
		list.clear(); // 清空上一次的数据

		TMAPI api = new TMAPI();
		list = api.QueryTaskReportData("746be26a-b630-4213-889b-4d085beaf279",
				begin, end);

		listViewData = sumTaskTotalTime(list);
		for (TaskInfo task : listViewData) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put(bindingFrom[0], task);// layout
			row.put(bindingFrom[1], task);
			row.put(bindingFrom[2], task);
			listViewMapList.add(row);
		}
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
			totalTask.setTaskName(key);
			part.add(0, totalTask);
			groups.addAll(part);
		}
		return groups;
	}

	protected void adjustLayout(LinearLayout layout, TaskInfo task) {
		if (task.getTaskWBS() == null) { // 表明是根节点
			layout.setBackgroundDrawable((getResources()
					.getDrawable(R.drawable.selector_balance_indent0)));
		} else {
			layout.setBackgroundDrawable((getResources()
					.getDrawable(R.drawable.selector_balance_indent)));
		}
	}

	protected void adjustItem(TextView tv, TaskInfo b, float dp) {
		float fontPixelSize = 18;
		float ratio = 0;
		int marginLeft = 0;
		int marginRight = 5;
		int paddingTB = 0;

		if (b.getTaskWBS() == null) {
			ratio = 1F;
			paddingTB = 5;
			marginLeft = 5;
			tv.setTextColor(getResources().getColor(R.color.other_fgl));
		} else {
			ratio = 0.85F;
			paddingTB = 3;
			marginLeft = 10;
			tv.setTextColor(getResources().getColor(R.color.other_fgd));
		}

		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontPixelSize * ratio);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv
				.getLayoutParams();
		lp.setMargins((int) (marginLeft * dp), lp.topMargin,
				(int) (marginRight * dp), lp.bottomMargin);
		tv.setPadding(tv.getPaddingLeft(), (int) (paddingTB * dp), tv
				.getPaddingRight(), (int) (paddingTB * dp));
	}
	
	private void initDateRegion() {
		Intent intent = getIntent();
		begin = intent.getStringExtra("begin");
		end = intent.getStringExtra("end");
	}

}
