package com.lotbyte.po;

import java.util.Date;

/**
 * 云记实体类
 * @author Administrator
 *
 */
public class Note {

	private Integer noteId; // 主键 field
	private String title; // 标题
	private String content; // 内容
	private Date pubtime; // 发布时间
	private Integer typeId; // 所属类型
	
	private String typeName; // 类型名称
	
	public Integer getNoteId() {
		return noteId;
	}
	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getPubtime() {
		return pubtime;
	}
	public void setPubtime(Date pubtime) {
		this.pubtime = pubtime;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
