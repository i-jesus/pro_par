package com.lotbyte.service;

import java.util.List;

import com.lotbyte.dao.TypeDao;
import com.lotbyte.po.NoteType;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;

public class TypeService {
	
	private TypeDao typeDao = new TypeDao();

	/**
	 * 类型列表：
	 * 	Service层
	 * 	1、判断用户ID
	 * 	2、调用Dao层的查询方法，返回List
	 * 	3、判断List对象，如果未查询到数据，code=0，msg=暂未查询到此类信息
	 * 	4、如果List对象有值，将List放到resultInfo中
	 * 	5、返回resultInfo对象
	 * 
	 * @param userId
	 * @return
	 */
	public ResultInfo<List<NoteType>> findNoteTypeList(Integer userId) {
		ResultInfo<List<NoteType>> resultInfo = new ResultInfo<>();
		//判断用户ID
		if (userId == null || userId == 0) {
			resultInfo.setCode(0);
			resultInfo.setMsg("暂未查询到此类信息");
			return resultInfo;
		}
		// 调用Dao层的查询方法
		List<NoteType> noteTypeList = typeDao.findNoteTypeList(userId);
		
		//判断List对象，如果未查询到数据，code=0，msg=暂未查询到此类信息
		
		if (noteTypeList == null || noteTypeList.size() < 1) { // 说明当前用户没有类型集合
			resultInfo.setCode(0);
			resultInfo.setMsg("暂未查询到类型信息！");
			return resultInfo;
		} else {
			resultInfo.setCode(1);
			resultInfo.setMsg("查询成功!");
			//如果List对象有值，将List放到resultInfo的result中
			resultInfo.setResult(noteTypeList);
		}
		return resultInfo;
	}
	
	/**
	 * 添加或者修改操作
	 * 		1、判断参数是否为空（类型名称）
			2、调用Dao层的更新方法，返回受影响的行数						
			3、return resultInfo
	 * @param userId
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public ResultInfo<NoteType> addOrUpdate(Integer userId, String typeName, String typeId) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (userId == null || userId == 0) {
			resultInfo.setCode(0);
			resultInfo.setMsg("添加失败");
			return resultInfo;
		}
		if (StringUtil.isNullOrEmpty(typeName)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名称不能为空！");
			return resultInfo;
		}
		
		// 调用Dao层的添加方法，返回受影响的行数
		int row = typeDao.addOrUpdate(userId, typeName, typeId);
		if (row < 1) {
			resultInfo.setCode(0);
			resultInfo.setMsg("添加失败");
			return resultInfo;
		}
		resultInfo.setCode(1);
		resultInfo.setMsg("添加成功");
		return resultInfo;
		
	}


	/**
	 * 验证类型名的唯一性
	 * @param userId
	 * @param typeId
	 * @param typeName
	 * @return
	 */
	public ResultInfo<NoteType> checkTypeName(Integer userId, String typeId, String typeName) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (userId == null || userId == 0) {
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名不能为空！");
			return resultInfo;
		}
		if (StringUtil.isNullOrEmpty(typeName)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名不能为空");
			return resultInfo;
		}
		
		
		// 调用Dao层方法，通过类型名称查询类型对象
		NoteType noteType = typeDao.findNoteTypeListByUserId(typeName);
		
		//如果查询到数据，判断是添加操作还是修改操作
		if (noteType != null) {
			if (StringUtil.isNullOrEmpty(typeId)) {
				resultInfo.setCode(0);
				resultInfo.setMsg("类型名已存在，不可使用");
				return resultInfo;
			}else{//修改操作
				//判断是否是当前操作记录的主键，如果相等，说明是当前记录 ，可以使用，否则不可使用
				if (!typeId.equals(noteType.getTypeId()+"")) {
					resultInfo.setCode(0);
					resultInfo.setMsg("当前类型名已存在，不可使用");
					return resultInfo;
				}
			}
			
		}
		resultInfo.setCode(1);
		resultInfo.setMsg("当前类型名称不存在，可以使用！");
		return resultInfo;
	}

	
	/**
	 * 删除类型
	 * 	删除前需要查询是否有子记录
	 * @param typeId
	 * @return
	 */
	public ResultInfo<NoteType> deleteType(String typeId) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 判断参数是否为空
		if (StringUtil.isNullOrEmpty(typeId)) {
			resultInfo.setCode(0);
			resultInfo.setMsg("系统异常！");
			return resultInfo;
		}
		// 通过typeId查询是否有子记录(通过类型ID查询对应类型的云记数量)
		Long count = typeDao.countNoteNums(typeId);
		// 如果有子记录，提示删除失败，返回
		if (count > 0) {
			resultInfo.setCode(0);
			resultInfo.setMsg("存在子记录，不能删除！");
			return resultInfo;
		}
		
		// 如果没有子记录，执行删除，返回受影响的行数
		int row = typeDao.deleteType(typeId);
		
		// 判断是否删除成功
		if (row > 0) {
			resultInfo.setCode(1);
			resultInfo.setMsg("删除成功！");
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("删除失败！");
		}
		
		return resultInfo;
	}

}
