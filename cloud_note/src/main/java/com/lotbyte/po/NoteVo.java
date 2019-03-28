package com.lotbyte.po;

public class NoteVo {
	
	private String  name; // 1、云记日期格式化后的字符串 2、类型名称
	private Integer noteCount; // 云记的数量
	private Integer typeId; // 类型的ID
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNoteCount() {
		return noteCount;
	}
	public void setNoteCount(Integer noteCount) {
		this.noteCount = noteCount;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
