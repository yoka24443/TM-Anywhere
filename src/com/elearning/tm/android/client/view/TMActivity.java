///*
// * Copyright (C) 2009 Google Inc.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.elearning.tm.android.client.view;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.elearning.tm.android.client.model.TaskInfo;
//import com.elearning.tm.android.client.task.GenericTask;
//import com.elearning.tm.android.client.task.TaskAdapter;
//import com.elearning.tm.android.client.task.TaskListener;
//import com.elearning.tm.android.client.task.TaskParams;
//import com.elearning.tm.android.client.task.TaskResult;
//import com.elearning.tm.android.client.view.base.TwitterCursorBaseActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//
//
//public class TMActivity extends TwitterCursorBaseActivity {
//	private static final String TAG = "TwitterActivity";
//	private static final String LAUNCH_ACTION = "com.ch_linghu.fanfoudroid.TWEETS";
//
//	public static Intent createIntent(Context context) {
//		Intent intent = new Intent(LAUNCH_ACTION);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//		return intent;
//	}
//
//	public static Intent createNewTaskIntent(Context context) {
//		Intent intent = createIntent(context);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//		return intent;
//	}
//
//	@Override
//	protected boolean _onCreate(Bundle savedInstanceState) {
//		if (super._onCreate(savedInstanceState)) {
//			mNavbar.setHeaderTitle("饭否fanfou.com");
//			// 仅在这个页面进行schedule的处理
//			manageUpdateChecks();
//
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//	}
//
//	// Menu.
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	protected Cursor fetchMessages() {
//		return getDb().fetchAllTweets(getUserId(), StatusTable.TYPE_HOME);
//	}
//
//	@Override
//	protected String getActivityTitle() {
//		return getResources().getString(R.string.page_title_home);
//	}
//
//	@Override
//	protected void markAllRead() {
//		getDb().markAllTweetsRead(getUserId(), StatusTable.TYPE_HOME);
//	}
//
//	// hasRetrieveListTask interface
//	@Override
//	public int addMessages(ArrayList<TaskInfo> task, boolean isUnread) {
//		// 获取消息的时候，将status里获取的user也存储到数据库
//
//		// ::MARK::
//		for (Tweet t : tweets) {
//			getDb().createWeiboUserInfo(t.user);
//		}
//		return getDb().putTweets(tweets, getUserId(), StatusTable.TYPE_HOME,
//				isUnread);
//	}
//
//	@Override
//	public String fetchMaxId() {
//		return getDb().fetchMaxTweetId(getUserId(), StatusTable.TYPE_HOME);
//	}
//
//	@Override
//	public List<TaskInfo> getMessageSinceId(String maxId) throws HttpException {
//		if (maxId != null) {
//			return getApi().getFriendsTimeline(new Paging(maxId));
//		} else {
//			return getApi().getFriendsTimeline();
//		}
//	}
//
//	public void onDeleteFailure() {
//		Log.e(TAG, "Delete failed");
//	}
//
//	public void onDeleteSuccess() {
//		mTweetAdapter.refresh();
//	}
//	
//	@Override
//	public String fetchMinId() {
//		return getDb().fetchMinTweetId(getUserId(), StatusTable.TYPE_HOME);
//	}
//
//	@Override
//	public List<Status> getMoreMessageFromId(String minId) throws HttpException {
//		Paging paging = new Paging(1, 20);
//		paging.setMaxId(minId);
//		return getApi().getFriendsTimeline(paging);
//	}
//
//	@Override
//	public int getDatabaseType() {
//		return StatusTable.TYPE_HOME;
//	}
//
//	@Override
//	public String getUserId() {
//		return TwitterApplication.getMyselfId();
//	}
//}