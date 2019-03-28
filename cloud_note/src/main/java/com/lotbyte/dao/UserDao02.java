package com.lotbyte.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lotbyte.po.User;
import com.lotbyte.util.DBUtil;

/**
 * 用户管理的数据层
 * @author Administrator
 *
 */
public class UserDao02 {

    // 创建日志类
    private Logger logger = LoggerFactory.getLogger(UserDao.class);

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
    public User findUserByUnameAndUpwd(String uname, String upwd) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from tb_user where uname = ? and upwd = ?";
            // 拼接方式
            logger.info("查询SQL语句：" + sql + "aaaa");
            // 占位符方式
            logger.debug("查询SQL语句2：{} aaa {}", sql,1);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, upwd);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                user = new User();
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
                user.setUname(uname);
                user.setUpwd(upwd);
                user.setUserId(resultSet.getInt("userid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(Optional.ofNullable(resultSet),
                    Optional.ofNullable(preparedStatement),
                    Optional.ofNullable(connection) );
        }

        return user;
    }

    /**
     * 通过昵称和用户名验证昵称是否可用
     * @param nick
     * @param userId
     * @return
     */
    public boolean checkNick(String nick, Integer userId) {
        boolean flag = true; // 默认可用
        String sql = "select nick from tb_user where nick = ? and userId != ?";
        Object[] params = {nick, userId};
        // 调用BaseDao的公用方法
        Object object = BaseDao.querySingValue(sql, params);
        if (object != null) { // 不可用
            flag = false;
        }
		/*Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select nick from tb_user where nick = ? and userId != ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nick);
			preparedStatement.setInt(2, userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String nickName = resultSet.getString("nick");
				 // 如果查询到昵称，说明昵称已被占用
				if (!StringUtil.isNullOrEmpty(nickName)) {
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(resultSet, preparedStatement, connection);
		}*/
        return flag;
    }

    /**
     * 修改用户信息
     * @param u
     * @return
     */
    public int updateInfo(User u) {
        String sql = "update tb_user set nick = ?, head = ?, mood= ?  where userId = ?";
        int row  = BaseDao.executeUpdate(sql, u.getNick(),u.getHead(),u.getMood(),u.getUserId());
        return row;
    }

}
