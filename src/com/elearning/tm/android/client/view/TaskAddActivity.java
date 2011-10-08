package com.elearning.tm.android.client.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
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
import com.elearning.tm.android.client.manage.ProjectInfo.NetWorkProjectInfoManager;
import com.elearning.tm.android.client.manage.TaskWBS.NetWorkTaskWBSManager;
import com.elearning.tm.android.client.model.ProjectInfo;
import com.elearning.tm.android.client.model.TaskWBS;
import com.elearning.tm.android.client.util.DateTimeHelper;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class TaskAddActivity extends BaseActivity {
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

	private static String[] spfrom = new String[] { "project", "projectid" };
	private static int[] spto = new int[] { R.id.simple_spitem_display,
			R.id.simple_spdditem_display };

	List<ProjectInfo> projectList;
	List<TaskWBS> projectWbsList;

	List<Map<String, String>> projectMapList;
	List<Map<String, String>> projectWbsMapList;

	private SimpleAdapter projectAdapter;
	private SimpleAdapter projectWbsAdapter;
	int selpos; //当前选中的下拉选项的位置
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.create_task);

		initEditor();
		initialSpinner();
		
		//保存按钮事件
		save = (Button)this.findViewById(R.id.top_send_btn);
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

		content = (EditText)this.findViewById(R.id.createtask_taskcontent_edittext);
		title = (EditText)this.findViewById(R.id.createtask_subjectcontent_edittext);
		expectTime = (EditText)this.findViewById(R.id.createtask_expectcost_edittext);
		costTime = (EditText)this.findViewById(R.id.createtask_realcost_edittext);
		// 初始化日期按钮
		Calendar today = Calendar.getInstance();
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");

		if (!TextUtils.isEmpty(date)) {
			java.util.Date dt = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dt=df.parse(date);
				today.setTime(dt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	
	
		begindate = (Button) this
				.findViewById(R.id.createtask_begindate_button);
		begindate.setText(DateTimeHelper.fmtDate(today.getTime()));
		begindate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openDatePicker(TaskAddActivity.this, begindate);
			}
		});

		enddate = (Button) this.findViewById(R.id.createtask_enddate_button);
		enddate.setText(DateTimeHelper.fmtDate(today.getTime()));
		enddate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openDatePicker(TaskAddActivity.this, enddate);
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
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fromEditor.setAdapter(adapter);

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

		reloadSpinnerData();

		project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
						selpos = project.getSelectedItemPosition();
						TextView tv =	(TextView)view.findViewById(R.id.simple_spitem_display);
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
		
		if(list != null && list.size() != 0){
			projectList.addAll(list);
			for(ProjectInfo pi : projectList){
				Map<String, String> row = new HashMap<String, String>();
				
				row.put(spfrom[0], pi.getPID().toString());
				row.put(spfrom[1], pi.getPTitle());
				projectMapList.add(row);
			}
			
		}
		projectAdapter.notifyDataSetChanged();
//		project.setSelection(0, true);
//		
//		HashMap<String, String> item =  (HashMap<String, String>)project.getSelectedItem();
//    	String pid = item.get(spfrom[0]);
//		onProjectSelected(pid);
	}

    private void onProjectSelected(String pid) {
    	projectWbsList.clear();
    	projectWbsMapList.clear();
    	projectWbsAdapter.notifyDataSetChanged();
    	
    	NetWorkTaskWBSManager proWBSManager = new NetWorkTaskWBSManager();
    	List<TaskWBS> list = proWBSManager.QueryProjectWBSByPID(pid);
		
		if(list != null && list.size() != 0){
			projectWbsList.addAll(list);
			for(TaskWBS tw : projectWbsList){
				Map<String, String> row = new HashMap<String, String>();
				
				row.put(spfrom[0], tw.getTaskID().toString());
				row.put(spfrom[1], tw.getName());
				projectWbsMapList.add(row);
			}
		}
		projectWbsAdapter.notifyDataSetChanged();
    }

    private void doSave(){
    	HashMap<String, String> item =  (HashMap<String, String>)project.getSelectedItem();
    	String pid = item.get(spfrom[0]);
    	
    	HashMap<String, String> item1 =  (HashMap<String, String>)projectWbs.getSelectedItem();
    	String tid = item1.get(spfrom[0]);
    	
    	String titleValue = title.getText().toString();   
    	if(TextUtils.isEmpty(titleValue)){
    		title.requestFocus();
    		return;
    	}
    	String beginDate = begindate.getText().toString();
    	String endDate = enddate.getText().toString();
    	
    	SimpleDateFormat parseTime = new SimpleDateFormat("yyyy-MM-dd"); 
    	Date begin;
    	Date end;
		try {
			begin = parseTime.parse(beginDate);
			end = parseTime.parse(endDate);
			if(begin.compareTo(end) > 0){
				Toast.makeText(this, "开始时间不能大于结束时间.", Toast.LENGTH_SHORT);
				return;
	    	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
    	String contentValue = content.getText().toString(); 
    	
    	String expectTimeValue =expectTime.getText().toString();
    	if(TextUtils.isEmpty(expectTimeValue)){
    		expectTime.requestFocus();
    		return;
    	}
    	int planTime = Integer.parseInt(expectTimeValue);
    	
    	String realTimeValue =costTime.getText().toString();
    	if(TextUtils.isEmpty(realTimeValue)){
    		costTime.requestFocus();
    		return;
    	}
    	int realTime = Integer.parseInt(realTimeValue);
    	int status = fromEditor.getSelectedItemPosition();
    	
    	
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
}
