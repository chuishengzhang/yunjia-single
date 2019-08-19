package com.zcs.yunjia.service;

import com.zcs.yunjia.pojo.EasyUITreeNode;
import com.zcs.yunjia.pojo.PortalCatResult;

import java.util.List;


/**
 * 商品列表接口
 *
 */
public interface ItemCatService {
	
	//查询商品类别列表
	public List<EasyUITreeNode> getItemCatList(Long parentId);
	//获取商品分类
	public PortalCatResult getItemCatList();
}
