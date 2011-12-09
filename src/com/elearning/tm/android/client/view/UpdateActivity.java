package com.elearning.tm.android.client.view;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.manage.DownLoadManager;
import com.elearning.tm.android.client.manage.UpdateManager;
import com.elearning.tm.android.client.model.UpdataInfo;
import com.elearning.tm.android.client.view.base.BaseActivity;
import com.elearning.tm.android.client.view.module.NavBar;


public class UpdateActivity extends BaseActivity {
	private static final String TAG = "ContactActivity";
	private static final int UPDATA_CLIENT = 0;
	private static final int GET_UNDATAINFO_ERROR = 1;
	private static final int DOWN_ERROR = 2;
	
	NavBar mNavbar;
	private Button update;
	private UpdataInfo info;
	private String versionname;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_update_view);
		mNavbar = new NavBar(NavBar.HEADER_STYLE_BACK, this);
		mNavbar.setHeaderTitle("版本更新");
//		new Thread(new CheckVersionTask()).start();
		try {
			versionname = getVersionName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		update = (Button)this.findViewById(R.id.btn_update);
		update.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new CheckVersionTask()).start();
//				showUpdataDialog();
			}
		});
	}
	
	/* 
	 * 获取当前程序的版本号  
	 */  
	private String getVersionName() throws Exception{  
	    //获取packagemanager的实例   
	    PackageManager packageManager = getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息  
	    PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);  
	    return packInfo.versionName;   
	}  
	
	
	
	/*
	 * 从服务器获取xml解析并进行比对版本号 
	 */
	public class CheckVersionTask implements Runnable{

		public void run() {
			try {
				//从资源文件获取服务器 地址 
				String path = getResources().getString(R.string.serverurl);
				//包装成url的对象 
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection(); 
				conn.setConnectTimeout(5000);
				InputStream is =conn.getInputStream(); 
				info =  UpdateManager.getUpdataInfo(is);
				
				if(info.getVersion().equals(versionname)){
					Log.i(TAG,"版本号相同无需升级");
					LoginMain();
				}else{
					Log.i(TAG,"版本号不同 ,提示用户升级 ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 待处理 
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} 
		}
	}
	
	Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				//对话框通知用户升级程序 
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//服务器超时 
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show();
				LoginMain();
				break;	
			case DOWN_ERROR:
				//下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				LoginMain();
				break;	
			}
		}
	};
	
	/*
	 * 
	 * 弹出对话框通知用户更新程序 
	 * 
	 * 弹出对话框的步骤：
	 * 	1.创建alertDialog的builder.  
	 *	2.要给builder设置属性, 对话框的内容,样式,按钮
	 *	3.通过builder 创建一个对话框
	 *	4.对话框show()出来  
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this) ; 
		builer.setTitle("版本升级");
		builer.setMessage(info.getDescription());
		//当点确定按钮时从服务器上下载 新的apk 然后安装 
		builer.setPositiveButton("确定", new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG,"下载apk,更新");
				downLoadApk();
			}   
		});
		//当点取消按钮时进行登录
		builer.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	
	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		final ProgressDialog pd;	//进度条对话框
		pd = new  ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread(){
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
					sleep(3000);
					installApk(file);
					pd.dismiss(); //结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}}.start();
	}
	
	//安装apk 
	protected void installApk(File file) {
		Intent intent = new Intent();
		//执行动作
		intent.setAction(Intent.ACTION_VIEW);
		//执行的数据类型
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	/*
	 * 进入程序的主界面
	 */
	private void LoginMain(){
		Intent intent = new Intent(this,UpdateActivity.class);
		startActivity(intent);
		//结束掉当前的activity 
		this.finish();
	}
	
	
	
}
