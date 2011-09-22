package com.elearning.tm.android.client.view;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class WorkListActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index_view);

		// Fill ListView from database
		fillTaskList();
	}

	private void fillTaskList() {
		
//		Cursor c = db.query("select UserID  as _id,UserAccount,Password from [User_Info]",
//				null);
//		startManagingCursor(c);
//		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.index_row_list_item,
//				c, new String[] { "UserAccount", "Password" }, new int[] {
//						R.id.index_row_list_item_note, R.id.index_row_list_item_time });
//
//		ListView av = (ListView) findViewById(R.id.indexList);
//		av.setAdapter(adapter);
	}
}
