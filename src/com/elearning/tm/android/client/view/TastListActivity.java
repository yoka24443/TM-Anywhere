package com.elearning.tm.android.client.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.StartPage;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class TastListActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showpets);

		// Fill ListView from database
		fillTaskList();

		// Handle Go enter more pets button
		final Button gotoEntry = (Button) findViewById(R.id.ButtonEnterMorePets);
		gotoEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(TastListActivity.this,
						TaskAddActivity.class));
				finish();
			}
		});
	}

	private void fillTaskList() {
//		Cursor c = db.query("select UserID  as _id,UserAccount,Password from [User_Info]",
//				null);
//		startManagingCursor(c);
//		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.pet_item,
//				c, new String[] { "UserAccount", "Password" }, new int[] {
//						R.id.TextView_PetName, R.id.TextView_PetType });
//
//		ListView av = (ListView) findViewById(R.id.petList);
//		av.setAdapter(adapter);
		
		//Ϊÿ�������ӵ���ļ�����
////		av.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//	            public void onItemClick(
//	                AdapterView<?> parent, View view, int position, long id) {
//	            	// Check for delete button
//	            	final long deletePetId =  id;
//	            	
//	            	// Use an Alert dialog to confirm delete operation
//					new AlertDialog.Builder(TastListActivity.this).setMessage(
//					"Delete Pet Record?").setPositiveButton(
//					"Delete", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							
//							// Delete that pet
////	                		db.del("User_Info", 1);	
//
//	                		// Refresh the data in our cursor and therefore our List
//	                		//c.requery();
//	                	}
//	        		}).show();
//	            }
//	        });

	}
}
