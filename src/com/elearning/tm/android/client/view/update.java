/*package com.iStudyV2;

*//**
 * 功能：欢迎界面
 * 
 * **//*

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;

import com.BRule.Function; 

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent; 
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class Welcome extends Activity {
	private static final String TAG = "Main";
	 
		public static final String UPDATE_APKNAME = "studyV2.apk";
		public static final String UPDATE_VERJSON = "version.txt";
		public static final String UPDATE_SAVENAME = "StudyV2.apk";
		public static final String PACKAPG_NAME = "com.iStudyV2";
		
		public static final String SAVE_PATH = "/sdcard/iStudy/";  //下载后的存储文件夹
		
		public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

		private ProgressDialog mProgressDialog;
		public static String nowApk_Name ="";
		private Handler handler = new Handler();
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        //no title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		setContentView(R.layout.welcome);

        nowApk_Name = getVerName(this);  //手机客户端 APK 版本名
        
        try {
        	//创建 下载 文件夹 （第一次进入系统），用于保存 课程下载资料
            Function.createDir(SAVE_PATH);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
		}
        
         
    }  
    
        
    *//**
	 * onResume
	 *//*
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			
		
		//判断网络状态
    	if(checkNetWorkStatus()== false)
    	{
    		Toast.makeText(this, getText(R.string.noLink), Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//获取服务器 APK版本号
		        String serverVer = Function.GetVersion();
		        Log.i("error:",serverVer);
		        if((nowApk_Name.equals(serverVer)==false)&&(serverVer.equals(null)==false)&&(serverVer.equals("")==false))
		        {  
		        	 StringBuffer sb = new StringBuffer();   

		        	    sb.append("当前版本:");   

		        	    sb.append(nowApk_Name);     

		        	    sb.append(", 发现新版本:");   

		        	    sb.append(serverVer);    

		        	    sb.append(", 是否更新?");   

		        	    Dialog dialog = new AlertDialog.Builder(Welcome.this)   

		        	            .setTitle("软件更新")   

		        	            .setMessage(sb.toString())   

		        	            // 设置内容   

		        	            .setPositiveButton("更新",// 设置确定按钮   

		        	                    new DialogInterface.OnClickListener() {   

		        	                        @Override  

		        	                        public void onClick(DialogInterface dialog,   

		        	                                int which) {   

		        	                        	startDownload();

		        	                        }   

		        	                    })   

		        	            .setNegativeButton("暂不更新",   

		        	                    new DialogInterface.OnClickListener() {   

		        	                        public void onClick(DialogInterface dialog,   

		        	                                int whichButton) {   

		        	                            // 点击"取消"按钮之后退出程序   
		        	                        	startActivity(new Intent(Welcome.this, LoginActivity.class)); 
		        	                            finish();   

		        	                        }   

		        	                    }).create();// 创建   

		        	    // 显示对话框   

		        	    dialog.show();   


		        	
		        }
		        else
		        {
		        	startActivity(new Intent(Welcome.this, LoginActivity.class));
					finish();
		        }
		         
			}
		}.start();
		
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, getText(R.string.noLink), Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	 private boolean checkNetWorkStatus() {    
	        boolean netSataus = false;    
	            
	        try {
	        	 ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);    
	  	       // cwjManager.getActiveNetworkInfo();    
	  	        if (cwjManager.getActiveNetworkInfo() != null) {    
	  	            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();    
	  	        }    
			} catch (Exception e) {
				// TODO: handle exception
			}
	       
	        return netSataus;  
	        
	        
	    }    
	 
	
 
	    
	    public static int getVerCode(Context context) {   
	        int verCode = -1;   
	        try {   
	            verCode = context.getPackageManager().getPackageInfo(   
	            		PACKAPG_NAME, 0).versionCode;   
	        } catch (NameNotFoundException e) {   
	            Log.e(TAG, e.getMessage());   
	        }   
	        return verCode;   
	    }   
	      
	    public static String getVerName(Context context) {   
	        String verName = "";   
	        try {   
	            verName = context.getPackageManager().getPackageInfo(   
	            		PACKAPG_NAME, 0).versionName;   
	        } catch (NameNotFoundException e) {   
	            Log.e(TAG, e.getMessage());   
	        }   
	        return verName;      
	} 
	    
	    private void startDownload() {

	    	new DownloadFileAsync().execute(com.BRule.ConfigUtil.UPDATE_SERVER_URL);
	    	
	    	}

	    
	    @Override

	    protected Dialog onCreateDialog(int id) {

	    switch (id) {

	    case DIALOG_DOWNLOAD_PROGRESS:

	    mProgressDialog = new ProgressDialog(this);

	    mProgressDialog.setMessage("正在下载..");

	    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

	    mProgressDialog.setCancelable(false);

	    mProgressDialog.show();

	    return mProgressDialog;

	    default:

	    return null;

	    }

	    }

	    void down() {
	    	  handler.post(new Runnable() {
	    	   public void run() {
	    	    mProgressDialog.cancel();
	    	    update();
	    	   }
	    	  });
	    	}
	    
	    	void update() {
	    	  Intent intent = new Intent(Intent.ACTION_VIEW);
	    	  intent.setDataAndType(Uri.fromFile(new File("/sdcard/"+UPDATE_SAVENAME)),
	    	    "application/vnd.android.package-archive");
	    	  startActivity(intent);
	    	}

	    
	    class DownloadFileAsync extends AsyncTask< String, String, String> {

	    	@Override

	    	protected void onPreExecute() {

	    	super.onPreExecute();

	    	showDialog(DIALOG_DOWNLOAD_PROGRESS);

	    	}





	    	@Override

	    	protected String doInBackground(String... aurl) {

	    	int count;

	    	try {

	    	URL url = new URL(aurl[0]);

	    	URLConnection conexion = url.openConnection();

	    	conexion.connect();

	    	int lenghtOfFile = conexion.getContentLength();

	    	Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

	    	InputStream input = new BufferedInputStream(url.openStream());

	    	OutputStream output = new FileOutputStream("/sdcard/"+UPDATE_APKNAME);

	    	byte data[] = new byte[1024];

	    	long total = 0;

	    	while ((count = input.read(data)) != -1) {

	    	total += count;

	    	publishProgress(""+(int)((total*100)/lenghtOfFile));

	    	output.write(data, 0, count);

	    	}

	    	output.flush();
	    	
	    	if(output!=null)
	    	{
	    		down();
	    	}
	    	
	    	output.close();

	    	input.close();

	    	} catch (Exception e) {

	    	Log.e("error",e.getMessage().toString());

	    	System.out.println(e.getMessage().toString());

	    	}

	    	return null;

	    	}

	    	protected void onProgressUpdate(String... progress) {

	    	Log.d("ANDRO_ASYNC",progress[0]);

	    	mProgressDialog.setProgress(Integer.parseInt(progress[0]));

	    	}

	 
	    	@Override

	    	protected void onPostExecute(String unused) {

	    	dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

	    	}

	    	} 
	}
 
    */