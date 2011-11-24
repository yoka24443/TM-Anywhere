package com.elearning.tm.android.client.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;
import com.elearning.tm.android.client.view.module.NavBar;


public class UpdateActivity extends BaseActivity {
	NavBar mNavbar;
	private Dialog gngDialog;
	private TextView dialogContent;
	private ImageView dialogImage;
	private Button update;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_update_view);
		mNavbar = new NavBar(NavBar.HEADER_STYLE_BACK, this);
		mNavbar.setHeaderTitle("版本更新");
		showDialog();
		
		update = (Button)this.findViewById(R.id.btn_update);
		update.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				gngDialog.show();
			}
		});
	}
	
	private void showDialog(){
		gngDialog = new Dialog(this);
    	gngDialog.setContentView(R.layout.gngdialog);
    	dialogContent = (TextView)gngDialog.findViewById(R.id.dialogText);
    	dialogImage = (ImageView)gngDialog.findViewById(R.id.dialogImage);
    	gngDialog.setTitle("关于本软件");
    	dialogContent.setText(getString(R.string.info_content));
    	dialogImage.setImageResource(R.drawable.dialog);
	}
}
