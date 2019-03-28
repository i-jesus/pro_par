package com.lotbyte.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.lotbyte.po.Note;
import com.lotbyte.util.DBUtil;
import com.lotbyte.util.StringUtil;

/**
 * 公用的增删改查方法
 * @author Administrator
 *
 */
public class BaseDao {

	/**
	 * 添加、修改、删除操作
	 * @param sql
	 * @param params 可变参数
	 * @return
	 */
	public static int executeUpdate (String sql, Object... params) {
		int row = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// 得到数据库连接
			connection = DBUtil.getConnection();
			// 设置事务不自动提交
			connection.setAutoCommit(false);
			// 预编译
			preparedStatement = connection.prepareStatement(sql);
			// 设置参数
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					// 循环设置参数
					preparedStatement.setObject(i+1, params[i]);
				}
			}
			// 执行更新
			row = preparedStatement.executeUpdate();
			// 如果更改成功，提交事务
			if (row > 0) {
				// 提交事务
				try {
					connection.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 事务回滚
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			// 关闭资源
			DBUtil.close(Optional.ofNullable(null), Optional.ofNullable(preparedStatement), Optional.ofNullable(connection));
		}
		
		return row;
	}
	
	/**
	 * 查询一个字段    场景： select count(1) from th_user
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Object querySingValue(String sql, Object[] params) {
		Object object = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 得到数据库连接
			connection = DBUtil.getConnection();
			// 预编译
			preparedStatement = connection.prepareStatement(sql);
			// 设置参数
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					// 循环设置
					preparedStatement.setObject(i+1, params[i]);
				}
			}
			// 执行查询
			resultSet = preparedStatement.executeQuery();
			// 解析结果集
			if (resultSet.next()) {
				object = resultSet.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			//DBUtil.close(resultSet, preparedStatement, connection);
		}
		
		return object;
	}
	
	/**
	 * 
	 * @param sql
	 *            要执行的sql
	 * @param params
	 *            参数
	 * @param clzz
	 *            查询的对象的 Class
	 * @return
	 */

	// 抑制警告
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List queryRows(String sql, Object[] params, Class clz) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(sql);
			if (null != params) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
			}
			// rs 查询的结果集中的数据 zs 000, ls 111
			rs = ps.executeQuery();
			// 元结果集 获取字段个数和字段名字 结果的描述
			ResultSetMetaData rsmd = rs.getMetaData();
			// 一共查询了几个字段， 一共要设置几个属性
			int fildNum = rsmd.getColumnCount();
			//
			while (rs.next()) {
				// 创建一个对象
				Object obj = clz.newInstance();
				// 给对象设置属性值
				for (int i = 0; i < fildNum; i++) {
					// 获取属性的名字， 字段的名字（别名）
					String fieldName = rsmd.getColumnLabel(i + 1);
					// 通过反射，通过指定列名得到实体类中的具体属性，获取某个属性 name
					Field fild = clz.getDeclaredField(fieldName);
					// 通过反射，拼接实体类中的set方法，并设置指定类型， set + 列名（列名首字母大写） name setName  fild.getType()：属性的类型
					Method method = clz.getDeclaredMethod("set" + StringUtil.firstChar2Upper(fieldName),
							fild.getType());
					// 将method放到newInstance()之后的对象中，并且将列名对应的结果设置到方法中
					method.invoke(obj, rs.getObject(fieldName));
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		} finally {
			//DBUtil.close(rs, ps, conn);
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		String sql="select noteId,title,content from tb_note";
		List list = BaseDao.queryRows(sql, null, Note.class);
		for (int i = 0; i < list.size(); i++) {
			Note note = (Note)list.get(i);
			System.out.println(note.getTitle());
		}
	}

	/**
	 * 查询单行记录
	 * 
	 * @param sql
	 * @param params
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object querySingleRow(String sql, Object[] params, Class clz) {
		Object obj = null;
		List list = queryRows(sql, params, clz);
		if (list != null && list.size() > 0) {
			obj = list.get(0);
		}
		return obj;
	}
	

	
}
