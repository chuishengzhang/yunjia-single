package com.zcs.yunjia.service.impl;

import com.zcs.yunjia.pojo.*;
import com.zcs.yunjia.utils.JsonUtils;
import com.zcs.yunjia.service.ItemCatService;
import com.zcs.yunjia.jedis.JedisClient;
import com.zcs.yunjia.mapper.TbItemCatMapper;
import com.zcs.yunjia.pojo.TbItemCatExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	//注入dao
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${PORTAL_CAT_KEY}")
	private String portal_cat_key;

	/**
	 * 商品类别列表
	 */
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//查询所有
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> itemCats = tbItemCatMapper.selectByExample(example);

		List<EasyUITreeNode> results = new ArrayList<EasyUITreeNode>();
		for(TbItemCat tic : itemCats){
			EasyUITreeNode result = new EasyUITreeNode();
			result.setId(tic.getId());
			result.setState(tic.getIsParent()?"closed":"open");
			result.setText(tic.getName());
			results.add(result);
		}
		return results;
	}
	/**
	 * 首页商品类目展示
	 * @return 首页类目展示pojo
	 */
	public PortalCatResult getItemCatList() {
		try{
			if(jedisClient.exists(portal_cat_key)){
				//存在缓存,从缓存中取
				System.out.println(portal_cat_key+"有缓存！！");
				return JsonUtils.jsonToPojo(jedisClient.get(portal_cat_key), PortalCatResult.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//执行查询
		List nodes = getCatListByParentId(0l);
		PortalCatResult result = new PortalCatResult();
		result.setData(nodes);
		try{
			//存入缓存中
			System.out.println(portal_cat_key+"没有缓存！！");
			jedisClient.set(portal_cat_key, JsonUtils.objectToJson(result));
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据父节点下的所有子节点
	 * @param parentId 父节点
	 * @return 子节点集合
	 */
	public List<?> getCatListByParentId(Long parentId){
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		List nodes = new ArrayList();
		for (TbItemCat item : list) {
			if(item.getIsParent()){//为父节点
				PortalCatNode node = new PortalCatNode();
				node.setUrl("/products/"+item.getId()+".html");
				//判断是否为顶层节点
				if(item.getParentId() == 0){
					node.setName("<a href='/products/"+item.getId()+".html'>"+item.getName()+"</a>");
				}else{//不是顶层节点
					node.setName(item.getName());
				}
				node.setItems(getCatListByParentId(item.getId()));
				nodes.add(node);
			}else{//为叶子节点
				nodes.add("/products/"+item.getId()+".html|"+item.getName());
			}
		}
		return nodes;
	}

}
