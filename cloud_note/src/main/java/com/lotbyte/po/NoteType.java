package com.lotbyte.po;

import lombok.Data;

/**
 * 类型实体类
 * @author Administrator
 *
 */
@Data
public class NoteType {

	private Integer typeId; // 主键
	private String typeName; // 类型名称
	private Integer userId; // 所属账户
	

}
