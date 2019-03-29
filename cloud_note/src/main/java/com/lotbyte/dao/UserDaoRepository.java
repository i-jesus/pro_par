package com.lotbyte.dao;



import com.lotbyte.base.BaseRepository;
import com.lotbyte.po.User;
import com.lotbyte.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 用户管理的数据层
 * @author Administrator
 *
 */
public class UserDaoRepository  implements BaseRepository<User> {
	
	// 创建日志类
	private Logger logger = LoggerFactory.getLogger(UserDaoRepository.class);

	/**
	 * 同通过用户名和密码查询用户对象
	 *  1、得到数据库连接
		2、写sql语句
		3、预编译
		4、设置参数
		5、执行查询
		6、解析结果集
		7、关闭资源
	 * @param uname
	 * @param upwd
	 * @return
	 */
	public Optional<User> findUserByUnameAndUpwd(String uname, String upwd) {
		String sql = "select * from tb_user where uname = ? and upwd = ?";
		return BaseRepository.super.queryObject(sql, User.class, uname, upwd);
	}

	/**
	 * 通过昵称和用户名验证昵称是否可用
	 * @param nick
	 * @param userId
	 * @return
	 */
	public boolean checkNick(String nick, Integer userId) {
		String sql = "select * from tb_user where nick = ? and userId != ?";
		return BaseRepository.super.queryObject(sql,User.class,nick,userId).isPresent()?true:false;
	}

	/**
	 * 修改用户信息
	 * @param u
	 * @return
	 */
	public int updateInfo(User u) {
		String sql = "update tb_user set nick = ?, head = ?, mood= ?  where userId = ?";
		return BaseRepository.update(sql,u.getNick(),u.getHead(),u.getMood(),u.getUserId());
	}


    public static void main(String[] args) {
        System.out.println(new UserDaoRepository().queryRows("select * from tb_user ", User.class).get());
        System.out.println(new UserDaoRepository().
                queryObject("select * from tb_user where uname =? and upwd =?", User.class,"shsxt",MD5Util.encode("123456"))
                .get());
    }
}
