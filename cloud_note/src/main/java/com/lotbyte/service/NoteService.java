package com.lotbyte.service;



import com.lotbyte.dao.NoteDaoRepository;
import com.lotbyte.po.Note;
import com.lotbyte.util.Page;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;

import java.util.List;
import java.util.Optional;

public class NoteService {

	private NoteDaoRepository noteRepository=new NoteDaoRepository() ;

	/**
	 * 通过主键查询note对象
	 * @param noteId
	 * @return
	 */
	public Note findNoteById(String noteId) {
		// 判断noteId是否为空
		if (StringUtil.isNullOrEmpty(noteId)) {
			return null;
		}
		// 调用Dao层的查询方法，查询到note对象
		Optional<Note> noteOptional = noteRepository.findNoteById(noteId);
		return noteOptional.isPresent()?noteOptional.get():null;
	}

	/**
	 * 添加或修改
	 * @param noteId
	 * @param title
	 * @param content
	 * @param typeId
	 * @return
	 */
	public ResultInfo noteEdit(String noteId, String title, String content, String typeId) {
		ResultInfo resultInfo = new ResultInfo();
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
		int row = noteRepository.noteEdit(note);
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
	 * @return
	 */
	public ResultInfo findNoteListByPage(String pageNumStr,
													 String pageSizeStr, Integer userId,
													 String title, String dateStr, String typeStr) {

		ResultInfo resultInfo = new ResultInfo();
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
		Integer total = noteRepository.findNoteTotalCount(userId, title, dateStr,typeStr);

		// 判断当前用户是否有云记
		if (total == null || total < 1) { //没有云记
			resultInfo.setCode(0);
			resultInfo.setMsg("暂未查询云记列表！");
			return resultInfo;
		}
		// 得到分页对象
		Page<Note> page = new Page<Note>(pageNum, pageSize, total);

		// 调用Dao层查询云记列表，分页查询 limit 两个参数：1、从哪一条数据开始查询2、每次查询几条数据
		// 查询开始的位置
		Integer index = (pageNum -1) * pageSize;
		// 通过用户ID和分页参数查询云记列表
		Optional<List<Note>> listOptional = noteRepository.findNoteListByPage(userId,index,pageSize,title, dateStr, typeStr);

		// 将list集合放到page对象中
		page.setDatas(listOptional.isPresent()?listOptional.get():null);
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
	public ResultInfo deleteNote(String noteId) {
		ResultInfo resultInfo = new ResultInfo();
		// 判断参数
		if (StringUtil.isNullOrEmpty(noteId)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("参数异常！");
			return resultInfo;
		}

		// 调用Dao层的删除方法
		int row = noteRepository.deleteNote(noteId);
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
	public List<Note> findNoteGroupByDate(Integer userId) {
		Optional<List<Note>> listOptional = noteRepository.findNoteGroupByDate(userId);
		return listOptional.isPresent()?listOptional.get():null;
	}

	/**
	 * 通过类型分组
	 * @param userId
	 * @return
	 */
	public List<Note> findNoteGroupByType(Integer userId) {
		Optional<List<Note>> list = noteRepository.findNoteGroupByType(userId);
		return list.isPresent()?list.get():null;
	}

}
