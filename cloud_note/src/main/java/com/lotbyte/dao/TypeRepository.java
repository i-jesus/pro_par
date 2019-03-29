package com.lotbyte.dao;

import java.util.List;
import java.util.Optional;

import com.lotbyte.base.BaseRepository;
import com.lotbyte.po.NoteType;
import com.lotbyte.util.StringUtil;

public class TypeRepository implements BaseRepository<NoteType> {

	/**
	 * 通过用户ID查询类型集合
	 * @param userId
	 * @return
	 */
	public Optional<List<NoteType>> findNoteTypeList(Integer userId) {
		String sql = "select typeid as typeId,typename as typeName,userid as userId from tb_note_type where userId = ?";
		return BaseRepository.super.queryRows(sql,NoteType.class,userId);
	}

	/**
	 * 验证类型名是否可使用
	 * @param userId
	 * @param typeId
	 * @param typeName
	 * @return
	 */
	public boolean checkTypeName(Integer userId, String typeId, String typeName) {
		String sql = "select typename as typeName from tb_note_type where userid = ? and typename = ? ";
		Object object = null;
		// 判断是验证添加操作还是修改操作
		if (StringUtil.isNullOrEmpty(typeId)) { // 验证添加操作
			Object[] params = {userId,typeName};
			object = BaseRepository.super.querySingValue(sql, params);
		} else { // 验证修改操作
			sql += " and typeId != ?";
			Object[] params = {userId,typeName,typeId};
			object = BaseRepository.super.querySingValue(sql, params);
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
			String sql = "insert into tb_note_type(userid,typename) values (?,?)";
			// 调用BaseDao的添加方法
			row = BaseRepository.update(sql, userId,typeName);
		} else { // 修改
			String sql = "update tb_note_type set typename = ? where userid = ? and typeid = ?";
			// 调用BaseDao的修改方法
			row =  BaseRepository.update(sql, typeName,userId,typeId);
		}

		return row;
	}

	/**
	 * 通过userId和typeName查询类型对象
	 * @param userId
	 * @param typeName
	 * @return
	 */
	public Optional<NoteType> findNoteType(Integer userId, String typeName) {
		String sql = "select userid as userId,typeid as typeId,typeName as typeName " +
				" from tb_note_type where userid = ? and typename = ?";
		return BaseRepository.super.queryObject(sql,NoteType.class,userId,typeName);
	}

	/**
	 * 通过typeId查询是否有子记录(通过类型ID查询对应类型的云记数量)
	 * @param typeId
	 * @return
	 */
	public Long countNoteNums(String typeId) {
		String sql = "select count(1) from tb_note where typeid = ?";
		Object[] params = {Integer.parseInt(typeId)};
		return (Long) BaseRepository.super.querySingValue(sql, params);
	}

	/**
	 * 删除类型
	 * @param typeId
	 * @return
	 */
	public int deleteType(String typeId) {
		String sql = "delete from tb_note_type where typeid = ?";
		Object[] params = {Integer.parseInt(typeId)};
		return BaseRepository.update(sql, params);
	}

	public Optional<NoteType> findNoteTypeListByUserIdAndTypeName(String typeName,Integer userId) {
		return BaseRepository.super.queryObject("select typeid as typeId,typename as typeName,userid as userId" +
						" from tb_note_type where typename = ? and userid= ?"
				,NoteType.class,typeName,userId);
	}

}
