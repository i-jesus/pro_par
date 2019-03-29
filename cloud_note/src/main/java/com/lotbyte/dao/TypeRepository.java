package com.lotbyte.dao;

import java.util.*;

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
	 * 添加或者修改类型
	 * @param userId
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public int addOrUpdate(Integer userId, String typeName, String typeId) {
		StringBuilder sqlBuilder =new StringBuilder();
		List params=new ArrayList();

		// Optinal 消除if else 判断操作
		Optional.ofNullable(typeId).filter((id)->id.trim().equals("")).map((str) -> {
			// id 为空值时
			sqlBuilder.append("insert into tb_note_type(userid,typename) values (?,?)");
			params.add(userId);
			params.add(typeName);
			return sqlBuilder.toString();
		}).orElseGet(() -> {
					// id 不空时
					sqlBuilder.append("update tb_note_type set typename = ? where userid = ? and typeid = ?");
					params.add(typeName);
					params.add(userId);
					params.add(typeId);
					return sqlBuilder.toString();
				}
		);
		return BaseRepository.update(sqlBuilder.toString(),params.toArray());
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


	public static void main(String[] args) {
		//new TypeRepository().addOrUpdate(1,"abcdeeeee","80");

	}
}
