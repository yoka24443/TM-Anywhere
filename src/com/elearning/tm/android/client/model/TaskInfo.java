package com.elearning.tm.android.client.model;

import java.util.Date;
import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Task_Info")
public class TaskInfo {
	@DatabaseField
	private int RowNumber;
	@DatabaseField(id = true)
	private UUID TaskID;
	@DatabaseField
	private String TaskName;
	@DatabaseField
	private String Remark;
	@DatabaseField
	private UUID PID;
	@DatabaseField
	private int TotalTime;
	@DatabaseField
	private String PTitle;
	@DatabaseField
	private String PNo;
	@DatabaseField
	private UUID PType;
	@DatabaseField
	private UUID AssignUser;
	@DatabaseField
	private String AssignUserName;
	@DatabaseField
	private int Status;
	@DatabaseField
	private Date beginTime;
	@DatabaseField
	private Date endTime;
	@DatabaseField
	private UUID creator;
	
	//同步状态
	
	public int getRowNumber() {
		return RowNumber;
	}
	public void setRowNumber(int rowNumber) {
		RowNumber = rowNumber;
	}
	public UUID getTaskID() {
		return TaskID;
	}
	public void setTaskID(UUID taskID) {
		TaskID = taskID;
	}
	public String getTaskName() {
		return TaskName;
	}
	public void setTaskName(String taskName) {
		TaskName = taskName;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public UUID getPID() {
		return PID;
	}
	public void setPID(UUID pID) {
		PID = pID;
	}
	public int getTotalTime() {
		return TotalTime;
	}
	public void setTotalTime(int totalTime) {
		TotalTime = totalTime;
	}
	public String getPTitle() {
		return PTitle;
	}
	public void setPTitle(String pTitle) {
		PTitle = pTitle;
	}
	public String getPNo() {
		return PNo;
	}
	public void setPNo(String pNo) {
		PNo = pNo;
	}
	public UUID getPType() {
		return PType;
	}
	public void setPType(UUID pType) {
		PType = pType;
	}
	public UUID getAssignUser() {
		return AssignUser;
	}
	public void setAssignUser(UUID assignUser) {
		AssignUser = assignUser;
	}
	public String getAssignUserName() {
		return AssignUserName;
	}
	public void setAssignUserName(String assignUserName) {
		AssignUserName = assignUserName;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public UUID getCreator() {
		return creator;
	}
	public void setCreator(UUID creator) {
		this.creator = creator;
	}
	

}
