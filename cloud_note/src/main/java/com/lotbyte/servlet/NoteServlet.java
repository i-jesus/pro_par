package com.lotbyte.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lotbyte.po.Note;
import com.lotbyte.po.NoteType;
import com.lotbyte.po.User;
import com.lotbyte.service.NoteService;
import com.lotbyte.service.TypeService;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;

/**
 * 云记管理
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private NoteService noteService = new NoteService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到用户行为
		String  action = request.getParameter("action");
		if (StringUtil.isNullOrEmpty(action) || "list".equals(action)) {
			// 查询列表
		} else if ("view".equals(action)) {
			// 进入发表云记
			noteView(request, response);
		} else if ("edit".equals(action)) {
			// 添加或者修改操作
			noteEdit(request, response);
		} else if ("detail".equals(action)) {
			// 查询云记详情
			noteDetail(request, response);
		} else if ("delete".equals(action)) {
			// 删除云记
			noteDelete(request ,response);
		}
	}


	/**
	 * 删除云记
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void noteDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 接收参数
		String noteId = request.getParameter("noteId"); 
		
		// 调用Service层的删除方法，返回resultInfo对象
		ResultInfo<Note> resultInfo = noteService.deleteNote(noteId);
		
		// 判断是否删除成功
		if (resultInfo.getCode() == 1) { // 成功
			// 重定向到首页
			response.sendRedirect("main");
		} else {
			// 将resultInfo存到request作用域中
			request.setAttribute("resultInfo", resultInfo);
			// 请求转发到云记详情页面
			request.getRequestDispatcher("note?action=detail&noteId="+noteId).forward(request, response);
		}
		
	}


	/**
	 * 查询云记详情
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void noteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接收参数
		String noteId = request.getParameter("noteId");
		
		// 调用Service层的方法，通过noteId主键查询note对象
		Note note = noteService.findNoteById(noteId);
		
		ResultInfo<Note> resultInfo = new ResultInfo<>();
		if (note != null) { // 如果查询到云记对象
			resultInfo.setCode(1);
			resultInfo.setResult(note);
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("未查询到云记详情！");
		}
		// 存到request作用域中
		request.setAttribute("resultInfo", resultInfo);
		
		// 设置动态页面值
		request.setAttribute("changePage", "note/detail.jsp");
		// 请求转发到首页
		request.getRequestDispatcher("main.jsp").forward(request, response);
		
	}

	/**
	 * 添加或者修改操作
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void noteEdit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 接收参数
		String  noteId = request.getParameter("noteId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String typeId = request.getParameter("typeId");
		
		// 调用Service的方法，返回resultInfo页面
		ResultInfo<Note> resultInfo = noteService.noteEdit(noteId,title,content,typeId);
		
		// 判断是否更新成功
		if (resultInfo.getCode() == 1) { // 成功
			response.sendRedirect("main");
		} else {
			// 存request作用域
			request.setAttribute("noteInfo", resultInfo.getResult());
			String url = "note?action=view"; // 添加操作
			if (!StringUtil.isNullOrEmpty(noteId)) { //修改操作
				url+="&noteId=" + Integer.parseInt(noteId);
			}
			// 请求转发到添加或修改页面
			request.getRequestDispatcher(url).forward(request, response);
		}
		
	}

	/**
	 * 进入发表云记
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 如果noteId不为空，说明是进入修改页面
		String noteId = request.getParameter("noteId");
		if (!StringUtil.isNullOrEmpty(noteId)) {
			// 调用Service查询方法，通过noteId主键查询note对象
			Note note = noteService.findNoteById(noteId);
			// 将note对象存放request作用域中
			request.setAttribute("noteInfo", note);
		}
		
		// 得到用户ID
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 查询当前用户的云记类型列表
		ResultInfo<List<NoteType>> resultInfo = new TypeService().findNoteTypeList(userId);
		// 将resultInfo、存放到request域对象中
		request.setAttribute("resultInfo", resultInfo);
		
		// 设置动态页面值
		request.setAttribute("changePage", "note/edit.jsp");
		// 请求转发
		request.getRequestDispatcher("main.jsp").forward(request, response);
	}

}
