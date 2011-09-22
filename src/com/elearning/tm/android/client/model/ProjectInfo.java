package com.elearning.tm.android.client.model;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Project_Info")
public class ProjectInfo {
	@DatabaseField(id = true)
	private UUID PID;
	@DatabaseField
	private String PTitle;
	
	public UUID getPID() {
		return PID;
	}
	public void setPID(UUID pID) {
		PID = pID;
	}
	public String getPTitle() {
		return PTitle;
	}
	public void setPTitle(String pTitle) {
		PTitle = pTitle;
	}
}
