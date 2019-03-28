package com.lotbyte.vo;

/**
 * 响应结果封装类
 * @author Administrator
 * @param <T>
 *
 */
public class ResultInfo<T> {

	private Integer code; // 状态码 1=成功，0=失败
	private String msg; // 提示信息
	private T result; // 保存返回的对象
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
}
