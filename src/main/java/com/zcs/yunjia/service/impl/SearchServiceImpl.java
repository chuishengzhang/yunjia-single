package com.zcs.yunjia.service.impl;

import com.zcs.yunjia.pojo.RequestResult;
import com.zcs.yunjia.pojo.SearchItem;
import com.zcs.yunjia.pojo.SearchResult;
import com.zcs.yunjia.mapper.SearchItemMapper;
import com.zcs.yunjia.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索商品service
 * @author zcs
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 将数据导入到solr索引库
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public  RequestResult importSearchItemList(){
		List<SearchItem> searchItemList = searchItemMapper.importSearchItemList();
		RequestResult result = new RequestResult();
		//int count = 0;
		//导入到solr索引库
		for (SearchItem item : searchItemList) {
			//count++;
			SolrInputDocument docuemnt = new SolrInputDocument();
			docuemnt.addField("id",item.getId());
			docuemnt.addField("item_title", item.getTitle());
			docuemnt.addField("item_sell_point", item.getSell_point());
			docuemnt.addField("item_price", item.getPrice());
			docuemnt.addField("item_image", item.getImage());
			docuemnt.addField("item_cat_name", item.getItemCatName());
			docuemnt.addField("item_desc", item.getItem_desc());
			/*if(count == 5){
				break;
			}*/
			try {
				solrServer.add(docuemnt);
			} catch (Exception e) {
				e.printStackTrace();
				result.setStatus(444);
				return result;
			}
		}
		try {
			solrServer.commit();//提交
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(444);
			return result;
		} 
		
		result.setStatus(200);
		return result;
	}

	/**
	 * 搜索结果
	 * @param q 查询字段
	 * @param page 查询第几页
	 * @param rows 每页显示个数
	 * @throws SolrServerException 
	 */
	public SearchResult getSearchResult(String q,Integer page,Integer rows) throws SolrServerException {
		SearchResult result = new SearchResult();
		//创建查询
		SolrQuery query = new SolrQuery();
		//设置条件
		query.setQuery(q);
		//设置默认域
		query.set("df", "item_keywords");
		query.setStart(page == 0 ? 0 : page*rows);
		query.setRows(page == 0 ? rows : (page+1)*rows);
		//2.3设置高亮
		query.setHighlight(true);
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		query.addHighlightField("item_title");//设置高亮显示的域
		//执行查询
		QueryResponse response = solrServer.query(query);
		SolrDocumentList documentList = response.getResults();
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		result.setTotalPages((int)documentList.getNumFound()/rows);//设置总页数
		for (SolrDocument solrDoc : documentList) {
			SearchItem item = new SearchItem();
			item.setId(Long.parseLong((String)solrDoc.getFieldValue("id")));
			item.setTitle(solrDoc.getFieldValue("item_title")+"");
			item.setSell_point((String)solrDoc.getFieldValue("item_sell_point"));
            item.setPrice((Long)solrDoc.getFieldValue("item_price"));
            String images = solrDoc.getFieldValue("item_image")+"";
            //多张图片截取第一张图片显示
            item.setImage(images.split(",")[0]);
			item.setItemCatName(solrDoc.getFieldValue("item_cat_name")+"");
			item.setItem_desc(solrDoc.getFieldValue("item_desc")+"");
			itemList.add(item);
		}
		result.setItemList(itemList);
		System.out.println("total:"+result.getTotalPages());
		return result;
	}

}
