//package com.elearning.tm.android.client.view;
//
//import java.text.MessageFormat;
//import java.util.List;
//
//import com.elearning.tm.android.client.model.Paging;
//import com.elearning.tm.android.client.model.UserInfo;
//import com.elearning.tm.android.client.view.module.UserArrayAdapter;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ListView;
//
//
//
//public class FollowersActivity extends UserArrayBaseActivity {
//
//	private ListView mUserList;
//	private UserArrayAdapter mAdapter;
//	private static final String TAG = "FollowersActivity";
//
//	private String userId;
//	private String userName;
//	private static final String LAUNCH_ACTION = "com.ch_linghu.fanfoudroid.FOLLOWERS";
//	private static final String USER_ID = "userId";
//	private static final String USER_NAME = "userName";
//	private int currentPage = 1;
//	private int followersCount = 0;
//	private static final double PRE_PAGE_COUNT = 100.0;// 官方分页为每页100
//	private int pageCount = 0;
//
//	private String[] ids;
//
//	@Override
//	protected boolean _onCreate(Bundle savedInstanceState) {
//
//		Intent intent = getIntent();
//		Bundle extras = intent.getExtras();
//		if (extras != null) {
//			this.userId = extras.getString(USER_ID);
//			this.userName = extras.getString(USER_NAME);
//		} else {
//			// 获取登录用户id
//			userId = TwitterApplication.getMyselfId();
//			userName = TwitterApplication.getMyselfName();
//		}
//
//		if (super._onCreate(savedInstanceState)) {
//
//			String myself = TwitterApplication.getMyselfId();
//			if (getUserId() == myself) {
//				mNavbar.setHeaderTitle(MessageFormat.format(
//						getString(R.string.profile_followers_count_title), "我"));
//			} else {
//				mNavbar.setHeaderTitle(MessageFormat.format(
//						getString(R.string.profile_followers_count_title),
//						userName));
//			}
//			return true;
//
//		} else {
//			return false;
//		}
//
//	}
//
//	public static Intent createIntent(String userId, String userName) {
//		Intent intent = new Intent(LAUNCH_ACTION);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra(USER_ID, userId);
//		intent.putExtra(USER_NAME, userName);
//		return intent;
//	}
//
//	@Override
//	public Paging getNextPage() {
//		currentPage += 1;
//		return new Paging(currentPage);
//	}
//
//	@Override
//	protected String getUserId() {
//		return this.userId;
//	}
//
//	@Override
//	public Paging getCurrentPage() {
//
//		return new Paging(this.currentPage);
//	}
//
//	@Override
//	protected List<UserInfo> getUsers(String userId, Paging page) {
//		return getApi().getFollowersList(userId, page);
//	}
//
//}
