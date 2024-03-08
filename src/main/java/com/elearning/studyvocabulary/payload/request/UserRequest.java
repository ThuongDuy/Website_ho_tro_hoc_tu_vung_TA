package com.elearning.studyvocabulary.payload.request;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

public class UserRequest {
	
	@NotEmpty(message = "Username không được để trống.")
	private String username;
	
	@NotEmpty(message = "Password không được để trống.")
	private String password;
	
//	@NotEmpty(message = "Password không được để trống.")
	private String confirmPassword;
	
	@NotEmpty(message = "Email không được để trống.")
	@Email(message = "Email chưa hợp lệ.")
	private String email;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date created = new Date();
	private String avatar="";
	private int learningMode;
	private int reviewAgainMode;
	private boolean userStatus=true;
	private Set<String> listRoles;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public boolean isUserStatus() {
		return userStatus;
	}
	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}
	public Set<String> getListRoles() {
		return listRoles;
	}
	public void setListRoles(Set<String> listRoles) {
		this.listRoles = listRoles;
	}
	public int getLearningMode() {
		return learningMode;
	}
	public void setLearningMode(int learningMode) {
		this.learningMode = learningMode;
	}
	public int getReviewAgainMode() {
		return reviewAgainMode;
	}
	public void setReviewAgainMode(int reviewAgainMode) {
		this.reviewAgainMode = reviewAgainMode;
	}
	
}
