package com.zcs.yunjia.service;


import com.zcs.yunjia.pojo.RequestResult;
import com.zcs.yunjia.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrServerException;


/**
 * 搜索接口
 * @author zcs
 */
public interface SearchService {
	//导入数据到索引库
	public RequestResult importSearchItemList();
	
	//根据条件搜索
	public SearchResult getSearchResult(String q, Integer page, Integer rows) throws SolrServerException;
	
}
