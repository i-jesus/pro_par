package com.lotbyte.po;

/**
 * 用户实体类
 * @author Administrator
 *
 */
public class User {
	
	private Integer userId; // 主键ID
	private String uname; // 用户名称
	private String upwd; // 用户密码
	private String nick; // 昵称
	private String head; // 头像
	private String mood; // 心情
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}


	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", uname='" + uname + '\'' +
				", upwd='" + upwd + '\'' +
				", nick='" + nick + '\'' +
				", head='" + head + '\'' +
				", mood='" + mood + '\'' +
				'}';
	}
}
