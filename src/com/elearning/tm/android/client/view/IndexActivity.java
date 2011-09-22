package com.elearning.tm.android.client.view;

import java.util.UUID;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class IndexActivity extends BaseActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
         
     // Handle Login Button
		final Button loginBtn = (Button) findViewById(R.id.btn_signin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText userAccount = (EditText) findViewById(R.id.et_account);
				final EditText password = (EditText) findViewById(R.id.et_psw);
				
				// Save new records
				ContentValues values = new ContentValues();
				values.put("UserAccount", userAccount.getText().toString());
				values.put("Password", password.getText().toString());
				values.put("UserID", UUID.randomUUID().toString());
				//db.insert("User_Info",values);
				
				new AlertDialog.Builder(IndexActivity.this).setMessage(
						userAccount.getText().toString() + password.getText().toString()).show();
				
				
			} 
		});
    }

}
