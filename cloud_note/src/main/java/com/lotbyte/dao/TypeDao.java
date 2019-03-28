package com.lotbyte.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.lotbyte.po.NoteType;
import com.lotbyte.util.DBUtil;
import com.lotbyte.util.StringUtil;

public class TypeDao {

	/**
	 * 通过用户ID查询类型集合
	 * @param userId
	 * @return
	 */
	public List<NoteType> findNoteTypeList(Integer userId) {
		List<NoteType> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select * from tb_note_type where userid = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				NoteType type = new NoteType();
				type.setTypeId(resultSet.getInt("typeid"));
				type.setTypeName(resultSet.getString("typename"));
				type.setUserId(userId);
				// 加到集合中
				list.add(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//DBUtil.close(resultSet, preparedStatement, connection);
		}
		return list;
	}

	/**
	 * 验证类型名是否可使用
	 * @param userId
	 * @param typeId
	 * @param typeName
	 * @return
	 */
	public boolean checkTypeName(Integer userId, String typeId, String typeName) {
		String sql = "select typeName from tb_note_type where userId = ? and typeName = ? ";
		Object object = null;
		// 判断是验证添加操作还是修改操作
		if (StringUtil.isNullOrEmpty(typeId)) { // 验证添加操作
			Object[] params = {userId,typeName};
			object = BaseDao.querySingValue(sql, params);
			
		} else { // 验证修改操作
			sql += " and typeId != ?";
			Object[] params = {userId,typeName,typeId};
			object = BaseDao.querySingValue(sql, params);
		}
		if (object == null) {
			return true;
		}
		return false;
	}

	/**
	 * 添加或者修改类型
	 * @param userId
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public int addOrUpdate(Integer userId, String typeName, String typeId) {
		int row = 0;
		// 判断是添加操作还是修改操作
		if (StringUtil.isNullOrEmpty(typeId)) { // 添加
			String sql = "insert into tb_note_type (userid,typename) values (?,?)";
			// 调用BaseDao的添加方法
			row = BaseDao.executeUpdate(sql, userId,typeName);
		} else { // 修改
			String sql = "update tb_note_type set typename = ? where userId = ? and typeId = ?";
			// 调用BaseDao的修改方法
			row =  BaseDao.executeUpdate(sql, typeName,userId,typeId);
		}
		
		return row;
	}

	/**
	 * 通过userId和typeName查询类型对象
	 * @param userId
	 * @param typeName
	 * @return
	 */
	public NoteType findNoteType(Integer userId, String typeName) {
		NoteType noteType = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select userId,typeId,TypeName from tb_note_type where userid = ? and typeName = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2,typeName);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				noteType = new NoteType();
				noteType.setTypeId(resultSet.getInt("typeId"));
				noteType.setTypeName(typeName);
				noteType.setUserId(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//DBUtil.close(resultSet, preparedStatement, connection);
		}
		return noteType;
	}

	/**
	 * 通过typeId查询是否有子记录(通过类型ID查询对应类型的云记数量)
	 * @param typeId
	 * @return
	 */
	public Long countNoteNums(String typeId) {
		String sql = "select count(1) from tb_note where typeid = ?";
		Object[] params = {Integer.parseInt(typeId)};
		Long count =  (Long) BaseDao.querySingValue(sql, params);
		return count;
	}

	/**
	 * 删除类型
	 * @param typeId
	 * @return
	 */
	public int deleteType(String typeId) {
		String sql = "delete from tb_note_type where typeId = ?";
		Object[] params = {Integer.parseInt(typeId)};
		int row = BaseDao.executeUpdate(sql, params);
		return row;
	}

	public NoteType findNoteTypeListByUserId(String typeName) {
		// TODO Auto-generated method stub
		return null;
	}

}
