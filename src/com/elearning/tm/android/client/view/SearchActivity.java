package com.elearning.tm.android.client.view;

import java.util.Calendar;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.util.DateTimeHelper;
import com.elearning.tm.android.client.view.base.BaseActivity;
import com.elearning.tm.android.client.view.module.NavBar;



public class SearchActivity extends BaseActivity {

	private static final String TAG = SearchActivity.class.getSimpleName();

	private ImageButton start_day_button;
	private ImageButton end_day_button;
	private EditText start_text;
	private EditText end_text;
	private Button query;
	private NavBar mNavbar;


	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			setContentView(R.layout.search);
			mNavbar = new NavBar(NavBar.HEADER_STYLE_TITLE, this);
			mNavbar.setHeaderTitle("报表");
			
			// 初始化日期按钮
			Calendar mon = Calendar.getInstance();
			Calendar monday = DateTimeHelper.getADayOfWeek(mon, Calendar.MONDAY);
			
			Calendar sun = Calendar.getInstance();
			Calendar sunday = DateTimeHelper.getADayOfWeek(sun, Calendar.SUNDAY);
			
			start_day_button = (ImageButton)this.findViewById(R.id.start_day_button);
			end_day_button = (ImageButton)this.findViewById(R.id.end_day_button);
			
			start_text = (EditText)this.findViewById(R.id.start_text);
			end_text = (EditText)this.findViewById(R.id.end_text);
			
			start_text.setText(DateTimeHelper.fmtDate(monday.getTime()));
			start_day_button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					openDatePicker(SearchActivity.this, start_text);
				}
			});
			
			end_text.setText(DateTimeHelper.fmtDate(sunday.getTime()));
			end_day_button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					openDatePicker(SearchActivity.this, end_text);
				}
			});
			
			//保存按钮事件
			query = (Button)this.findViewById(R.id.query_button);
			query.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					doQuery();
				}
			});
			
			return true;
		} else {
			return false;
		}
	}


	protected void doQuery() {
		Intent intent = new Intent().setClass(this, ReportResultActivity.class);

		String begin = start_text.getText().toString();
		String end = end_text.getText().toString();
		intent.putExtra("begin", begin);
		intent.putExtra("end", end);
		startActivity(intent);
	}


	@Override
	protected void onResume() {
		super.onResume();
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
