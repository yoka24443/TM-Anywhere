package com.elearning.tm.android.client.view.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.StartPage;
import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.view.AboutActivity;
import com.elearning.tm.android.client.view.LoginActivity;

public class BaseActivity extends Activity {

	private static final String TAG = "BaseActivity";

	protected SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_onCreate(savedInstanceState);
	}

	// 因为onCreate方法无法返回状态，因此无法进行状态判断，
	// 为了能对上层返回的信息进行判断处理，我们使用_onCreate代替真正的
	// onCreate进行工作。onCreate仅在顶层调用_onCreate。
	protected boolean _onCreate(Bundle savedInstanceState) {

		if (!checkIsLogedIn()) {
			return false;
		} else {
			mPreferences = TMApplication.tmPref;
			return true;
		}
	}

	protected void handleLoggedOut() {
		if (isTaskRoot()) {
			showLogin();
		} else {
			setResult(RESULT_LOGOUT);
		}
		finish();
	}

	public SharedPreferences getPreferences() {
		return mPreferences;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private static final int RESULT_LOGOUT = RESULT_FIRST_USER + 1;

	private void _logout() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.clear();
		editor.commit();
		handleLoggedOut();
	}

	public void logout() {
		Dialog dialog = new AlertDialog.Builder(BaseActivity.this).setTitle(
				"提示").setMessage("确实要注销吗?").setPositiveButton("确定",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						_logout();
					}

				}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create();
		dialog.show();
	}

	public void feedback() {
		String deviceModel = android.os.Build.MODEL;
		String versionRelease = android.os.Build.VERSION.RELEASE;
		String versionName = this.getString(R.string.help_version_string);
		String feedback = "@熊猫大侠爱地球 " + "# " + versionName + " #" +"/"
				+ deviceModel + "/" + versionRelease + "\n建议如下:";

		//Extra直接写mail地址字符串的方法貌似不行
		String[] receive = { this.getString(R.string.feedback_mail) };
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				receive);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				this.getString(R.string.feedback_subject));
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, feedback);
		startActivity(Intent.createChooser(emailIntent, this.getString(R.string.omenu_feedback)));
	}

	protected void showLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra(Intent.EXTRA_INTENT, getIntent());
		startActivity(intent);
	}


	// Menus.
	protected static final int OPTIONS_MENU_ID_LOGOUT = 1;
	protected static final int OPTIONS_MENU_ID_PREFERENCES = 2;
	protected static final int OPTIONS_MENU_ID_ABOUT = 3;
	protected static final int OPTIONS_MENU_ID_SEARCH = 4;
	protected static final int OPTIONS_MENU_ID_REPLIES = 5;
	protected static final int OPTIONS_MENU_ID_DM = 6;
	protected static final int OPTIONS_MENU_ID_TWEETS = 7;
	protected static final int OPTIONS_MENU_ID_TOGGLE_REPLIES = 8;
	protected static final int OPTIONS_MENU_ID_FOLLOW = 9;
	protected static final int OPTIONS_MENU_ID_UNFOLLOW = 10;
	protected static final int OPTIONS_MENU_ID_IMAGE_CAPTURE = 11;
	protected static final int OPTIONS_MENU_ID_PHOTO_LIBRARY = 12;
	protected static final int OPTIONS_MENU_ID_EXIT = 13;
	protected static final int OPTIONS_MENU_ID_FEEDBACK = 14; // 反馈信息按钮

	/**
	 * 如果增加了Option Menu常量的数量，则必须重载此方法， 以保证其他人使用常量时不产生重复
	 * 
	 * @return 最大的Option Menu常量
	 */
	protected int getLastOptionMenuId() {
		return OPTIONS_MENU_ID_FEEDBACK;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item;
		item = menu.add(0, OPTIONS_MENU_ID_FEEDBACK, 0, R.string.omenu_feedback);
		item.setIcon(android.R.drawable.ic_menu_send);

		item = menu.add(0, OPTIONS_MENU_ID_LOGOUT, 0, R.string.omenu_signout);
		item.setIcon(android.R.drawable.ic_menu_revert);

		item = menu.add(0, OPTIONS_MENU_ID_ABOUT, 0, R.string.omenu_about);
		item.setIcon(android.R.drawable.ic_menu_info_details);

		item = menu.add(0, OPTIONS_MENU_ID_EXIT, 0, R.string.omenu_exit);
		item.setIcon(android.R.drawable.ic_menu_close_clear_cancel);

		return true;
	}

	protected static final int REQUEST_CODE_LAUNCH_ACTIVITY = 0;
	protected static final int REQUEST_CODE_PREFERENCES = 1;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTIONS_MENU_ID_LOGOUT:
			logout();
			return true;
		case OPTIONS_MENU_ID_SEARCH:
			onSearchRequested();
			return true;
		case OPTIONS_MENU_ID_FEEDBACK:
			feedback();
			return true;
		case OPTIONS_MENU_ID_ABOUT:
			Intent intent = new Intent().setClass(this, AboutActivity.class);
			startActivity(intent);
			return true;
		case OPTIONS_MENU_ID_EXIT:
			exit();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void exit() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
	}

	protected void launchActivity(Intent intent) {
		startActivityForResult(intent, REQUEST_CODE_LAUNCH_ACTIVITY);
	}

	protected void launchDefaultActivity() {
		Intent intent = new Intent();
		intent.setClass(this, StartPage.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_PREFERENCES && resultCode == RESULT_OK) {
		} else if (requestCode == REQUEST_CODE_LAUNCH_ACTIVITY
				&& resultCode == RESULT_LOGOUT) {
			Log.d(TAG, "Result logout.");
			handleLoggedOut();
		}
	}

	protected boolean checkIsLogedIn() {
		 if (!TMApplication.instance.checkIsLogedIn()) { //代码存在问题,为了调试暂时注释掉
		 Log.d(TAG, "Not logged in.");
		 handleLoggedOut();
		 return false;
		 }
		return true;
	}

	public static boolean isTrue(Bundle bundle, String key) {
		return bundle != null && bundle.containsKey(key)
				&& bundle.getBoolean(key);
	}
}
