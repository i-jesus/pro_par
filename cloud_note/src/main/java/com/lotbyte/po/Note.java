package com.lotbyte.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 云记实体类
 * @author Administrator
 *
 */
@Data
public class Note implements Serializable {

	private Integer noteId; // 主键 field
	private String title; // 标题
	private String content; // 内容
	private Date pubtime; // 发布时间
	private Integer typeId; // 所属类型
	
	private String typeName; // 类型名称

	private String  name; // 1、云记日期格式化后的字符串 2、类型名称
	private Long noteCount; // 云记的数量


	
}
