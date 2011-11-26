package com.elearning.tm.android.client.view.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.view.module.Feedback;
import com.elearning.tm.android.client.view.module.FeedbackFactory;
import com.elearning.tm.android.client.view.module.NavBar;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.elearning.tm.android.client.view.module.FeedbackFactory.FeedbackType;


public abstract class UserListBaseActivity extends BaseActivity implements Refreshable {
	static final String TAG = "ListBaseActivity";

	protected TextView mProgressText;
	protected NavBar mNavbar;
	protected Feedback mFeedback;

	protected static final int STATE_ALL = 0;
	protected static final String SIS_RUNNING_KEY = "running";

	abstract protected int getLayoutId();

	abstract protected ListView getUserList();

	abstract protected TmAdapter getUserAdapter();

	abstract protected void setupState();

	abstract protected String getActivityTitle();

	abstract protected boolean useBasicMenu();

	abstract protected UserInfo getContextItemUser(int position);

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			setContentView(getLayoutId());
			mNavbar = new NavBar(NavBar.HEADER_STYLE_SEARCH, this);
			mFeedback = FeedbackFactory.create(this, FeedbackType.PROGRESS);
			mProgressText = (TextView) findViewById(R.id.progress_text);
			setupState();
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

	private void draw() {
		getUserAdapter().refresh();
	}

	private void goTop() {
		getUserList().setSelection(0);
	}

	protected void adapterRefresh() {
		getUserAdapter().refresh();
	}

	protected void specialItemClicked(int position) {

	}
	
}
