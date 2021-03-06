package com.elearning.tm.android.client.view.module;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elearning.tm.android.client.R;



public class SimpleFeedback implements Feedback, Widget {
	private static final String TAG = "SimpleFeedback";

	public static final int MAX = 100;

	private ProgressBar mProgress = null;

	public SimpleFeedback(Context context) {
		mProgress = (ProgressBar) ((Activity) context)
				.findViewById(R.id.progress_bar);
	}

	@Override
	public void start(CharSequence text) {
		mProgress.setProgress(20);
	}

	@Override
	public void success(CharSequence text) {
		mProgress.setProgress(100);
		resetProgressBar();
	}

	@Override
	public void failed(CharSequence text) {
		resetProgressBar();
		showMessage(text);
	}

	@Override
	public void cancel(CharSequence text) {

	}

	@Override
	public void update(Object arg0) {
		if (arg0 instanceof Integer) {
			mProgress.setProgress((Integer) arg0);
		} else if (arg0 instanceof CharSequence) {
			showMessage((String) arg0);
		}
	}

	@Override
	public void setIndeterminate(boolean indeterminate) {
		mProgress.setIndeterminate(indeterminate);
	}

	@Override
	public Context getContext() {
		if (mProgress != null) {
			return mProgress.getContext();
		}
		return null;
	}

	@Override
	public boolean isAvailable() {
		if (null == mProgress) {
			Log.e(TAG, "R.id.progress_bar is missing");
			return false;
		}
		return true;
	}

	public static int calProgressBySize(int total, int maxSize, List<?> list) {
		if (null != list) {
			return (MAX - (int) Math.floor(list.size() * (total / maxSize)));
		}
		return MAX;
	}

	private void resetProgressBar() {
		if (mProgress.isIndeterminate()) {
			// TODO: 第二次不会出现
			mProgress.setIndeterminate(false);
		}
		mProgress.setProgress(0);
	}

	private void showMessage(CharSequence text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
	}

}
