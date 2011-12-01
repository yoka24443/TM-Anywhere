package com.elearning.tm.android.client.model;

import java.util.UUID;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "User_Info")
public class UserInfo {
	
	public int getRowNumber() {
		return RowNumber;
	}
	public void setRowNumber(int rowNumber) {
		RowNumber = rowNumber;
	}
	public UUID getUserID() {
		return UserID;
	}
	public void setUserID(UUID userID) {
		UserID = userID;
	}
	public String getUserAccount() {
		return UserAccount;
	}
	public void setUserAccount(String userAccount) {
		UserAccount = userAccount;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getHeader() {
		return Header;
	}
	public void setHeader(String header) {
		Header = header;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getIsAdmin() {
		return IsAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		IsAdmin = isAdmin;
	}
	
	@DatabaseField
	private int RowNumber;
	@DatabaseField(id = true)
	private UUID  UserID;
	@DatabaseField(canBeNull = false)
	private String UserAccount;
	@DatabaseField
	private String Email;
	@DatabaseField
	private String Mobile;
	@DatabaseField
	private String Header;
	@DatabaseField(canBeNull = false)
	private String Password;
	@DatabaseField
	private String Status; 
	@DatabaseField
	private String IsAdmin;
	
}
