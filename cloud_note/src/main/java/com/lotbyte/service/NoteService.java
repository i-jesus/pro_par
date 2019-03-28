package com.lotbyte.service;

import java.util.List;

import com.lotbyte.dao.NoteDao;
import com.lotbyte.po.Note;
import com.lotbyte.po.NoteVo;
import com.lotbyte.util.Page;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;

public class NoteService {

	private NoteDao noteDao=new NoteDao() ;

	/**
	 * 通过主键查询note对象
	 * @param noteId
	 * @return
	 */
	public Note findNoteById(String noteId) {
		Note note = null;
		// 判断noteId是否为空
		if (StringUtil.isNullOrEmpty(noteId)) {
			return note;
		}
		// 调用Dao层的查询方法，查询到note对象
		note = noteDao.findNoteById(noteId);

		return note;
	}

	/**
	 * 添加或修改
	 * @param noteId
	 * @param title
	 * @param content
	 * @param typeId
	 * @return
	 */
	public ResultInfo<Note> noteEdit(String noteId, String title, String content, String typeId) {
		ResultInfo<Note> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (StringUtil.isNullOrEmpty(title)
			|| StringUtil.isNullOrEmpty(title) 
			|| StringUtil.isNullOrEmpty(typeId)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("参数不能为空");
		}
		// 将参数放到对象中
		Note note = new Note();
		note.setContent(content);
		note.setTitle(title);
		note.setTypeId(Integer.parseInt(typeId));
		if (!StringUtil.isNullOrEmpty(noteId)) {
			note.setNoteId(Integer.parseInt(noteId));
		}
		
		// 调用Dao层的更新方法，返回受影响的行数
		int row = noteDao.noteEdit(note);
		if (row > 0) { // 成功
			resultInfo.setCode(1);
			resultInfo.setMsg("更新成功！");
		} else { // 失败
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败！");
			resultInfo.setResult(note);
		}
		
		return resultInfo;
	}

	/**
	 * 分页查询云记列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResultInfo<Page<Note>> findNoteListByPage(String pageNumStr, 
			String pageSizeStr, Integer userId, 
			String title, String dateStr, String typeStr) {
		
		ResultInfo<Page<Note>> resultInfo = new ResultInfo<>();
		// 判断参数是否为空,设置默认值
		Integer pageNum = 1;
		if (!StringUtil.isNullOrEmpty(pageNumStr)) {
			pageNum = Integer.parseInt(pageNumStr);
		}
		Integer pageSize = 5;
		if (!StringUtil.isNullOrEmpty(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		// 调用Dao层的查询方法，得到当前用户的云记总数量
		Integer total = noteDao.findNoteTotalCount(userId, title, dateStr,typeStr);
		
		// 判断当前用户是否有云记
		if (total == null || total < 1) { //没有云记
			resultInfo.setCode(0);
			resultInfo.setMsg("暂未查询云记列表！");
			return resultInfo;
		}
		
		// 得到分页对象
		Page<Note> page = new Page<>(pageNum, pageSize, total);
		
		// 调用Dao层查询云记列表，分页查询 limit 两个参数：1、从哪一条数据开始查询2、每次查询几条数据
		// 查询开始的位置
		Integer index = (pageNum -1) * pageSize; 
		// 通过用户ID和分页参数查询云记列表
		List<Note> list = noteDao.findNoteListByPage(userId,index,pageSize,title, dateStr, typeStr);
		
		// 将list集合放到page对象中
		page.setDatas(list);
		
		// 将page对象放到resultInfo对象中
		resultInfo.setResult(page);
		resultInfo.setCode(1); // 查询成功
		
		return resultInfo;
	}

	/**
	 * 删除云记
	 * @param noteId
	 * @return
	 */
	public ResultInfo<Note> deleteNote(String noteId) {
		ResultInfo<Note> resultInfo = new ResultInfo<>();
		// 判断参数
		if (StringUtil.isNullOrEmpty(noteId)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("参数异常！");
			return resultInfo;
		}
		
		// 调用Dao层的删除方法
		int row = noteDao.deleteNote(noteId);
		if (row > 0) {
			resultInfo.setCode(1);
			resultInfo.setMsg("删除成功！");
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("删除失败！");
		}
		return resultInfo;
	}

	/**
	 * 通过云记日期分组
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByDate(Integer userId) {
		List<NoteVo> list = noteDao.findNoteGroupByDate(userId);
		return list;
	}

	/**
	 * 通过类型分组
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByType(Integer userId) {
		List<NoteVo> list = noteDao.findNoteGroupByType(userId);
		return list;
	}

}
