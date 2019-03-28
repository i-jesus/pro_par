package com.lotbyte.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lotbyte.po.Note;
import com.lotbyte.po.NoteVo;
import com.lotbyte.util.DBUtil;
import com.lotbyte.util.StringUtil;

public class NoteDao {

	/**
	 * 通过主键查询note对象
	 * @param noteId
	 * @return
	 */
	public Note findNoteById(String noteId) {
		Note note = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select noteId,title,content,pubtime,n.typeid as typeId,typeName from tb_note n INNER JOIN tb_note_type t on n.typeid = t.typeid where noteid =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, Integer.parseInt(noteId));
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				note = new Note();
				note.setContent(resultSet.getString("content"));
				note.setNoteId(Integer.parseInt(noteId));
				note.setPubtime(resultSet.getDate("pubtime"));
				note.setTitle(resultSet.getString("title"));
				note.setTypeId(resultSet.getInt("typeid"));
				note.setTypeName(resultSet.getString("typeName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		
		return note;
	}

	/**
	 * 添加或者修改
	 * @param note
	 * @return
	 */
	public int noteEdit(Note note) {
		int row = 0; // 受影响的行数
		if (note.getNoteId() != null && note.getNoteId() > 0) { // 修改操作
			String sql = "update tb_note set title = ? ,content = ?, typeId = ?,pubtime = now() where noteId = ?";
			Object[] params = {note.getTitle(),note.getContent(),note.getTypeId(), note.getNoteId()};
			row = BaseDao.executeUpdate(sql, params);
		} else { // 添加
			String sql = "insert into tb_note (title,content,typeId,pubtime) values (?,?,?,now())";
			Object[] params = {note.getTitle(),note.getContent(),note.getTypeId()};
			row = BaseDao.executeUpdate(sql, params);
		}
		return row;
	}

	/**
	 * 查询当前用户的云记总数量
	 * @param userId
	 * @return
	 */
	public Integer findNoteTotalCount(Integer userId, String title,
			String dateStr,String typeStr) {
		Integer total = 0;
		Object object = null;
		String sql = "select count(1) from tb_note n INNER JOIN tb_note_type t on n.typeid = t.typeid where userid = ?";
		if (!StringUtil.isNullOrEmpty(title)) { //标题模糊查询
			sql += " and title like concat('%',?,'%')";
			Object[] params = {userId,title};
			object = BaseDao.querySingValue(sql, params);
		} else if (!StringUtil.isNullOrEmpty(dateStr)){
			sql += " and DATE_FORMAT(pubtime,'%Y年%m月') = ?";
			Object[] params = {userId,dateStr};
			object = BaseDao.querySingValue(sql, params);
		} else if (!StringUtil.isNullOrEmpty(typeStr)){
			sql += " and t.typeId = ?";
			Object[] params = {userId,Integer.parseInt(typeStr)};
			object = BaseDao.querySingValue(sql, params);
		} else { // 没有条件查询时
			Object[] params = {userId};
			object = BaseDao.querySingValue(sql, params);
		}
		 
		if (object != null) { // 如果查询到的object不为空，即给total赋值
			total = Integer.parseInt(object.toString());
		}
		return total;
	}

	/**
	 * 分页查询云记集合
	 * @param userId
	 * @param index
	 * @param pageSize
	 * @return
	 */
	public List<Note> findNoteListByPage(Integer userId, Integer index, 
			Integer pageSize, String title, String dateStr, String typeStr) {
		List<Note> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select noteId,title,content,pubtime,n.typeid as typeId from tb_note n INNER JOIN tb_note_type t on n.typeid = t.typeid where userid = ? ";
			// 标题模糊查询
			if (!StringUtil.isNullOrEmpty(title)) {
				sql += " and title like concat('%',?,'%') ";
			} else if (!StringUtil.isNullOrEmpty(dateStr)) { // 日期查询
				sql += " and DATE_FORMAT(pubtime,'%Y年%m月') = ?"; 
			} else if (!StringUtil.isNullOrEmpty(typeStr)) { // 类型查询
				sql += " and t.typeId = ?"; 
			}
			sql += " limit ?,?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			// 标题模糊查询
			if (!StringUtil.isNullOrEmpty(title)) {
				preparedStatement.setString(2, title);
				preparedStatement.setInt(3, index);
				preparedStatement.setInt(4, pageSize);
			} else if (!StringUtil.isNullOrEmpty(dateStr)) { // 云记日期查询
				preparedStatement.setString(2, dateStr);
				preparedStatement.setInt(3, index);
				preparedStatement.setInt(4, pageSize);
			} else if (!StringUtil.isNullOrEmpty(typeStr)) { // 云记类型查询
				preparedStatement.setInt(2, Integer.parseInt(typeStr));
				preparedStatement.setInt(3, index);
				preparedStatement.setInt(4, pageSize);
			} else { // 无条件查询
				preparedStatement.setInt(2, index);
				preparedStatement.setInt(3, pageSize);
			}
			
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Note note = new Note();
				note.setContent(resultSet.getString("content"));
				note.setNoteId(resultSet.getInt("noteId"));
				note.setPubtime(resultSet.getDate("pubtime"));
				note.setTitle(resultSet.getString("title"));
				note.setTypeId(resultSet.getInt("typeId"));
				// 将对象加到集合中
				list.add(note);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		return list;
	}

	/**
	 * 删除云记
	 * @param noteId
	 * @return
	 */
	public int deleteNote(String noteId) {
		String sql = "delete from tb_note where noteId = ?";
		int row = BaseDao.executeUpdate(sql, Integer.parseInt(noteId));
		return row;
	}

	/**
	 * 通过云记日期分组
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByDate(Integer userId) {
		List<NoteVo> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select count(noteid) as noteCount,DATE_FORMAT(pubtime,'%Y年%m月') as pubtime "
					+ " from tb_note n INNER JOIN tb_note_type t "
					+ " on n.typeid = t.typeid where userid = ? "
					+ " GROUP BY DATE_FORMAT(pubtime,'%Y年%m月') "
					+ " ORDER BY DATE_FORMAT(pubtime,'%Y年%m月') desc";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				NoteVo noteVo = new NoteVo();
				noteVo.setName(resultSet.getString("pubtime"));
				noteVo.setNoteCount(resultSet.getInt("noteCount"));
				// 将对象加到集合中
				list.add(noteVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		return list;
	}

	public List<NoteVo> findNoteGroupByType(Integer userId) {
		List<NoteVo> list = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String sql = "select typename,count(noteId) as noteCount, "
					+ " t.typeid as typeId from tb_note n right JOIN "
					+ " tb_note_type t on n.typeid = t.typeid "
					+ " where userid = ? GROUP BY t.typeid "
					+ " ORDER BY t.typeid";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				NoteVo noteVo = new NoteVo();
				noteVo.setName(resultSet.getString("typename"));
				noteVo.setNoteCount(resultSet.getInt("noteCount"));
				noteVo.setTypeId(resultSet.getInt("typeId"));
				// 将对象加到集合中
				list.add(noteVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		return list;
	}

}
