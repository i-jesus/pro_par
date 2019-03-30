package com.lotbyte.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * 字符集过滤器
 */
@WebFilter("/*")
public class EncodeFilter implements Filter {

	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// 基于HTTP
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		
		// 1、需要知道哪些资源要被拦截
		// 1.1 设置POST请求的编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		// 1.2 解决GET请求
		// 1.2.1 获取服务器的版本  Apache Tomcat/8.0.45
		String serverInfo = request.getServletContext().getServerInfo();
		System.out.println(serverInfo);
		// 1.2.2 得到服务器具体的版本号
		Integer version = 7;
		// 通过截取，得到具体的版本号，从"/"开始，往后取一位
		String serverInfoVersion = serverInfo.substring(serverInfo.indexOf("/")+1, serverInfo.indexOf("/")+2);
		if (serverInfoVersion != null && !"".equals(serverInfoVersion)) {
			version = Integer.parseInt(serverInfoVersion);
		}
		// 1.2.3 判断服务器版本是否是Tomcat以下，并且是Get请求(忽略大小写) GET get
		if (version < 8 && "get".equalsIgnoreCase(request.getMethod())) {
			// 设置参数的编码
			// 利用HttpServletRequestWrapper解决乱码
			chain.doFilter(new MyWapper(request), response);
			return;
		}
		
		chain.doFilter(request, response);
	}
	

	/**
	 * 参数包装类，处理乱码
	 * 重写getParameter()方法
	 * @author Administrator
	 *
	 */
	class MyWapper extends HttpServletRequestWrapper {
		
		HttpServletRequest request;
		public MyWapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}
		
		@Override
		public String getParameter(String name) {
			// 得到参数值
			String value = request.getParameter(name);
			try {
				if(null !=value && !("".equals(value.trim()))){
					value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return value;
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}


}
