package com.lotbyte.service;


import com.lotbyte.dao.TypeRepository;
import com.lotbyte.po.NoteType;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public class TypeService {

    private TypeRepository typeRepository = new TypeRepository();

    /**
     * 类型列表：
     * Service层
     * 1、判断用户ID
     * 2、调用Dao层的查询方法，返回List
     * 3、判断List对象，如果未查询到数据，code=0，msg=暂未查询到此类信息
     * 4、如果List对象有值，将List放到resultInfo中
     * 5、返回resultInfo对象
     *
     * @param userId
     * @return
     */
    public ResultInfo findNoteTypeList(Integer userId) {
        ResultInfo resultInfo = new ResultInfo();
        return Optional.ofNullable(userId).filter(uid -> uid == 0).map((id) -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户id非空!");
            return resultInfo;
        }).orElseGet(() -> {
            Optional<List<NoteType>> noteTypeList = typeRepository.findNoteTypeList(userId);
            return noteTypeList.map((list -> {
                resultInfo.setCode(1);
                resultInfo.setMsg("查询成功!");
                resultInfo.setResult(list);
                return resultInfo;
            })).orElseGet(() -> {
                resultInfo.setCode(0);
                resultInfo.setMsg("暂未查询到类型信息！");
                return resultInfo;
            });
        });
    }

    /**
     * 添加或者修改操作
     * 1、判断参数是否为空（类型名称）
     * 2、调用Dao层的更新方法，返回受影响的行数
     * 3、return resultInfo
     *
     * @param userId
     * @param typeName
     * @param typeId
     * @return
     */
    public ResultInfo addOrUpdate(Integer userId, String typeName, String typeId) {
        ResultInfo resultInfo = new ResultInfo();
        return Optional.ofNullable(userId).filter(u -> u == 0).map((uid) -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户id非空!");
            return resultInfo;
        }).orElseGet(() -> {
            return Optional.ofNullable(typeName).filter(StringUtils::isBlank).map((t) -> {
                resultInfo.setCode(0);
                resultInfo.setMsg("类型名称不能为空！");
                return resultInfo;
            }).orElseGet(() -> {
                Integer row = typeRepository.addOrUpdate(userId, typeName, typeId);
                return Optional.ofNullable(row).filter(r -> r > 0).map((r) -> {
                    resultInfo.setCode(1);
                    resultInfo.setMsg("操作成功");
                    return resultInfo;
                }).orElseGet(() -> {
                    resultInfo.setCode(0);
                    resultInfo.setMsg("操作失败");
                    return resultInfo;
                });
            });
        });

    }


    /**
     * 验证类型名的唯一性
     *
     * @param userId
     * @param typeId
     * @param typeName
     * @return
     */
    public ResultInfo checkTypeName(Integer userId, String typeId, String typeName) {
        ResultInfo resultInfo = new ResultInfo();
        return Optional.ofNullable(userId).filter(u -> u == 0).map((uid) -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名不能为空！");
            return resultInfo;
        }).orElseGet(() -> {
            return Optional.ofNullable(typeName).filter(StringUtils::isBlank).map((t) -> {
                resultInfo.setCode(0);
                resultInfo.setMsg("类型名不能为空");
                return resultInfo;
            }).orElseGet(() -> {
                // 调用Dao层方法，通过类型名称查询类型对象
                Optional<NoteType> noteTypeOptional = typeRepository.findNoteTypeListByUserIdAndTypeName(typeName, userId);
                return noteTypeOptional.filter((nt) -> !typeId.equals(nt.getTypeId() + "")).map((t) -> {
                    resultInfo.setCode(0);
                    resultInfo.setMsg("当前类型名已存在，不可使用");
                    return resultInfo;
                }).orElseGet(() -> {
                    resultInfo.setCode(1);
                    resultInfo.setMsg("当前类型合法");
                    return resultInfo;
                });
            });
        });
    }


    /**
     * 删除类型
     * 删除前需要查询是否有子记录
     *
     * @param typeId
     * @return
     */
    public ResultInfo deleteType(String typeId) {
        ResultInfo resultInfo = new ResultInfo();
        return Optional.ofNullable(typeId).filter(StringUtils::isBlank).map((t) -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("系统异常！");
            return resultInfo;
        }).orElseGet(() -> {
            // 通过typeId查询是否有子记录(通过类型ID查询对应类型的云记数量)
            Long count = typeRepository.countNoteNums(typeId);
            return Optional.ofNullable(count).filter(c -> c > 0).map((c) -> {
                resultInfo.setCode(0);
                resultInfo.setMsg("存在子记录，不能删除！");
                return resultInfo;
            }).orElseGet(() -> {
                Integer row = typeRepository.deleteType(typeId);
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
        });
    }
}
