package com.lotbyte.service;


import com.lotbyte.dao.NoteDaoRepository;
import com.lotbyte.po.Note;
import com.lotbyte.util.Page;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("all")
public class NoteService {

    private NoteDaoRepository noteRepository = new NoteDaoRepository();

    /**
     * 通过主键查询note对象
     *
     * @param noteId
     * @return
     */
    public Note findNoteById(String noteId) {
        return (Note) Optional.ofNullable(noteId).map(n -> {
            return null;
        }).orElseGet(() -> {
            Optional<Note> noteOptional = noteRepository.findNoteById(noteId);
            return noteOptional.isPresent() ? noteOptional.get() : null;
        });
    }

    /**
     * 添加或修改
     *
     * @param noteId
     * @param title
     * @param content
     * @param typeId
     * @return
     */
    public ResultInfo noteEdit(String noteId, String title, String content, String typeId) {
        ResultInfo resultInfo = new ResultInfo();
        return Optional.ofNullable(title).filter(StringUtils::isBlank).map((t) -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("标题不能为空!");
            return resultInfo;
        }).orElseGet(() -> {
            return Optional.ofNullable(content).filter(StringUtils::isBlank).map((c) -> {
                resultInfo.setCode(0);
                resultInfo.setMsg("内容不能为空!");
                return resultInfo;
            }).orElseGet(() -> {
                return Optional.ofNullable(typeId).filter(StringUtils::isBlank).map((id) -> {
                    resultInfo.setCode(0);
                    resultInfo.setMsg("请选择类型!");
                    return resultInfo;
                }).orElseGet(() -> {
                    // 将参数放到对象中
                    Note note = new Note();
                    note.setContent(content);
                    note.setTitle(title);
                    note.setTypeId(Integer.parseInt(typeId));
                    Optional.ofNullable(noteId).filter(StringUtils::isNoneBlank).ifPresent((t) -> {
                        note.setNoteId(Integer.parseInt(noteId));
                    });
                    // 调用Dao层的更新方法，返回受影响的行数
                    Integer row = noteRepository.noteEdit(note);
                    return Optional.ofNullable(row).filter(r -> r > 0).map((r) -> {
                        resultInfo.setCode(1);
                        resultInfo.setMsg("更新成功！");
                        return resultInfo;
                    }).orElseGet(() -> {
                        resultInfo.setCode(0);
                        resultInfo.setMsg("更新失败！");
                        return resultInfo;
                    });
                });
            });
        });

    }

    /**
     * 分页查询云记列表
     *
     * @return
     */
    public ResultInfo findNoteListByPage(String pageNumStr,
                                         String pageSizeStr, Integer userId,
                                         String title, String dateStr, String typeStr) {
        ResultInfo resultInfo = new ResultInfo();
        // 判断参数是否为空,设置默认值
        // final 问题解决
        AtomicReference<Integer> pageNum = new AtomicReference<>(1);
        AtomicReference<Integer> pageSize = new AtomicReference<>(5);
        Optional.ofNullable(pageNumStr).filter(StringUtils::isNoneBlank).ifPresent((p) -> {
            pageNum.set(Integer.parseInt(p));
        });
        Optional.ofNullable(pageSizeStr).filter(StringUtils::isNoneBlank).ifPresent((p) -> {
            pageSize.set(Integer.parseInt(pageSizeStr));
        });
        // 调用Dao层的查询方法，得到当前用户的云记总数量
        Integer total = noteRepository.findNoteTotalCount(userId, title, dateStr, typeStr);
        return Optional.ofNullable(total).filter((t) -> t > 0).map((t) -> {
            // 得到分页对象
            Page<Note> page = new Page<Note>(pageNum.get(), pageSize.get(), total);
            // 调用Dao层查询云记列表，分页查询 limit 两个参数：1、从哪一条数据开始查询2、每次查询几条数据
            // 查询开始的位置
            Integer index = (pageNum.get() - 1) * pageSize.get();
            // 通过用户ID和分页参数查询云记列表
            Optional<List<Note>> listOptional = noteRepository.findNoteListByPage(userId, index, pageSize.get(), title, dateStr, typeStr);
            // 将list集合放到page对象中
            page.setDatas(listOptional.isPresent() ? listOptional.get() : null);
            // 将page对象放到resultInfo对象中
            resultInfo.setResult(page);
            resultInfo.setCode(1); // 查询成功
            return resultInfo;
        }).orElseGet(() -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("暂未查询云记列表！");
            return resultInfo;
        });
    }

    /**
     * 删除云记
     *
     * @param noteId
     * @return
     */
    public ResultInfo deleteNote(String noteId) {
        ResultInfo resultInfo = new ResultInfo();
        return Optional.ofNullable(noteId).filter(StringUtils::isBlank).map(n -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("参数异常！");
            return resultInfo;
        }).orElseGet(() -> {
            Integer row = noteRepository.deleteNote(noteId);
            return Optional.ofNullable(row).filter(r -> r > 0).map((r) -> {
                resultInfo.setCode(1);
                resultInfo.setMsg("删除成功！");
                return resultInfo;
            }).orElseGet(() -> {
                resultInfo.setCode(0);
                resultInfo.setMsg("删除失败！");
                return resultInfo;
            });
        });
    }

    /**
     * 通过云记日期分组
     *
     * @param userId
     * @return
     */
    public List<Note> findNoteGroupByDate(Integer userId) {
        Optional<List<Note>> listOptional = noteRepository.findNoteGroupByDate(userId);
        return listOptional.isPresent() ? listOptional.get() : null;
    }

    /**
     * 通过类型分组
     *
     * @param userId
     * @return
     */
    public List<Note> findNoteGroupByType(Integer userId) {
        Optional<List<Note>> list = noteRepository.findNoteGroupByType(userId);
        return list.isPresent() ? list.get() : null;
    }



}
