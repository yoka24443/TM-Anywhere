package com.elearning.tm.android.client.view;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.StartPage;
import com.elearning.tm.android.client.manage.TaskInfo.NetWorkTaskInfoManager;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class TastListActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showpets);

		// Fill ListView from database
		fillTaskList();

		final Button gotoEntry = (Button) findViewById(R.id.ButtonEnterMorePets);
		gotoEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(TastListActivity.this,
						TaskAddActivity.class));
				finish();
			}
		});
	}

	private void fillTaskList() {
		ListView listView = (ListView) findViewById(R.id.petList);

		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		NetWorkTaskInfoManager net = new NetWorkTaskInfoManager();
		List<TaskInfo> list = net.queryForAWeekList(UUID.fromString("746be26a-b630-4213-889b-4d085beaf279"), Date.valueOf("2011-09-02"), Date.valueOf("2011-09-26"));
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", list.get(i).getPTitle());
			map.put("TITLE", list.get(i).getRemark());
			map.put("CONTENT", list.get(i).getTaskName());
			contents.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.task,
				new String[] { "PIC", "TITLE", "CONTENT" }, new int[] {
						R.id.task_project_text, R.id.task_content_text,
						R.id.task_meta_text });

		listView.setAdapter(adapter);
	}
}
