package com.zcs.yunjia.controller;


import com.zcs.yunjia.pojo.DataGridResult;
import com.zcs.yunjia.pojo.RequestResult;
import com.zcs.yunjia.service.ContentService;
import com.zcs.yunjia.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ContentController {

	//注入service
	@Autowired
	private ContentService contentService;
	
	/**
	 * 显示id代表分类下分类的所有内容
	 * @param categoryId 分类id
	 * @param page 显示第几页
	 * row 一页显示的行数
	 */
	@RequestMapping(value="/content/query/list",method= RequestMethod.GET)
	@ResponseBody
	public DataGridResult getContentList(Long categoryId,Integer page,Integer rows){
		return contentService.getContentList(categoryId, page, rows);
	}
	
	/**
	 * 新增分类内容
	 * @param tbContent 表单数据
	 */
	@RequestMapping(value="/content/save",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult saveContent(TbContent tbContent){
		return contentService.saveContent(tbContent);
	}
	
	/**
	 * 修改分类内容
	 * @param tbContent 修改内容表单数据
	 */
	@RequestMapping(value="/rest/content/edit",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult editContent(TbContent tbContent){
		return contentService.editContent(tbContent);
	}
	
	/**
	 * 删除分类内容
	 *  id 删除分类内容的id
	 */
	@RequestMapping(value="/content/delete",method= RequestMethod.POST)
	@ResponseBody
	public RequestResult deleteContent(Long[] ids){
		return contentService.deleteContents(ids);
	}
}
