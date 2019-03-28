package com.lotbyte.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class JsonUtil {

	/**
	 * 将对象转换成JSON字符串，响应给ajax的回调函数
	 * @param object
	 * @param response
	 * @throws IOException 
	 */
	public static void toJson(Object object, HttpServletResponse response) throws IOException {
		// 设置响应类型和编码 (响应给ajax回调函数的MIME设置)
		response.setContentType("application/json;charset=utf-8");
		// (响应给浏览器的MIME设置)
		// response.setContentType("text/html;charset=utf-8");
		// 得到Gson对象
		Gson gson = new Gson();
		// 将对象转换成json字符串
		String json = gson.toJson(object);
		// 得到输出流
		PrintWriter printWriter = response.getWriter();
		// 输出
		printWriter.write(json);
		// 刷新
		printWriter.flush();
		// 关闭
		printWriter.close();
	}
}
