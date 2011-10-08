/**
 * AbstractTwitterListBaseLine用于抽象tweets List的展现
 * UI基本元素要求：一个ListView用于tweet列表
 *               一个ProgressText用于提示信息
 */
package com.elearning.tm.android.client.view.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.view.module.Feedback;
import com.elearning.tm.android.client.view.module.FeedbackFactory;
import com.elearning.tm.android.client.view.module.NavBar;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.elearning.tm.android.client.view.module.FeedbackFactory.FeedbackType;

public abstract class TwitterListBaseActivity extends BaseActivity implements Refreshable {

	static final String TAG = "TMListBaseActivity";
	protected TextView mProgressText;
	protected Feedback mFeedback;
	protected NavBar mNavbar;

	protected static final String SIS_RUNNING_KEY = "running";
	
	abstract protected int getLayoutId();

	abstract protected ListView getTMList();

	abstract protected TmAdapter getTMAdapter();

	abstract protected void setupState();

	abstract protected String getActivityTitle();

	abstract protected TaskInfo getContextItemTask(int position);

	abstract protected void updateTask(TaskInfo task);

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			setContentView(getLayoutId());
			mNavbar = new NavBar(NavBar.HEADER_STYLE_HOME, this);
			mFeedback = FeedbackFactory.create(this, FeedbackType.PROGRESS);
			// 提示栏
			mProgressText = (TextView) findViewById(R.id.progress_text);
			setupState();
			registerOnClickListener(getTMList());
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsLogedIn();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void draw() {
		getTMAdapter().refresh();
	}

	protected void goTop() {
		getTMList().setSelection(1);
	}

	protected void adapterRefresh() {
		getTMAdapter().refresh();
	}

	protected void specialItemClicked(int position) {

	}

	protected void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TaskInfo task = getContextItemTask(position);
				if (task == null) {
					specialItemClicked(position);
				} else {
					//launchActivity(StatusActivity.createIntent(tweet));
				}
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}