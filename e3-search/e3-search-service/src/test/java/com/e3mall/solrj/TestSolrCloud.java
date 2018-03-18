package com.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	@Test
	public void testAddDocument() throws Exception{
		//创建一个集群的丽娜姐，应该使用CloudSolrServer
		//zkHost:zookeeper的地址列表,以,号分隔
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.131:2181,192.168.25.131:2182,192.168.25.131:2183");
		//设置一个defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//接下来的工作和单机版时一样的。
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.setField("id", "solrcloud02");
		document.setField("item_title", "商品2 好东西");
		document.setField("item_price", 123);
		//把文件写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	//查询功能
	@Test
	public void testQuerydocument() throws SolrServerException{
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.131:2181,192.168.25.131:2182,192.168.25.131:2183");
		solrServer.setDefaultCollection("collection2");
		SolrQuery query = new SolrQuery();
		//query.set("q", "*:*");
		//query.setFields("item_title");
		query.setQuery("*:*");
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList results = queryResponse.getResults();
		System.out.println(results.getNumFound());
		for(SolrDocument document : results){
			System.out.println(document.getFieldValue("item_title"));
			System.out.println(document.getFieldValue("id"));
		}
	}
}
