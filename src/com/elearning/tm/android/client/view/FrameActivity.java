package com.elearning.tm.android.client.view;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.module.BaseCalendar;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FrameActivity extends TabActivity  {

	private TabHost myTabhost;
	private RadioGroup group;
	
	public static final String TAB_HOME="tabHome";
	public static final String TAB_MES="tabMes";
	public static final String TAB_TOUCH="tab_touch";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.footer);
		
//		final Button concel = (Button) findViewById(R.id.title_left_button);
//		final Button submit = (Button) findViewById(R.id.title_right_button);
//		final TextView title = (TextView) findViewById(R.id.title_text);
//		concel.setText(R.string.btn_cancel);
//		submit.setText(R.string.btn_save);
//		title.setText(R.string.tasklist_title);

		myTabhost = (TabHost)this.findViewById(android.R.id.tabhost);//this.getTabHost();
		group = (RadioGroup)findViewById(R.id.main_radio);

		TabHost.TabSpec spec = null;

		//首页
		Intent task = new Intent();
		task.setClass(this, TMActivity.class);
		spec = myTabhost.newTabSpec("Tag1");
		spec.setIndicator("Intent1");
		spec.setContent(task);
		myTabhost.addTab(spec);

		//添加任务
		Intent calender = new Intent();
		calender.setClass(this, BaseCalendar.class);
		spec = myTabhost.newTabSpec("Tag2");
		spec.setIndicator("Intent2");
		spec.setContent(calender);
		myTabhost.addTab(spec);

		//报表
		Intent talk = new Intent();
		//talk.setClass(this, StartPage.class);
//		talk.setClass(this, TastListActivity.class);
		talk.setClass(this, SearchActivity.class);
		spec = myTabhost.newTabSpec("Tag3");
		spec.setIndicator("Intent3");
		spec.setContent(talk);
		myTabhost.addTab(spec);

		//发送短信
		Intent report = new Intent();
		report.setClass(this, ContactActivity.class);
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
