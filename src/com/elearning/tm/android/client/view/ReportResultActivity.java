package com.elearning.tm.android.client.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.report.BalanceActivity;
import com.elearning.tm.android.client.view.report.ProjectPieReportActivity;
import com.elearning.tm.android.client.view.report.TaskBarReportActivity;



public class ReportResultActivity extends TabActivity {

	private static final String TAG = ReportResultActivity.class.getSimpleName();
	private TabHost tabHost;
	private RadioGroup mainbtGroup;
	private static final String DISTRIBUTION = "项目分布";
	private static final String DETAIL = "项目明细";
	private static final String TOTAL = "工时统计";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_result);
		initDateRegion();
		tabHost = this.getTabHost();

		View view1 = View.inflate(ReportResultActivity.this, R.layout.header_report, null);
		((TextView) view1.findViewById(R.id.tab_1)).setText(DISTRIBUTION);

		TabHost.TabSpec spec1 = tabHost.newTabSpec(DISTRIBUTION)
				.setIndicator(view1)
				.setContent(new Intent(this, ProjectPieReportActivity.class));
		tabHost.addTab(spec1);
		
		View view2 = View.inflate(ReportResultActivity.this, R.layout.header_report, null);
		((TextView) view2.findViewById(R.id.tab_1)).setText(DETAIL);

		TabHost.TabSpec spec2 = tabHost.newTabSpec(DETAIL)
				.setIndicator(view2)
				.setContent(new Intent(this, BalanceActivity.class));
		tabHost.addTab(spec2);
		
		View view3 = View.inflate(ReportResultActivity.this, R.layout.header_report, null);
		((TextView) view3.findViewById(R.id.tab_1)).setText(TOTAL);

		TabHost.TabSpec spec3 = tabHost.newTabSpec(TOTAL)
				.setIndicator(view3)
				.setContent(new Intent(this, TaskBarReportActivity.class));
		tabHost.addTab(spec3);
	}
	
	private void initDateRegion(){
		Intent intent = getIntent();
		String begin = intent.getStringExtra("begin");
		String end = intent.getStringExtra("end");
		TextView during = (TextView)this.findViewById(R.id.dateregion);
		during.setText(begin + "至" + end +"期间");
		
	}
	
}
