package com.lotbyte.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.lotbyte.base.BaseRepository;
import com.lotbyte.po.Note;
import com.lotbyte.util.StringUtil;


public class NoteDaoRepository implements BaseRepository<Note> {

    /**
     * 通过主键查询note对象
     *
     * @param noteId
     * @return
     */
    public Optional<Note> findNoteById(String noteId) {
        String sql = "select noteid as noteId,title,content,pubtime,n.typeid as typeId,typename as typeName from tb_note n INNER JOIN tb_note_type t on n.typeid = t.typeid where noteid =?";
        return BaseRepository.super.queryObject(sql, Note.class, noteId);
    }

    /**
     * 添加或者修改
     *
     * @param note
     * @return
     */
    public int noteEdit(Note note) {
        int row = 0; // 受影响的行数
        if (note.getNoteId() != null && note.getNoteId() > 0) { // 修改操作
            String sql = "update tb_note set title = ? ,content = ?, typeId = ?,pubtime = now() where noteid = ?";
            Object[] params = {note.getTitle(), note.getContent(), note.getTypeId(), note.getNoteId()};
            row = BaseRepository.update(sql, params);
        } else { // 添加
            String sql = "insert into tb_note (title,content,typeid,pubtime) values (?,?,?,now())";
            Object[] params = {note.getTitle(), note.getContent(), note.getTypeId()};
            row = BaseRepository.update(sql, params);
        }
        return row;
    }

    /**
     * 查询当前用户的云记总数量
     *
     * @param userId
     * @return
     */
    public Integer findNoteTotalCount(Integer userId, String title,
                                      String dateStr, String typeStr) {
        Integer total = 0;
        Object object = null;
        String sql = "select count(1) from tb_note n INNER JOIN tb_note_type t on n.typeid = t.typeid where userid = ?";
        if (!StringUtil.isNullOrEmpty(title)) { //标题模糊查询
            sql += " and title like concat('%',?,'%')";
            Object[] params = {userId, title};
            object = BaseRepository.super.querySingValue(sql, params);
        } else if (!StringUtil.isNullOrEmpty(dateStr)) {
            sql += " and DATE_FORMAT(pubtime,'%Y年%m月') = ?";
            Object[] params = {userId, dateStr};
            object = BaseRepository.super.querySingValue(sql, params);
        } else if (!StringUtil.isNullOrEmpty(typeStr)) {
            sql += " and t.typeid = ?";
            Object[] params = {userId, Integer.parseInt(typeStr)};
            object = BaseRepository.super.querySingValue(sql, params);
        } else { // 没有条件查询时
            Object[] params = {userId};
            object = BaseRepository.super.querySingValue(sql, params);
        }

        if (object != null) { // 如果查询到的object不为空，即给total赋值
            total = Integer.parseInt(object.toString());
        }
        return total;
    }

    /**
     * 分页查询云记集合
     *
     * @param userId
     * @param index
     * @param pageSize
     * @return
     */
    public Optional<List<Note>> findNoteListByPage(Integer userId, Integer index,
                                                   Integer pageSize, String title, String dateStr, String typeStr) {
        List params = new ArrayList();
        String sql = "select noteid as noteId,title,content,pubtime,n.typeid as typeId from tb_note n INNER JOIN tb_note_type t on n.typeid = t.typeid where userid = ? ";
        params.add(userId);
        // 标题模糊查询
        if (!StringUtil.isNullOrEmpty(title)) {
            sql += " and title like concat('%',?,'%') ";
            params.add(title);
        } else if (!StringUtil.isNullOrEmpty(dateStr)) { // 日期查询
            sql += " and DATE_FORMAT(pubtime,'%Y年%m月') = ?";
            params.add(dateStr);
        } else if (!StringUtil.isNullOrEmpty(typeStr)) { // 类型查询
            sql += " and t.typeid = ?";
            params.add(typeStr);
        }
        sql += " limit ?,?";
        params.add(index);
        params.add(pageSize);
        return BaseRepository.super.queryRows(sql,Note.class,params.toArray());
    }

    /**
     * 删除云记
     *
     * @param noteId
     * @return
     */
    public int deleteNote(String noteId) {
        String sql = "delete from tb_note where noteid = ?";
        return BaseRepository.update(sql, Integer.parseInt(noteId));
    }

    /**
     * 通过云记日期分组
     *
     * @param userId
     * @return
     */
    public Optional<List<Note>> findNoteGroupByDate(Integer userId) {

        String sql = "select count(noteid) as noteCount,DATE_FORMAT(pubtime,'%Y年%m月') as name "
                + " from tb_note n INNER JOIN tb_note_type t "
                + " on n.typeid = t.typeid where userid = ? "
                + " GROUP BY DATE_FORMAT(name,'%Y年%m月') "
                + " ORDER BY DATE_FORMAT(name,'%Y年%m月') desc";
        return BaseRepository.super.queryRows(sql,Note.class,userId);
    }

    public Optional<List<Note>> findNoteGroupByType(Integer userId) {
        String sql = "select typename as name,count(noteid) as noteCount, "
                + " t.typeid as typeId from tb_note n right JOIN "
                + " tb_note_type t on n.typeid = t.typeid "
                + " where userid = ? GROUP BY t.typeid "
                + " ORDER BY t.typeid";

        return BaseRepository.super.queryRows(sql,Note.class,userId);
    }

    public static void main(String[] args) {
        System.out.println(new NoteDaoRepository().findNoteGroupByDate(1).isPresent());
    }


}
