package com.elearning.tm.android.client.view.module;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.StartPage;
import com.elearning.tm.android.client.view.AboutActivity;
import com.elearning.tm.android.client.view.ExListView;
import com.elearning.tm.android.client.view.TaskAddActivity;
import com.elearning.tm.android.client.view.TastListActivity;


import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;

import android.widget.RadioGroup.OnCheckedChangeListener;

public class BottomMenu extends TabActivity  {

	private TabHost myTabhost;
	private RadioGroup group;
	
	public static final String TAB_HOME="tabHome";
	public static final String TAB_MES="tabMes";
	public static final String TAB_TOUCH="tab_touch";
	
	public BottomMenu(Context context, AttributeSet attrs) {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.footer);
		
		myTabhost = this.getTabHost();
		group = (RadioGroup)findViewById(R.id.main_radio);
		TabHost.TabSpec spec = null;

		//首页
		Intent task = new Intent();
		task.setClass(this, TastListActivity.class);
		spec = myTabhost.newTabSpec("Tag1");
		spec.setIndicator("Intent1");
		spec.setContent(task);
		myTabhost.addTab(spec);

		//添加任务
		Intent calender = new Intent();
		calender.setClass(this, TaskAddActivity.class);
		spec = myTabhost.newTabSpec("Tag2");
		spec.setIndicator("Intent2");
		spec.setContent(calender);
		myTabhost.addTab(spec);

		//报表
		Intent talk = new Intent();
		talk.setClass(this, StartPage.class);
		spec = myTabhost.newTabSpec("Tag3");
		spec.setIndicator("Intent3");
		spec.setContent(talk);
		myTabhost.addTab(spec);

		//发送短信
		Intent report = new Intent();
		report.setClass(this, ExListView.class);
		spec = myTabhost.newTabSpec("Tag4");
		spec.setIndicator("Intent4");
		spec.setContent(report);
		myTabhost.addTab(spec);
		
		//关于
		Intent about = new Intent();
		about.setClass(this, AboutActivity.class);
		spec = myTabhost.newTabSpec("Tag5");
		spec.setIndicator("Intent4");
		spec.setContent(about);
		myTabhost.addTab(spec);

		myTabhost.setCurrentTab(0);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button0:
					myTabhost.setCurrentTabByTag("Tag1");
					break;
				case R.id.radio_button1:
					myTabhost.setCurrentTabByTag("Tag2");
					break;
				case R.id.radio_button2:
					myTabhost.setCurrentTabByTag("Tag3");
					break;
				case R.id.radio_button3:
					myTabhost.setCurrentTabByTag("Tag4");
					break;
				case R.id.radio_button4:
					myTabhost.setCurrentTabByTag("Tag5");
					break;
				default:
					break;
				}
			}
		});
		
	}

}
