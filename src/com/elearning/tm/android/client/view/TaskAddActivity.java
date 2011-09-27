package com.elearning.tm.android.client.view;

import java.util.ArrayList;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.BaseActivity;

public class TaskAddActivity extends BaseActivity {
	Spinner fromEditor;
	ArrayList<String> list=new ArrayList<String>();
	ArrayAdapter<String> adapter;
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		setContentView(R.layout.create_task);
		initialSpinner();
	}
	
    private void initialSpinner() {
        fromEditor = (Spinner) findViewById(R.id.createtask_completionrate_spinner);
        String[]  ls=getResources().getStringArray(R.array.task_status);
        //获取XML中定义的数组
        for(int i=0;i<ls.length;i++){
        	list.add(ls[i]);
        }
        //把数组导入到ArrayList中
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //设置下拉菜单的风格
        fromEditor.setAdapter(adapter);
    }
}
