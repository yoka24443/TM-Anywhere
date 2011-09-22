package com.elearning.tm.android.client.model;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Task_WBS")
public class TaskWBS {
	public UUID getTaskID() {
		return TaskID;
	}
	public void setTaskID(UUID taskID) {
		TaskID = taskID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public UUID getPID() {
		return PID;
	}
	public void setPID(UUID pID) {
		PID = pID;
	}

	@DatabaseField(id = true)
	private UUID  TaskID;
	@DatabaseField
	private String Name;
	@DatabaseField
	private UUID PID;
	
}
