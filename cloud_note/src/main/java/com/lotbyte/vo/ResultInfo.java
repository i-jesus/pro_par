package com.lotbyte.vo;

import lombok.Data;

/**
 * 响应结果封装类
 * @author Administrator
 */
@Data
public class ResultInfo {

	private Integer code; // 状态码 1=成功，0=失败
	private String msg; // 提示信息
	private Object result; // 保存返回的对象


}
