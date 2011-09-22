package com.elearning.tm.android.client.view;

import com.elearning.tm.android.client.R;


import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
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
		setContentView(R.layout.front);
		
		final Button concel = (Button) findViewById(R.id.title_left_button);
		final Button submit = (Button) findViewById(R.id.title_right_button);
		final TextView title = (TextView) findViewById(R.id.title_text);
		concel.setText(R.string.btn_cancel);
		submit.setText(R.string.btn_save);
		title.setText(R.string.tasklist_title);

		myTabhost = this.getTabHost();
		group = (RadioGroup)findViewById(R.id.main_radio);
		// 设置TabHost的背景颜色blue
		//myTabhost.setBackgroundColor(Color.argb(150, 22, 70, 150));

		TabHost.TabSpec spec = null;
		// 通过Intent，将activity导入， 对于intent，这个类不能 是内部类，这个类必须在AndroidManifest中注册的类
		Intent task = new Intent();
		task.setClass(this, TastListActivity.class);
		spec = myTabhost.newTabSpec("Tag1");
		// spec.setIndicator("Intent",getResources().getDrawable(R.drawable.tab_1));
		spec.setIndicator("Intent1");
		spec.setContent(task);
		myTabhost.addTab(spec);

		Intent calender = new Intent();
		calender.setClass(this, TaskAddActivity.class);
		spec = myTabhost.newTabSpec("Tag2");
		spec.setIndicator("Intent2");
		spec.setContent(calender);
		myTabhost.addTab(spec);

		Intent talk = new Intent();
		talk.setClass(this, IndexActivity.class);
		spec = myTabhost.newTabSpec("Tag3");
		spec.setIndicator("Intent3");
		spec.setContent(talk);
		myTabhost.addTab(spec);

		Intent report = new Intent();
		report.setClass(this, ExListView.class);
		spec = myTabhost.newTabSpec("Tag4");
		spec.setIndicator("Intent4");
		spec.setContent(report);
		myTabhost.addTab(spec);
		
		Intent about = new Intent();
		about.setClass(this, TaskExListView.class);
		spec = myTabhost.newTabSpec("Tag5");
		spec.setIndicator("Intent4");
		spec.setContent(about);
		myTabhost.addTab(spec);

		myTabhost.setCurrentTab(1);//设置第一次打开时默认显示的标签，参数代表其添加到标签中的顺序，位置是从0开始的哦

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
