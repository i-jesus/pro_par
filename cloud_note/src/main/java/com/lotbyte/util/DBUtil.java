package com.lotbyte.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * 数据库连接类
 * @author Administrator
 *
 */
public class DBUtil {

	/**
	 * 得到数据库的连接
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			// 得到配置文件的输入流，通过getResourceAsSteam(path),path的路径是classpath,前面不要加"/"
			InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
			// 将输入流加载到配置文件对象
			Properties properties = new Properties();
			// 使用properties.load()加载输入流
			properties.load(inputStream);
			
			// 通过properties.getProperty(key)取到对相应的value值
			String jdbcName = properties.getProperty("jdbcName"); // 驱动
			String dbUrl = properties.getProperty("dbUrl"); // 数据库的路径
			String dbUserName = properties.getProperty("dbUserName"); // 数据库的用户名
			String dbPassword = properties.getProperty("dbPassword"); // 数据库的用户密码
			
			// 加载驱动
			Class.forName(jdbcName);
			// 得到数据库的连接
			connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	/**
	 * 关闭数据库连接
	 * @param resultSet
	 * @param preparedStatement
	 * @param connection
	 */
	public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection){
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			
		}
	}
}
