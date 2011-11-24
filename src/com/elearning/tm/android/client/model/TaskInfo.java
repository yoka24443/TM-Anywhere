package com.elearning.tm.android.client.model;

import java.util.Date;
import java.util.UUID;

import com.elearning.tm.android.client.util.DateTimeHelper;
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
	private String ProjectName;
	@DatabaseField
	private int TotalTime;
	@DatabaseField
	private int PlanTime;
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
	private Date BeginTime;
	@DatabaseField
	private Date EndTime;
	@DatabaseField
	private UUID creator;
	@DatabaseField
	private String TaskWBS;
	
	
	//同步状态
	public String getProjectName() {
		return ProjectName;
	}
	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}
	
	public String getTaskWBS() {
		return TaskWBS;
	}
	public void setTaskWBS(String taskWBS) {
		TaskWBS = taskWBS;
	}
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
	public int getPlanTime() {
		return PlanTime;
	}
	public void setPlanTime(int planTime) {
		PlanTime = planTime;
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
		return BeginTime;
	}
	public void setBeginTime(Date beginTime) {
		BeginTime = beginTime;
	}
	public Date getEndTime() {
		return EndTime;
	}
	public void setEndTime(Date endTime) {
		EndTime = endTime;
	}
	public UUID getCreator() {
		return creator;
	}
	public void setCreator(UUID creator) {
		this.creator = creator;
	}
	
	public String getBeginDate() {
		return DateTimeHelper.fmtDate(BeginTime);
	}
	
	public String getEndDate() {
		return DateTimeHelper.fmtDate(EndTime);
	}
}
