package com.lotbyte.po;

/**
 * 类型实体类
 * @author Administrator
 *
 */
public class NoteType {

	private Integer typeId; // 主键
	private String typeName; // 类型名称
	private Integer userId; // 所属账户
	
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
