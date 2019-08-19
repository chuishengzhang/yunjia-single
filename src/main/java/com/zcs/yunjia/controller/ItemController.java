package com.zcs.yunjia.controller;


import com.zcs.yunjia.pojo.DataGridResult;
import com.zcs.yunjia.pojo.Item;
import com.zcs.yunjia.pojo.RequestResult;
import com.zcs.yunjia.pojo.TbItem;
import com.zcs.yunjia.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 商品显示相关controller
 *
 */
@Controller
public class ItemController {
	
	//注入service
	@Autowired
	private ItemService itemService;
	
	//查询商品列表
	@RequestMapping(value="/item/list",method= RequestMethod.GET)
	@ResponseBody
	public DataGridResult getItemList(Integer page,Integer rows){
		DataGridResult data = itemService.itemList(page, rows);
		return data;
	}
	
	/**
	 * 添加商品
	 */
	@RequestMapping(value="/item/save",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult saveItem(TbItem tbItem, String desc, String itemParams){
		return itemService.saveItem(tbItem, desc, itemParams);
	}
	
	/**
	 * 更新商品信息
	 * @param tbItem 前端表单数据
	 * @param desc 商品描述
	 */
	@RequestMapping(value="/rest/item/update",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult updateItem(TbItem tbItem, String desc){
		return itemService.updateItem(tbItem, desc);
	}
	
	/**
	 * 删除商品
	 * @param ids 要删除的商品id数组
	 */
	@RequestMapping(value="/rest/item/delete",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult deleteItems(Long[] ids){
		return itemService.deleteItems(ids);
	}
	
	/**
	 * 上架商品
	 * 商品status 1-表示正常   2-表示下架
	 * @param ids 要上架的商品的id
	 * @return status 200-成功  444-失败  333-该商品修改前已是上架状态
	 */
	@RequestMapping(value="/rest/item/instock",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult inStock(Long[] ids){
		return itemService.inStock(ids);
	}
	
	/**
	 * 下架商品
	 * 商品status 1-表示正常   2-表示下架
	 * @param ids 要下架的商品的id
	 * @return status 200-成功  444-失败  333-该商品修改前已是下架状态
	 */
	@RequestMapping(value="/rest/item/outstock",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult outStock(Long[] ids){
		return itemService.outStock(ids);
	}

	@RequestMapping("/item/{id}.html")
	public String getItemById(Model model, @PathVariable Long id){
		TbItem result = itemService.getItemById(id);
		Item item = tbItemToItem(result);
		item.setId(id);
		//将图片装换成数组注入
		item.setImages(result.getImage().split(","));
		model.addAttribute("item",item);
		return "item";
	}

	/**
	 * 将Tbitem对面转换成Item对象 除了图片数组  Item对象里的图片为数组
	 * @param tbItem
	 * @return Item对象
	 */
	private Item tbItemToItem(TbItem tbItem) {
		Item item = new Item();
		item.setTitle(tbItem.getTitle());
		item.setBarcode(tbItem.getBarcode());
		item.setCid(tbItem.getCid());
		item.setCreated(tbItem.getCreated());
		item.setNum(tbItem.getNum());
		item.setPrice(tbItem.getPrice());
		item.setSellPoint(tbItem.getSellPoint());
		item.setStatus(tbItem.getStatus());
		item.setUpdated(tbItem.getUpdated());
		return item;
	}
}
