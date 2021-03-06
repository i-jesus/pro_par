package com.lotbyte.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.lotbyte.po.User;
import com.lotbyte.service.NoteService;
import com.lotbyte.vo.ResultInfo;
import org.apache.commons.lang3.StringUtils;


/**
 * 主页
 */

@WebServlet("/main")
public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private  NoteService noteService = new NoteService();

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 三种查询条件不共存，一次只查询一种
		
		String action = request.getParameter("action");
		// 将action存到request作用域中
		request.setAttribute("action", action);
		Optional<String> ap=Optional.ofNullable(action);
		ap.filter(a->StringUtils.isBlank(a)||a.equals("list")).map((a)->{
			// 云记分页列表
			noteList(request, response,null, null, null);
			return null;
		}).orElseGet(()->{
			return ap.filter((a)->a.equals("searchKey")).map((a)->{
				String title = request.getParameter("title");
				// 存request域对象
				request.setAttribute("title", title);
				// 标题模糊查询
				noteList(request, response, title,null, null);
				return null;
			}).orElseGet(()->{
				return ap.filter((a)->a.equals("searchDate")).map((a)->{
					String dateStr = request.getParameter("dateStr");
					// 标题模糊查询
					noteList(request, response, null,dateStr,null);
					return null;
				}).orElseGet(()->{
					return ap.filter((a)->a.equals("searchType")).map((a)->{
						String typeStr = request.getParameter("typeStr");
						// 存request域对象
						request.setAttribute("typeStr", typeStr);
						// 标题模糊查询
						noteList(request, response, null,null,typeStr);
						return null;
					});
				});
			});
		});
		// 从session中得到用户ID
		User user = (User) request.getSession().getAttribute("user");
		// 日期分组查询
		List dateInfo = noteService.findNoteGroupByDate(user.getUserId());
		// 存到session中
		request.getSession().setAttribute("dateInfo", dateInfo);

		// 类型分组查询
		List typeInfo = noteService.findNoteGroupByType(user.getUserId());
		// 存到session中
		request.getSession().setAttribute("typeInfo", typeInfo);

		// 设置动态改变的页面
		request.setAttribute("changePage", "note/list.jsp");
		// 请求转发跳转到首页
		request.getRequestDispatcher("main.jsp").forward(request, response);
	}



/**
	 * 云记分页列表
	 * @param request
	 * @param response
	 */

	private void noteList(HttpServletRequest request, HttpServletResponse response, 
			String title, String dateStr, String typeStr) {
		// 得到参数
		String pageNum = request.getParameter("pageNum"); // 当前页
		String pageSize = request.getParameter("pageSize"); // 每页的数量
		
		// 得到用户ID
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		
		// 调用Service层的查询方法，得到云记的分页对象
		System.out.println("查询日期--->"+dateStr);
		ResultInfo resultInfo =  noteService.findNoteListByPage(pageNum, pageSize,userId, title, dateStr, typeStr);

		// 将resultInfo对象存到request作用域中
		request.setAttribute("resultInfo", resultInfo);
		
	}

}

