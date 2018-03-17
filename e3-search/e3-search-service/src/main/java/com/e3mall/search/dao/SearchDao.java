package com.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.common.pojo.SearchResult;

/**
 * Solr商品查询dao
 * @author qiankeqin
 *
 */
@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	/**
	 * 根据查询条件查询索引库
	 * @param query
	 * @return
	 * @throws SolrServerException 
	 */
	public SearchResult search(SolrQuery query) throws Exception{
		//根据query查询商品索引库数据
		QueryResponse queryResponse = solrServer.query(query);
		//取结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//取查询结果总记录数
		long numFound = solrDocumentList.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecordCount(numFound);
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> searchItemList = new ArrayList<SearchItem>();
		//取商品列表，需要高亮显示
		for(SolrDocument document : solrDocumentList){
			SearchItem item = new SearchItem();
			item.setId((String) document.get("id"));
			item.setCategory_name((String) document.get("item_category_name"));
			item.setPrice((long)document.get("item_price"));
			item.setSell_point((String)document.get("item_sell_point"));
			item.setImage((String)document.get("item_image"));
			String title = "";
			List<String> list = highlighting.get(document.get("id")).get("item_title");
			if(list != null && list.size()>0){
				title = list.get(0);
			} else {
				title = (String)document.get("item_title");
			}
			item.setTitle(title);
			searchItemList.add(item);
		}
		result.setItemList(searchItemList);
		//返回结果
		return result;
	}
}
