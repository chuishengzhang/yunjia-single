package com.zcs.yunjia.service;


import com.zcs.yunjia.pojo.EasyUITreeNode;
import com.zcs.yunjia.pojo.RequestResult;

import java.util.List;


/**
 * 分类管理接口
 *
 */
public interface CategoryService {
	
	//分类管理列表
	public List<EasyUITreeNode> getCategory(Long id);
	
	//增加分类
	public RequestResult createCategory(Long parenntId, String name);

	//分类重命名
	public List<EasyUITreeNode> updateCategoryName(Long id, String newName);
	
	//删除分类
	public RequestResult deleteCategory(Long id);
	
	
}
