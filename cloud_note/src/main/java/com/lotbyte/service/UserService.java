package com.lotbyte.service;


import com.lotbyte.dao.UserDaoRepository;
import com.lotbyte.po.User;
import com.lotbyte.util.MD5Util;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Optional;

/**
 * 用户模块的业务逻辑处理
 * @author Administrator
 *
 */
public class UserService {

	private UserDaoRepository userRepository = new UserDaoRepository();

	/**
	 * 用户登录
	 *
	 *  1、判断参数是否为空，如果为空，返回错误信息
	 2、先将前台传过来的密码加密，再去查询
	 3、调用Dao层，通过用户名和密码去数据库中查询用户对象，得到用户对象user
	 4、判断用户user对象是否为空，为空返回错误信息
	 5、如果不为空，登录成功
	 * @param uname
	 * @param upwd
	 * @return
	 */
	public ResultInfo userLogin(String uname, String upwd) {
		ResultInfo resultInfo = new ResultInfo();
		// 判断参数是否为空
		// 将前台传过来的密码通过MD5加密
		upwd = MD5Util.encode(upwd);
		// 调用Dao层，通过用户名和密码去数据库中查询用户对象，得到用户对象user
		Optional<User> userOptional  = userRepository.findUserByUnameAndUpwd(uname, upwd);
		// 判断user对象是否为空
		if (!userOptional.isPresent()) { // 用户不存在
			resultInfo.setCode(0);
			resultInfo.setMsg("用户名或密码不正确！");
			resultInfo.setResult(Optional.empty());
			return resultInfo;
		}
		// 登录成功
		resultInfo.setCode(1);
		resultInfo.setMsg("登录成功！");
		resultInfo.setResult(userOptional.get());
		System.out.println("登录结果-->"+resultInfo.getResult());
		return resultInfo;
	}
	/**
	 * 验证昵称的唯一性
	 *  1、判断参数
	 2、调用Dao层的查询方法，返回true和false，true代表可用，false不可用（参数：nick、userId）
	 3、判断昵称是否可用，可用：msg=昵称未占用，可以使用！不可用：msg=昵称已被占用，不可使用！
	 * @param nick
	 * @param userId
	 * @return
	 */
	public ResultInfo checkNick(String nick, Integer userId) {
		ResultInfo resultInfo = new ResultInfo();
		// 判断参数
		if (StringUtil.isNullOrEmpty(nick)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("用户昵称不能为空！");
			return resultInfo;
		}

		// 调用Dao层的查询方法，返回true和false，true代表可用，false不可用（参数：nick、userId）
		boolean flag = userRepository.checkNick(nick, userId);
		if (flag) { // 可用
			resultInfo.setCode(1);
			resultInfo.setMsg("昵称未被占用，可以使用！");
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("昵称被占用，不可使用！");
		}

		return resultInfo;
	}

	/**
	 * 修改用户信息
	 * @return
	 */
	public ResultInfo updateInfo(HttpServletRequest request) {

		// 接收参数
		String nick = request.getParameter("nick");
		String mood = request.getParameter("mood");

		// 得到user对象
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();

		User u = new User();
		u.setNick(nick);
		u.setMood(mood);
		u.setUserId(userId);

		// ==============上传头像=========
		try {
			// 得到part对象，name是file元素的name属性值
			Part part = request.getPart("img");
			// 得到文件上传的名称
			String fileName = part.getName();
			System.out.println("文件名:"+fileName);
			// 判断是否上传了头像
			if (!StringUtil.isNullOrEmpty(fileName)) {
				u.setHead(fileName);
				// 上传文件的存放路径
				String path = request.getServletContext().getRealPath("/upload/"+fileName);
				part.write(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResultInfo resultInfo = new ResultInfo();
		// 调用Dao层的修改方法，返回受影响的行数
		int row = userRepository.updateInfo(u);
		if (row > 0) {
			resultInfo.setCode(1);
			resultInfo.setMsg("修改成功！");
			resultInfo.setResult(u); // 更新了用户信息，需要更新user在session的值
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("修改失败！");
		}

		return resultInfo;
	}

	public static void main(String[] args) {
		new UserService().userLogin("shsxt","123456");
	}

}
