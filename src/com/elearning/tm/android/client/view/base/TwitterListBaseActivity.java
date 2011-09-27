/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * AbstractTwitterListBaseLine用于抽象tweets List的展现
 * UI基本元素要求：一个ListView用于tweet列表
 *               一个ProgressText用于提示信息
 */
package com.elearning.tm.android.client.view.base;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.view.module.Feedback;
import com.elearning.tm.android.client.view.module.FeedbackFactory;
import com.elearning.tm.android.client.view.module.NavBar;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.elearning.tm.android.client.view.module.FeedbackFactory.FeedbackType;

public abstract class TwitterListBaseActivity extends BaseActivity implements
		Refreshable {

	static final String TAG = "TMListBaseActivity";
	protected TextView mProgressText;
	protected Feedback mFeedback;
	protected NavBar mNavbar;

	protected static final String SIS_RUNNING_KEY = "running";

	abstract protected int getLayoutId();

	abstract protected ListView getTaskList();

	abstract protected TmAdapter getTaskAdapter();

	abstract protected TaskInfo getContextItemTask(int position);

	abstract protected void specialItemClicked(int position);

	@Override
	protected void onResume() {
		super.onResume();
		checkIsLogedIn();
	}

	protected void draw() {
		getTaskAdapter().refresh();
	}

	protected void goTop() {
		getTaskList().setSelection(1);
	}

	protected void adapterRefresh() {
		getTaskAdapter().refresh();
	}

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		if (super._onCreate(savedInstanceState)) {
			setContentView(getLayoutId());
			mNavbar = new NavBar(NavBar.HEADER_STYLE_HOME, this);
			mFeedback = FeedbackFactory.create(this, FeedbackType.PROGRESS);
			// 提示栏
			mProgressText = (TextView) findViewById(R.id.progress_text);
			return true;
		} else {
			return false;
		}
	}

}