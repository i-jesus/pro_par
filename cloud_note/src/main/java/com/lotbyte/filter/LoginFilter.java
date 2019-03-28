package com.lotbyte.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lotbyte.po.User;
import com.lotbyte.util.StringUtil;


/**
 *非法访问拦截（未登录）
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    public LoginFilter() {
    	
    }

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// 基于HTTP
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		// 得到访问的路径
		String url = request.getRequestURI();
		
		// 放行静态资源
		if (url.contains("/statics")) {
			// 放行
			chain.doFilter(request, response);
			return;
		}
		
		// 放行指定资源 login.jsp register.jsp  loginServlet registerServlet
		if (url.contains("/login.jsp") || 
			url.contains("/register.jsp")) {
			// 放行
			chain.doFilter(request, response);
			return;
		}
		
		// 放行指定行为
		if (url.contains("user")) {
			// 得到用户行为
			String action = request.getParameter("action");
			if ("login".equals(action) || ("register").equals(action)) {
				// 放行
				chain.doFilter(request, response);
				return;
			}
		}
		
		// 判断用户是否登录（判断session，登录成功后会存session，所以只要是登录状态就一定会有session）
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) { // 登录状态
			// 放行
			chain.doFilter(request, response);
			return;
		}
		
		// 判断cookie是否有值
		// 得到cookie数组
		Cookie[] cookies = request.getCookies();
		// 循环并判断user的cookie
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// cookie的名称
				String name = cookie.getName(); // user
				if ("user".equals(name)) {
					// 得到cookie对象的值 lotbyte-123456
					String value = cookie.getValue();
					if (!StringUtil.isNullOrEmpty(value)) {
						 // 将value值分割成数组
						String[] strings = value.split("-");
						// 用户名称
						String uname = strings[0];
						// 用户密码
						String upwd = strings[1];
						// 请求转发到登录操作
						String path = "user?action=login&uname=" + uname + "&upwd=" + upwd;
						// 自动登录
						request.getRequestDispatcher(path).forward(request, response);
						return;
					}
				}
			}
		}
		
		
		// 如果没有登录，重定向到登录界面
		response.sendRedirect("login.jsp");
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
