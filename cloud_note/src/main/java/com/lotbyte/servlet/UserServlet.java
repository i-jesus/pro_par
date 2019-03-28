package com.lotbyte.servlet;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lotbyte.po.User;
import com.lotbyte.service.UserService;
import com.lotbyte.util.JsonUtil;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;

/**
 * 用户模块
 */
@MultipartConfig
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// 得到当前类的日志对象
	private Logger logger = LoggerFactory.getLogger(UserServlet.class);
	
	private UserService userService = new UserService();

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到用户的行为
		String action = request.getParameter("action");
		
		if (StringUtil.isNullOrEmpty(action) ) {
			
			// 进入登录页面
			response.sendRedirect("login.jsp");
			
		} else if ("login".equals(action)) { // 登录操作
			
			// 用户登录
			userLogin(request,response);
			
		} else if ("logout".equals(action)) { // 退出操作
			
			// 用户退出登录
			userLogout(request, response);
			
		} else if ("userCenter".equals(action)) {
			
			// 进入个人中心页面
			userCenter(request, response);
			
		} else if ("userHead".equals(action)) {
			
			// 加载头像
			userHead(request, response);
		} else if ("checkNick".equals(action)) {
			
			// 验证昵称的唯一性
			checkNick(request, response);
		} else if ("updateInfo".equals(action)) {
			
			// 修改用户信息
			//updateInfo(request, response);
		}
		
	}


	/**
	 * 修改用户信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	/*private void updateInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// 调用Service层的修改方法，返回ResultInfo对象
		ResultInfo<User> resultInfo = userService.updateInfo(request);
		
		if (resultInfo.getCode() ==1) {
			// 更新了用户信息之后更新session
			request.getSession().setAttribute("user", resultInfo.getResult());
		}
		
		request.setAttribute("resultInfo", resultInfo);
		// 设置动态页面值
		request.setAttribute("changePage", "user/info.jsp");
		// 请求转发
		request.getRequestDispatcher("main.jsp").forward(request, response);
		
	}*/


	/**
	 * 验证昵称的唯一性
	 * 	1、接收参数
		2、从session中得到user对象，并且得到userId
		3、调用Service层查询方法，返回resultInfo对象
		4、将resultInfo对象转换成JSON字符串，响应给ajax的回调函数
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 接收参数
		String nick = request.getParameter("nick");
		// 从session中得到user对象
		User user = (User)request.getSession().getAttribute("user");
		// 得到userId
		Integer userId = user.getUserId();
		// 调用Service层查询方法，返回resultInfo对象
		ResultInfo<User> resultInfo = userService.checkNick(nick, userId);
		// 将resultInfo对象转换成JSON字符串，响应给ajax的回调函数
		/*response.getWriter().write(new Gson().toJson(resultInfo));*/
		JsonUtil.toJson(resultInfo, response);
		
	}


	/**
	 * 头像显示
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 接收参数
		String fn = request.getParameter("fn");
		// 判断头像是否为空
		if (StringUtil.isNullOrEmpty(fn)) {
			return;
		}
		// 得到图片在服务器上存放的路径
		String path = request.getServletContext().getRealPath("/upload/" + fn);
		logger.info("得到图片在服务器上存放的路径：{}", path);
		
		// 通过头像的路径的文件对象
		File file = new File(path);
		
		// 判断file是否存在，并且是一个标准文件
		if (file.exists() && file.isFile()) {
			// 得到图片的后缀,从指定位置截取到最后
			String pic = fn.substring(fn.indexOf(".")+1);
			// 判断后缀是否为空,如果为空，可能就不是图片
			if (StringUtil.isNullOrEmpty(pic)) {
				return;
			}
			// 通过不同的图片类型来设置不同的MIME响应类型
			if ("png".equalsIgnoreCase(pic)) { // 忽略大小写判断
				response.setContentType("image/png;charset=utf-8");
			} else if ("jpg".equalsIgnoreCase(pic)) {
				response.setContentType("image/jpeg;charset=utf-8");
			} else if ("jpeg".equalsIgnoreCase(pic)) {
				response.setContentType("image/jpeg;charset=utf-8");
			} else if ("gif".equalsIgnoreCase(pic)) {
				response.setContentType("image/gif;charset=utf-8");
			}
			
			// 响应，commons-io的FileUtils对象，拷贝文件
			FileUtils.copyFile(file, response.getOutputStream());
			
		}
	}


	/**
	 * 进入个人中心
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 设置动态页面值
		request.setAttribute("changePage", "user/info.jsp");
		// 请求转发到首页
		request.getRequestDispatcher("main.jsp").forward(request, response);
		
	}


	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void userLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 清空session
		request.getSession().invalidate();
		// 清空cookie
		Cookie cookie = new Cookie("user", null);
		cookie.setMaxAge(0); // 0表示删除
		response.addCookie(cookie);
		// 跳转到login.jsp
		response.sendRedirect("login.jsp");
	}


	/**
	 * 用户登录
	 * 
	 *  1、接收参数
		2、调用Service层，得到封装对象ResultInfo（登录状态1=成功，0=失败、提示信息、泛型对象，保存响应的对象）
		3、判断是否登录成功
			如果否，将封装对象ResultInfo存到request作用域中，请求转发到login.jsp
			如果成功，
				1、将user对象存session作用域
				2、判断是否记住密码，如果是存cookie
					Cookie cookie = new Cookie(key,user);
					cookie.setMaxAge(秒); // 失效时间 0=删除，负数：关闭浏览器失效，正整数：保存指定时间
					response.addCookie(cookie);
				3、重定向到首页
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接收参数
		String  uname = request.getParameter("uname");
		String  upwd = request.getParameter("upwd");
		
		// 调用Service层的查询方法，返回resultInfo对象
		ResultInfo<User> resultInfo = userService.userLogin(uname, upwd);
		
		// 判断用户登录成功
		if (resultInfo.getCode() == 1) { // 成功
			// 将用户对象存到session域中
			request.getSession().setAttribute("user", resultInfo.getResult());
			// 判断是否记住密码
			String rem = request.getParameter("rem");
			// 1=记住密码，0=没有记住密码
			if (!StringUtil.isNullOrEmpty(rem) && "1".equals(rem)) {
				// 存cookie
				Cookie cookie = new Cookie("user", uname + "-" + upwd);
				cookie.setMaxAge(3*24*60*60); // 设置失效时间，三天失效，单位秒
				response.addCookie(cookie); // 响应给客户端
			}
			// 重定向到主页
			response.sendRedirect("main");
			
		} else { // 失败
			// 设置request域对象
			request.setAttribute("resultInfo", resultInfo);
			// 请求转发到登录页面
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		
	}

}
