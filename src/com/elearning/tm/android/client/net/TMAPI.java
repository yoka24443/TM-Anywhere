package com.elearning.tm.android.client.net;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import com.elearning.tm.android.client.model.ProjectInfo;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.model.TaskWBS;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.util.BeanRefUtil;

public class TMAPI {

	private static final String NAMESPACE = "http://tempuri.org/";
	private static String URL = "http://tm.e-learning.com.cn/WebService/QueryService.asmx";

	public SoapObject getSoapObjectResponse(String methodName,
			Map<String, String> parms) {
		SoapObject detail = null;
		try {
			SoapObject rpc = new SoapObject(NAMESPACE, methodName);
			if (parms != null && !parms.isEmpty()) {
				for (Entry<String, String> para : parms.entrySet()) {
					rpc.addProperty(para.getKey(), para.getValue());
				}
			}
			AndroidHttpTransport ht = new AndroidHttpTransport(URL);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);

			String soapAction = NAMESPACE + methodName;
			ht.call(soapAction, envelope);

			SoapObject result = (SoapObject) envelope.bodyIn;
			Object obj = result.getProperty(methodName + "Result");
			if(obj instanceof SoapPrimitive){
				SoapPrimitive soap = (SoapPrimitive)obj;
				return detail = new SoapObject(soap.getNamespace(), soap.toString());
			}
			detail = (SoapObject)obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}

	public UserInfo Login(String userName, String passWord) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("userName", userName);
		parms.put("passWord", passWord);

		SoapObject soap = getSoapObjectResponse("Login", parms);
		UserInfo user = new UserInfo();
		if (soap != null)
			BeanRefUtil.setFieldValueBySoapObject(user, soap);
		return user;
	}

	public List<UserInfo> QueryUserList(String userName, int pageIndex,
			int pageSize) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("userName", userName);
		parms.put("pageIndex", String.valueOf(pageIndex));
		parms.put("pageSize", String.valueOf(pageSize));
		SoapObject soap = getSoapObjectResponse("SearchUserList", parms);
		List<UserInfo> list = new ArrayList<UserInfo>();
		int elementCount = soap.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			UserInfo pi = new UserInfo();
			SoapObject table = (SoapObject) soap.getProperty(i);
			BeanRefUtil.setFieldValueBySoapObject(pi, table);
			list.add(pi);
		}
		return list;
	}

	public List<ProjectInfo> QueryProjectList() {
		SoapObject soap = getSoapObjectResponse("QueryProjectList", null);
		List<ProjectInfo> list = new ArrayList<ProjectInfo>();
		if (soap != null) {
			SoapObject so = (SoapObject) soap.getProperty("diffgram");
			SoapObject doc = (SoapObject) so.getProperty("DocumentElement");
			int elementCount = doc.getPropertyCount();
			for (int i = 0; i < elementCount; i++) {
				ProjectInfo pi = new ProjectInfo();
				SoapObject table = (SoapObject) doc.getProperty(i);
				BeanRefUtil.setFieldValueBySoapObject(pi, table);
				list.add(pi);
			}
		}
		return list;
	}

	public List<TaskInfo> QueryUserTaskList(String uid, int pageIndex,
			int pageSize) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("uid", uid);
		parms.put("pageIndex", String.valueOf(pageIndex));
		parms.put("pageSize", String.valueOf(pageSize));

		List<TaskInfo> list = new ArrayList<TaskInfo>();
		SoapObject soap = getSoapObjectResponse("QueryUserTaskList", parms);
		if (soap != null) {

			int elementCount = soap.getPropertyCount();
			for (int i = 0; i < elementCount; i++) {
				TaskInfo pi = new TaskInfo();
				SoapObject table = (SoapObject) soap.getProperty(i);
				BeanRefUtil.setFieldValueBySoapObject(pi, table);
				list.add(pi);
			}
		}
		return list;
	}

	public List<TaskWBS> QueryProjectWBS() {
		SoapObject soap = getSoapObjectResponse("QueryProjectWBS", null);
		SoapObject so = (SoapObject) soap.getProperty("diffgram");
		SoapObject doc = (SoapObject) so.getProperty("DocumentElement");

		List<TaskWBS> list = new ArrayList<TaskWBS>();
		int elementCount = doc.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			TaskWBS pi = new TaskWBS();
			SoapObject table = (SoapObject) doc.getProperty(i);
			BeanRefUtil.setFieldValueBySoapObject(pi, table);
			list.add(pi);
		}
		return list;
	}

	public List<TaskWBS> QueryProjectWBSByPID(String pid) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("pid", pid);

		SoapObject soap = getSoapObjectResponse("QueryProjectWBSByPID", parms);
		SoapObject so = (SoapObject) soap.getProperty("diffgram");
		SoapObject doc = (SoapObject) so.getProperty("DocumentElement");

		List<TaskWBS> list = new ArrayList<TaskWBS>();
		int elementCount = doc.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			TaskWBS pi = new TaskWBS();
			SoapObject table = (SoapObject) doc.getProperty(i);
			com.elearning.tm.android.client.util.BeanRefUtil
					.setFieldValueBySoapObject(pi, table);
			list.add(pi);
		}
		return list;
	}

	public Boolean CreateOrModifyTaskInfo(TaskInfo task, String type) {
		Map<String, String> parms = new HashMap<String, String>();
		if(type.equalsIgnoreCase("edit")){
			parms.put("tid", task.getTaskID().toString());  //修改功能传id,新增数据库自动生成
		}
		parms.put("pid", task.getPID().toString());
		parms.put("parentid", task.getPType().toString());
		parms.put("name", task.getTaskName());
		parms.put("remark", task.getRemark());
		parms.put("begin", task.getBeginDate());
		parms.put("end", task.getEndDate());
		parms.put("totalTime", String.valueOf(task.getTotalTime()));
		parms.put("planTime", String.valueOf(task.getPlanTime()));
		parms.put("uid", task.getAssignUser().toString());
		parms.put("status", String.valueOf(task.getStatus()));
		parms.put("type", type);  // add, edit
		SoapObject soap = getSoapObjectResponse("CreateOrModifyTaskInfo", parms);
		if (soap != null)
			return true;
		else
			return false;
	}
	
	//report 数据
	public Map<String, List<TaskInfo>> QueryTaskReportData(String uid, String beginDate, String endDate) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("uid", uid);
		parms.put("begin", beginDate);
		parms.put("end", endDate);
		SoapObject soap = getSoapObjectResponse("QueryUserTaskReport", parms);
		SoapObject so = (SoapObject) soap.getProperty("diffgram");
		SoapObject doc = (SoapObject) so.getProperty("DocumentElement");
		
		Map<String, List<TaskInfo>> tasks = new HashMap<String, List<TaskInfo>>();
		int elementCount = doc.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			TaskInfo pi = new TaskInfo();
			SoapObject table = (SoapObject) doc.getProperty(i);
			com.elearning.tm.android.client.util.BeanRefUtil.setFieldValueBySoapObject(pi, table);
			if(!tasks.containsKey(pi.getProjectName())){
				List<TaskInfo> list = new ArrayList<TaskInfo>();
				list.add(pi);
				tasks.put(pi.getProjectName(), list);
			} else {
				 List<TaskInfo> tasklist =  tasks.get(pi.getProjectName());
				 tasklist.add(pi);
			}
		}
		return tasks;
	}
	
	public  List<TaskInfo> QueryUsersTaskReportData(String beginDate, String endDate) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("begin", beginDate);
		parms.put("end", endDate);
		SoapObject soap = getSoapObjectResponse("QueryAllUsersTaskReport", parms);
		SoapObject so = (SoapObject) soap.getProperty("diffgram");
		SoapObject doc = (SoapObject) so.getProperty("DocumentElement");
		
		List<TaskInfo> tasks = new ArrayList<TaskInfo>();
		int elementCount = doc.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			TaskInfo pi = new TaskInfo();
			SoapObject table = (SoapObject) doc.getProperty(i);
			com.elearning.tm.android.client.util.BeanRefUtil.setFieldValueBySoapObject(pi, table);
			tasks.add(pi);
		}
		return tasks;
	}
	
	public TaskInfo QueryTaskInfo(String tid){
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("tid", tid);
		SoapObject soap = getSoapObjectResponse("QueryTaskInfo", parms);
		
		TaskInfo task = new TaskInfo();
		if (soap != null)
			BeanRefUtil.setFieldValueBySoapObject(task, soap);
		return task;
	}
	
	public Boolean DeleteTaskInfo(String tid){
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("tid", tid);
		SoapObject soap = getSoapObjectResponse("DeleteTaskInfo", parms);
		if (soap != null && soap.getName().equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}
	

}
