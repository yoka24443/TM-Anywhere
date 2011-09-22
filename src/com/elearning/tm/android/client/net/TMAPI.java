package com.elearning.tm.android.client.net;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
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
			detail = (SoapObject) result.getProperty(methodName + "Result");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detail;
	}

	public  UserInfo Login(String userName, String passWord) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("userName", userName);
		parms.put("passWord", passWord);

		SoapObject soap = getSoapObjectResponse("Login", parms);
		UserInfo user = new UserInfo();
		if(soap != null)
		BeanRefUtil.setFieldValueBySoapObject(user, soap);
		return user;
	}

	public List<ProjectInfo> QueryProjectList() {
		SoapObject soap = getSoapObjectResponse("QueryProjectList", null);
		SoapObject so = (SoapObject) soap.getProperty("diffgram");
		SoapObject doc = (SoapObject) so.getProperty("DocumentElement");
		
		List<ProjectInfo> list = new ArrayList<ProjectInfo>();
		int elementCount = doc.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			ProjectInfo pi = new ProjectInfo();
			SoapObject table = (SoapObject) doc.getProperty(i);
			BeanRefUtil.setFieldValueBySoapObject(pi, table);
			list.add(pi);
		}
		return list;
	}

	public List<TaskInfo> QueryUserTaskList(String uid, Date beginDate,
			Date endDate) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("uid", uid);
		parms.put("beginDate", beginDate.toLocaleString());
		parms.put("endDate", endDate.toLocaleString());
		
		SoapObject soap = getSoapObjectResponse("QueryUserTaskList", parms);
		SoapObject so = (SoapObject) soap.getProperty("diffgram");
		SoapObject doc = (SoapObject) so.getProperty("DocumentElement");
		
		List<TaskInfo> list = new ArrayList<TaskInfo>();
		int elementCount = doc.getPropertyCount();
		for (int i = 0; i < elementCount; i++) {
			TaskInfo pi = new TaskInfo();
			SoapObject table = (SoapObject) doc.getProperty(i);
			BeanRefUtil.setFieldValueBySoapObject(pi, table);
			list.add(pi);
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
			com.elearning.tm.android.client.util.BeanRefUtil.setFieldValueBySoapObject(pi, table);
			list.add(pi);
		}
		return list;
	}
}
