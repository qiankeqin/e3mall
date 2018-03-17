package com.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.dao.SearchDao;
import com.e3mall.search.service.SearchService;

/**
 * 商品搜索Service
 * @author qiankeqin
 *
 */
@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SearchDao searchDao;
	
	/**
	 * 商品搜索方法
	 * keyword：搜索关键字
	 * page：第几页
	 * rows：每页显示
	 */
	@Override
	public SearchResult search(String keyword, int page, int rows) {
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery(keyword);
		//设置分页条件
		if(page<=0) page=1;
		query.setStart(rows*(page-1));
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_title");
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//调用dao执行查询
		SearchResult result = null;
		try {
			result = searchDao.search(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//计算总页数
		int totalPages = (int) Math.ceil(result.getRecordCount()/rows);
		result.setTotalPages(totalPages);
		//返回结果
		return result;
	}

}
