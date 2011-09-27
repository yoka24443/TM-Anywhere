package com.elearning.tm.android.client.view.base;

import android.os.Bundle;
import android.widget.TextView;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.module.Feedback;
import com.elearning.tm.android.client.view.module.FeedbackFactory;
import com.elearning.tm.android.client.view.module.NavBar;
import com.elearning.tm.android.client.view.module.FeedbackFactory.FeedbackType;



public abstract class UserListBaseActivity extends BaseActivity implements Refreshable {
	static final String TAG = "UserListBaseActivity";

	protected TextView mProgressText;
	protected NavBar mNavbar;
	protected Feedback mFeedback;

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			mNavbar = new NavBar(NavBar.HEADER_STYLE_HOME, this);
			mFeedback = FeedbackFactory.create(this, FeedbackType.PROGRESS);
			mProgressText = (TextView) findViewById(R.id.progress_text);
			return true;
		} else {
			return false;
		}
	}
}
